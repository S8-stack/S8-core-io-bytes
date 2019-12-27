package com.qx.level0.utilities.bytes;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;



/**
 * 
 * @author pc
 *
 */
public abstract class ByteFile {

	public static final int DEFAULT_BUFFER_CAPACITY = 1024;

	public static final boolean IS_DELETING_ILL_FORMATED_FILES = false;

	private Path path;

	private boolean isAutosaveActive = false;
	
	private int capacity = DEFAULT_BUFFER_CAPACITY;

	public ByteFile(Path path) {
		super();
		this.path = path;
	}
	
	public ByteFile(Path path, int capacity) {
		super();
		this.path = path;
		this.capacity = capacity;
	}


	/**
	 * To be overriden
	 * @return
	 */
	public int getBufferCapacity() {
		return capacity;
	}

	public Path getFilePath() {
		return path;
	}

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
	
	public static void load(Path path, ByteFileReader consumer) {
		load(path, DEFAULT_BUFFER_CAPACITY, consumer);
	}

	/**
	 * Test if file is existing
	 * 
	 * @return a flag indicating if loading is successful
	 * @throws BkException
	 * @throws IOException 
	 */
	public static void load(Path path, int capacity, ByteFileReader consumer) {
		if(Files.exists(path)) {

			try {
				FileChannel channel = FileChannel.open(path, CREATE, READ);
				FileByteInflow inflow = new FileByteInflow(channel, capacity);
				inflow.pull();

				consumer.consume(inflow);
				channel.close();
			}
			catch (IOException e) {
				consumer.onIOException(e);
			}
		}
		else {
			consumer.onFileDoesNotExist();
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
		FileByteOutflow outflow = new FileByteOutflow(channel, getBufferCapacity());

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
