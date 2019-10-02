package com.qx.base.files;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import com.qx.base.bytes.ByteInput;
import com.qx.base.bytes.ByteOutput;
import com.qx.base.bytes.FileByteInput;
import com.qx.base.bytes.FileByteOutput;

public abstract class QxFile {
	
	
	public static final boolean IS_DELETING_ILL_FORMATED_FILES = false;

	private Path path;
	
	private boolean hasBeenModified = false;

	private boolean isAutosaveActive = false;

	public QxFile(Path path) {
		super();
		this.path = path;
	}


	/**
	 * To be overriden
	 * @return
	 */
	public int getFileBufferingSize() {
		return 64;
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
	 * @throws QxFileReadingException
	 * @throws IOException
	 */
	public abstract void read(ByteInput inflow) throws QxFileReadingException, IOException;

	/**
	 * 
	 * @param outflow
	 * @throws BkException
	 * @throws IOException
	 */
	public abstract void write(ByteOutput outflow) throws QxFileWritingException, IOException;



	/**
	 * Notify that the file has been modified
	 */
	public void hasBeenModified() {
		hasBeenModified = true;
	}
	
	
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
	public boolean load() throws QxFileReadingException, IOException {
		if(Files.exists(path)) {
			
			try {
				FileChannel channel = FileChannel.open(path, CREATE, READ);
				FileByteInput inflow = new FileByteInput(channel, getFileBufferingSize());
				inflow.pull();

				read(inflow);
				channel.close();
				hasBeenModified = false;
			}
			catch (QxFileReadingException | IOException e) {
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
	public void save() throws QxFileWritingException, IOException {

		if(hasBeenModified) {
			// ensure directories are created
			Path folderpath = path.getParent();
			if(!Files.exists(folderpath)) {
				Files.createDirectories(folderpath);
			}
			FileChannel channel = FileChannel.open(path, CREATE, WRITE).truncate(0);
			FileByteOutput outflow = new FileByteOutput(channel, getFileBufferingSize());

			write(outflow);

			// flush stream
			outflow.push();

			// close channel
			channel.close();	
			
			hasBeenModified = false;
		}
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
					catch (QxFileWritingException | IOException e) {
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
