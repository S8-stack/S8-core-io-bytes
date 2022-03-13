package com.s8.io.bytes;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class CompilableByteOutflow extends AutoByteOutflow {

	public final static int CAPACITY = 1024;

	private int capacity;

	private List<ByteBuffer> filled;


	public CompilableByteOutflow() {
		super();
		this.capacity = CAPACITY;
		filled = new ArrayList<>();

		// feed
		buffer = ByteBuffer.allocate(capacity);
	}


	public CompilableByteOutflow(int capacity) {
		super();
		this.capacity = capacity;
		filled = new ArrayList<>();
	}


	@Override
	public boolean push() {
		// dump previous buffer
		buffer.flip();
		filled.add(buffer);

		// feed a new one
		buffer = ByteBuffer.allocate(capacity);

		// hard swap, so always succesful in pushing bytes
		return true;
	}


	/**
	 * 
	 * @return
	 */
	public byte[] compile(){

		// dump current buffer
		buffer.flip();
		filled.add(buffer);

		// flatten...
		int size=0;
		for(ByteBuffer buffer : filled){
			size+=buffer.limit();
		}
		byte[] bytes = new byte[size];
		int offset=0, length;
		for(ByteBuffer buffer : filled){
			length = buffer.limit();
			buffer.get(bytes, offset, length);
			offset+=length;
		}
		return bytes;
	}


	@Override
	public void setCapacity(int capacity) {
		// TODO Auto-generated method stub
		
	}
}
