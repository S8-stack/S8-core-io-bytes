package com.qx.reactive;

import java.nio.ByteBuffer;

/**
 * Accumulate bytes by auto-increasing underlying byte[] array length
 * 
 * @author pc
 *
 */
public class OutputByteArray {

	private byte[] bytes;

	private int index;

	private int length;

	public OutputByteArray() {
		super();
		initialize(64);
	}

	public OutputByteArray(int length) {
		super();
		initialize(length);
	}
	

	public OutputByteArray(byte[] bytes) {
		super();
		this.bytes = bytes;
		index = 0;
		length = bytes.length;
	}


	private void initialize(int length) {
		this.length = length;
		this.bytes = new byte[length];
		this.index = 0;
	}

	
	/**
	 * write as much bytes as possible in the buffer 
	 * @param buffer
	 */
	public boolean pull(ByteBuffer buffer) {
		int n = Math.min(buffer.remaining(), length-index);
		buffer.put(bytes, index, n);
		index+=n;
		return index==length;
	}
	
}
