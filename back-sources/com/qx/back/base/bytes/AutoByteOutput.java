package com.qx.back.base.bytes;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public abstract class AutoByteOutput implements ByteOutput {

	
	protected ByteBuffer buffer;

	public AutoByteOutput() {
		super();
		feed();
	}

	
	
	protected abstract void feed();
	
	/**
	 * dump current buffer
	 */
	protected abstract void dump();
	
	
	private void check(int size){
		if(buffer.remaining()<size){
			swap();
		}
	}
	
	/**
	 * feed a new buffer and dump this one
	 */
	private void swap(){
		dump();
		feed();
	}


	public void sendBoolean(boolean b) {
		check(1);
		if(b){
			buffer.put((byte) 32);	
		}
		else{
			buffer.put((byte) 33);	
		}
	}

	@Override
	public void putFlags(boolean[] flags) {
		check(1);
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
	public void putByte(byte b) throws IOException {
		check(1);
		buffer.put(b);
	}

	@Override
	public void putUInt8(int value) {
		check(1);
		buffer.put((byte) (value & 0xff));
	}

	@Override
	public void putUInt16(int value){
		check(2);
		buffer.putShort((short) (value & 0xffff));
	}


	@Override
	public void putInt16(short value) {
		check(2);
		buffer.putShort(value);
	}


	@Override
	public void putUInt32(int value){
		check(4);
		buffer.putInt((int) (value & 0x7fffffff));
	}


	@Override
	public void putInt32(int value) {
		check(4);
		buffer.putInt((int) (value & 0x7fffffff));	
	}


	@Override
	public void putInt64(long value) {
		check(8);
		buffer.putLong(value);
	}


	public void putFloat32(double value) {
		putFloat32((float) value); 
	}

	@Override
	public void putFloat32(float value) {
		check(4);
		buffer.putFloat(value);
	}

	@Override
	public void putFloat64(double value) {
		check(8);
		buffer.putDouble(value);
	}


	/**
	 * Default is UTF-8 (most compact solution).
	 * @param value
	 * @throws IOException
	 */
	@Override
	public void putStringUTF8(String value) {
		if(value!=null){
			byte[] bytes = null;
			try {
				bytes = value.getBytes("UTF-8");
			} 
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				bytes = value.getBytes();
			}

			// we skip the first two bytes, but add to pass our own length
			int length = bytes.length;
			/*
			if(length>2147483647){
				throw new IOException("String arg size is exceeding 2^31-1 (length is encoded in 4 bytes).");
			}
			*/
			putUInt32(length);

			putByteArray(bytes);
		}
		else{ // null
			putUInt32(0); // empty string
		}
	}



	@Override
	public void putByteArray(byte[] bytes) {
		// /!\ No block allocation
		int offset = 0, length = bytes.length, space;
		while(length>0) {
			space = buffer.remaining();

			// not enough space
			if(space<length) {
				buffer.put(bytes, offset, space);
				length-=space;
				offset+=space;
				swap();
			}
			// enough space to write remaining bytes
			else {
				buffer.put(bytes, offset, length);
				length=0;
			}
		}
	}


	@Override
	public void putUInt32Array(int[] array) throws IOException {
		// write byte
		int length = array.length;
		putUInt32(length);

		for(int i=0; i<length; i++) {
			putUInt32(array[i]);
		}
	}


	@Override
	public void putInt32Array(int[] array) throws IOException {
		// write byte
		int length = array.length;
		putUInt32(length);

		for(int i=0; i<length; i++) {
			putInt32(array[i]);
		}
	}


	@Override
	public void putInt64Array(long[] array) throws IOException {
		// write byte
		int length = array.length;
		putUInt32(length);

		for(int i=0; i<length; i++) {
			putInt64(array[i]);
		}
	}



	@Override
	public void putFloat32Array(float[] array) throws IOException {
		// write byte
		int length = array.length;
		putUInt32(length);

		for(int i=0; i<length; i++) {
			putFloat32(array[i]);
		}
	}


	@Override
	public void putFloat64Array(double[] array) throws IOException {
		// write byte
		int length = array.length;
		putUInt32(length);

		for(int i=0; i<length; i++) {
			putFloat64(array[i]);
		}
	}
}
