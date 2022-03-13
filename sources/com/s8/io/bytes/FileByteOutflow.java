package com.s8.io.bytes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 * 
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class FileByteOutflow extends AutoByteOutflow {

	private FileChannel channel;

	public FileByteOutflow(FileChannel channel, int bufferingSize) {
		super();
		this.channel = channel;
		buffer = ByteBuffer.allocate(bufferingSize);
	}	

	
	@Override
	public boolean push() throws IOException {
		buffer.flip();
		
		int nRetries = 0, nBytes;
		boolean isSuccessful = false;
		// must at least flush half of the buffer
		while(buffer.position()<buffer.limit()/2 && nRetries<8) {
			nBytes = channel.write(buffer);
			if(nBytes>0) {
				isSuccessful = true;
			}
			nRetries++;
		}
		if(nRetries==8) {
			throw new IOException("Failed to flush efficiently the buffer");
		}
		buffer.compact();
		return isSuccessful; 
	}
	
	
	/*
	@Override
	public void putByteArray(byte[] bytes) throws IOException {
		int offset = 0, length=bytes.length;
		boolean isWritten = false;
		while(!isWritten) {
			int nWritables = Math.min(buffer.remaining(), length-offset);
			buffer.put(bytes, offset, nWritables);
			offset+=nWritables;
			if(offset==length) {
				isWritten = true;
			}
			else {
				push();
			}
		}
	}
	 */

	
	/**
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		buffer.flip();
		while(buffer.hasRemaining()) {
			channel.write(buffer);
		}
		channel.close();
	}


	@Override
	public void setCapacity(int capacity) {
		// TODO Auto-generated method stub
		
	}
}
