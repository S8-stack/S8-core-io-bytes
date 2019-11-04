package com.qx.base.bytes;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ByteFile {


	public static final boolean IS_DELETING_ILL_FORMATED_FILES = false;

	private Path path;

	private boolean isAutosaveActive = false;

	public ByteFile(Path path) {
		super();
		this.path = path;
	}


	/**
	 * To be overriden
	 * @return
	 */
	public int getFileBufferingSize() {
		return 1024;
	}

	public Path getFilePath() {
		return path;
	}


	/**
	 * <p><b>/!\ : Key assumption is that WE KNOW what type of file we are about to parse</b>
	 * This is mostly the case in the scope of Bk project.
	 * </p>
	 * @param inflow
	 * @throws BkException
	 * @throws ByteFileReadingException
	 * @throws IOException
	 */
	public abstract void read(ByteInflow inflow) throws ByteFileReadingException, IOException;

	/**
	 * 
	 * @param outflow
	 * @throws BkException
	 * @throws IOException
	 */
	public abstract void write(ByteOutflow outflow) throws ByteFileWritingException, IOException;


	public boolean isExisting() {
		return Files.exists(path);
	}

	/**
	 * Test if file is existing
	 * 
	 * @return a flag indicating if loading is successful
	 * @throws BkException
	 * @throws IOException 
	 */
	public boolean load() throws ByteFileReadingException, IOException {
		if(Files.exists(path)) {

			try {
				FileChannel channel = FileChannel.open(path, CREATE, READ);
				FileByteInflow inflow = new FileByteInflow(channel, getFileBufferingSize());
				inflow.pull();

				read(inflow);
				channel.close();
			}
			catch (ByteFileReadingException | IOException e) {
				//System.out.println("");

				// delete file
				if(IS_DELETING_ILL_FORMATED_FILES) {
					Files.delete(path);	
				}
				return false;
			}
			return true;
		}
		else {
			return false;
		}
	}




	/**
	 * 
	 * @param bucket
	 * @throws Exception
	 */
	public void save() throws ByteFileWritingException, IOException {


		// ensure directories are created
		Path folderpath = path.getParent();
		if(!Files.exists(folderpath)) {
			Files.createDirectories(folderpath);
		}
		FileChannel channel = FileChannel.open(path, CREATE, WRITE).truncate(0);
		FileByteOutflow outflow = new FileByteOutflow(channel, getFileBufferingSize());

		write(outflow);

		// flush stream
		outflow.push();

		// close channel
		channel.close();
	}


	public void delete() throws IOException {
		Files.delete(path);
	}



	public void startAutosave(long frequency) {
		Thread autosaveThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while(isAutosaveActive) {
					// wait for 5 min
					try {
						Thread.sleep(frequency);
					} 
					catch (InterruptedException e) {
						e.printStackTrace();
					}	

					// save
					try {
						save();
					} 
					catch (ByteFileWritingException | IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		isAutosaveActive = true;
		autosaveThread.start();
	}

	public void stopAutosave() {
		isAutosaveActive = false;
	}

}
