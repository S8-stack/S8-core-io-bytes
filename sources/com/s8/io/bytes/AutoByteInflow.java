package com.s8.io.bytes;

import java.io.IOException;

/**
 * 
 *
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class AutoByteInflow extends BaseByteInflow {



	/**
	 * 
	 * @param buffer
	 */
	public AutoByteInflow() {
		super();
	}


	protected abstract void allocate(int bytecount) throws IOException;


	public abstract void pull() throws IOException;


	@Override
	public boolean matches(byte[] sequence) throws IOException {
		int length = sequence.length;
		int offset = 0, remaining;
		while(length > 0) {
			remaining = buffer.remaining();
			// not enough space
			if(remaining < length) {
				// match what can be matched
				for(int i=0; i<remaining; i++) {
					if(sequence[i+offset] != buffer.get()) {
						return false;
					}
				}
				length-=remaining;
				offset+=remaining;
				pull();
			}
			else { // enough space to match remaining bytes
				for(int i=0; i<length; i++) {
					if(sequence[i+offset] != buffer.get()) {
						return false;
					}
				}
				length = 0;
			}
		}
		return true;
	}


	


	@Override
	public byte[] getByteArray(int length) throws IOException {
		byte[] bytes = new byte[length];
		/*
		prepare(length);
		byte[] bytes = new byte[length];
		buffer.get(bytes);

		 */

		// /!\ No block allocation

		int offset = 0, remaining;
		while(length>0) {
			remaining = buffer.remaining();

			// not enough space
			if(remaining < length) {
				buffer.get(bytes, offset, remaining);
				length-=remaining;
				offset+=remaining;
				pull();
			}
			// enough space to write remaining bytes
			else {
				buffer.get(bytes, offset, length);
				length = 0;
			}
		}
		return bytes;
	}
	
}
