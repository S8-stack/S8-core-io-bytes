package com.s8.io.bytes;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import com.s8.api.bytes.ByteOutflow;



/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public abstract class ByteFile {

	public static final int DEFAULT_BUFFER_CAPACITY = 1024;

	public static final boolean IS_DELETING_ILL_FORMATED_FILES = false;

	final private Path path;

	//volatile private boolean isAutosaveActive = false;
	
	private int capacity = DEFAULT_BUFFER_CAPACITY;
	


	public ByteFile(Path path, int capacity) {
		super();
		this.path = path;
		this.capacity = capacity;
	}
	
	public ByteFile(Path path) {
		super();
		this.path = path;
		capacity = DEFAULT_BUFFER_CAPACITY;
	}
	
	
	public Path getPath() {
		return path;
	}
	


	/**
	 * To be overriden
	 * @return
	 */
	public int getBufferCapacity() {
		return capacity;
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
	
	
	/*
	public static void load(Path path, ByteFileLoader consumer) {
		load(path, DEFAULT_BUFFER_CAPACITY, consumer);
	}
	 */
	



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



	/*
	public void startAutosave(long frequency) {
		Thread autosaveThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while(isAutosaveActive) {
					// wait for 5 min
				
					// save
					try {
						save();
					} 
					catch ( IOException e) {
						e.printStackTrace();
					}
					
					try {
						Thread.sleep(frequency);
					} 
					catch (InterruptedException e) {
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
	*/

}
