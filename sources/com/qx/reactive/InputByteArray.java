package com.qx.reactive;

import java.nio.ByteBuffer;

/**
 * Accumulate bytes by auto-increasing underlying byte[] array length
 * 
 * @author pc
 *
 */
public class InputByteArray {

	private byte[] bytes;

	private int index;

	private int length;

	public InputByteArray() {
		super();
		initialize(64);
	}

	public InputByteArray(int length) {
		super();
		initialize(length);
	}


	private void initialize(int length) {
		this.length = length;
		this.bytes = new byte[length];
		this.index = 0;
	}


	/**
	 * 
	 * @param delta
	 */
	public void extend(int delta) {
		int extendedLength = length+delta;
		byte[] extendedArray = new byte[extendedLength];
		for(int i=0; i<length; i++) {
			extendedArray[i] = bytes[i];
		}
		bytes = extendedArray;
		length = extendedLength;
	}


	public void push(byte b) {
		bytes[index] = b;
		index++;
	}


	/**
	 * 
	 * @param buffer
	 */
	public boolean push(ByteBuffer buffer) {
		int nPushableBytes = Math.min(buffer.remaining(), length-index);
		buffer.get(bytes, index, nPushableBytes);
		return (index+nPushableBytes == length);
	}


	/**
	 * <h2> /!\ No capacity checking before pushing </h2>
	 * Insert the bytes in the builder
	 * @param bytes
	 * @return
	 */
	public void push(byte[] bytes) {
		int nb = bytes.length;
		for(int i=0; i<nb; i++) {
			bytes[index+i] = bytes[i];
		}
		index+=nb;
	}


	public boolean isFilled() {
		return length==index;
	}


	public int getUInt31(int offset) {
		return (int) (((
				bytes[offset+0] & 0x7f) << 24) | 
				bytes[offset+1] << 16 | 
				bytes[offset+2] << 8 | 
				bytes[offset+3]);
	}
	
	
	public int getUInt24(int offset) {
		return (int) (
				bytes[offset+0] << 16 | 
				bytes[offset+1] << 8 | 
				bytes[offset+2]);
	}
	
	
	public short getUInt15(int offset) {
		return (short) (((
				bytes[offset+0] & 0x7f) << 8) | 
				bytes[offset+1]);
	}
	
	public int getUInt8(int offset) {
		return bytes[offset];
	}
	
	public byte getByte(int offset) {
		return bytes[offset];
	}
	
	
	public byte[] getArray() {
		return bytes;
	}
	


}
