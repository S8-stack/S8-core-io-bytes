package com.s8.io.bytes.linked;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class LinkedBytesIO {
	
	

	
	
	

	/**
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static LinkedBytes read(Path path, boolean isVerbose) throws IOException {

		FileChannel channel = null;
		try {
			// ensure directories are created
			Path folderpath = path.getParent();
			if(!Files.exists(folderpath)) {
				Files.createDirectories(folderpath);
			}

			// open channel
			channel = FileChannel.open(path, READ);
			
			LinkedBytes head = null, tail = null;

			// parse
			boolean hasNext = true;
			while(hasNext) {
				
				LinkedBytesHeader header = LinkedBytesHeader.parse(channel);
				
				LinkedBytes chunk = new LinkedBytes(header.getLength());
				
				readBuffer(channel, chunk.wrap());
				
				hasNext = header.hasNext();
				
				if(head != null) { 
					tail.next = chunk; tail = chunk;
				}
				else {
					head = chunk; tail = chunk;
				}
			}

			// follow-up
			return head;
		} 
		finally {
			if(channel!=null) {
				try {
					channel.close();
				} 
				catch (IOException e) {
					if(isVerbose) {
						e.printStackTrace();	
					}
				}
			}
		}
	}



	



	
	/**
	 * 
	 * @return
	 * @throws IOException 
	 */
	public static void write(LinkedBytes head, Path path, boolean isVerbose) throws IOException {
		
		FileChannel channel = null;
		
		try {
			// ensure directories are created
			Path folderpath = path.getParent();
			if(!Files.exists(folderpath)) {
				Files.createDirectories(folderpath);
			}
			channel = FileChannel.open(path, CREATE, WRITE);
			
			if(isVerbose) {
				long bytecount = head.getBytecount();
				System.out.println("[LinkedBytesIO] bytecount to be written: "+bytecount);
			}

			LinkedBytes chunk = head;
			// is sending
			while(chunk != null) {
				

				// write header
				LinkedBytesHeader header = new LinkedBytesHeader(chunk.next != null, chunk.length);
				header.compose(channel);
				
				// write body
				ByteBuffer buffer = chunk.wrap();
				writeBuffer(channel, buffer);

				// move to next chunk
				chunk = chunk.next;
			}
		} 
		finally {
			if(channel!=null) {
				try {
					channel.close();
				} 
				catch (IOException e) {
					if(isVerbose) {
						e.printStackTrace();	
					}
				}
			}
		}
	}

	


	/**
	 * max nb attempts
	 */
	public final static int MAX_NB_ATTEMPTS = 256;
	
	
	
	

	/**
	 * 
	 * @param channel
	 * @param buffer
	 * @throws IOException
	 */
	public static void readBuffer(FileChannel channel, ByteBuffer buffer) throws IOException {
		
		// attempts count
		int c = 0;

		// must at least flush half of the buffer
		while(buffer.hasRemaining() && c < MAX_NB_ATTEMPTS) {
			channel.read(buffer);
			c++;
		}

		if(c == MAX_NB_ATTEMPTS) {
			throw new IOException("Nb of retrieve attempts exceed limit");
		}
	}
	

	
	
	/**
	 * 
	 * @param channel
	 * @param buffer
	 * @throws IOException
	 */
	public static void writeBuffer(FileChannel channel, ByteBuffer buffer) throws IOException {
		
		int iAttempt = 0;

		// must at least flush half of the buffer
		while(buffer.hasRemaining() && iAttempt < MAX_NB_ATTEMPTS) {
			channel.write(buffer);
			iAttempt++;
		}

		// nRetries
		 if(iAttempt == MAX_NB_ATTEMPTS) {
			 throw new IOException("Max write attempts exceeded");
		 }
	}


}
