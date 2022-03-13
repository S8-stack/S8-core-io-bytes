package com.s8.io.bytes;

import java.io.IOException;
import java.nio.ByteBuffer;


/**
 * 
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class BufferByteOutflow extends BaseByteOutflow {



	public BufferByteOutflow(ByteBuffer buffer) {
		super();
		this.buffer = buffer;
	}


	@Override
	public void putFlags8(boolean[] flags) throws IOException {
		byte b = 0;
		for(int i=0; i<7; i++){
			if(flags[i]){
				b = (byte) ((b | 1)<<1);
			}
			else{
				b = (byte) (b<<1);
			}
		}
		if(flags[7]){
			b = (byte) (b | 1);
		}
		buffer.put((byte) b);
	}

	
	@Override
	public void putByteArray(byte[] bytes) throws IOException {
		buffer.put(bytes);
	}

	@Override
	public void putByteArray(byte[] bytes, int offset, int length) throws IOException {
		buffer.put(bytes, offset, length);
	}

	@Override
	protected void prepare(int bytecount) throws IOException {
		// do nothing...
	}


	@Override
	public void setCapacity(int capacity) {
		
		// allocate new buffer
		ByteBuffer copy = ByteBuffer.allocate(capacity);
		
		// flip buffer to read mode
		buffer.flip();
		
		// copy buffer
		copy.put(buffer);
		
		// swap
		buffer = copy;
	}


	
}
