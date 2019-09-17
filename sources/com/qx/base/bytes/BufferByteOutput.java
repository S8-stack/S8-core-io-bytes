package com.qx.base.bytes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class BufferByteOutput implements ByteOutput {

	public final static int CAPACITY = 1024;

	private ByteBuffer buffer;

	public BufferByteOutput(ByteBuffer buffer) {
		super();
		this.buffer = buffer;
	}

	@Override	
	public void putBoolean(boolean b) throws IOException{
		if(b){
			buffer.put((byte) 32);	
		}
		else{
			buffer.put((byte) 33);	
		}
	}

	@Override
	public void putFlags(boolean[] flags) throws IOException {
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
	public void putByte(byte b) throws IOException{
		buffer.put(b);
	}
	
	@Override
	public void putUInt8(int value) throws IOException{
		buffer.put((byte) (value & 0xff));
	}

	@Override
	public void putUInt16(int value){
		buffer.putShort((short) (value & 0xffff));
	}


	@Override
	public void putInt16(short value) throws IOException {
		buffer.putShort(value);
	}


	@Override
	public void putUInt32(int value){
		buffer.putInt((int) (value & 0x7fffffff));
	}


	@Override
	public void putInt32(int value) throws IOException {
		buffer.putInt((int) (value & 0x7fffffff));	
	}


	@Override
	public void putInt64(long value){
		buffer.putLong(value);
	}


	public void putFloat32(double value){
		putFloat32((float) value); 
	}

	@Override
	public void putFloat32(float value){
		buffer.putFloat(value);
	}

	@Override
	public void putFloat64(double value){
		buffer.putDouble(value);
	}


	/**
	 * Default is UTF-8 (most compact solution).
	 * @param value
	 * @throws IOException
	 */
	@Override
	public void putStringUTF8(String value) throws IOException{
		if(value!=null){
			byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

			// we skip the first two bytes, but add to pass our own length
			int length = bytes.length;
			if(length>2147483647){
				throw new IOException("String arg size is exceeding 2^31-1 "
						+ "(length is encoded in 4 bytes).");
			}
			putUInt32(length);
			
			putByteArray(bytes);
		}
		else{ // null
			putUInt32(0); // empty string
		}
	}



	@Override
	public void putByteArray(byte[] bytes) throws IOException {
		buffer.put(bytes);
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
