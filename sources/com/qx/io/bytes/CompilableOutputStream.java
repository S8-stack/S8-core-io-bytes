package com.qx.io.bytes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CompilableOutputStream implements ByteOutflow {

	public final static int CAPACITY = 1024;

	private ByteBuffer buffer;

	private List<ByteBuffer> filled;

	private int capacity;


	public CompilableOutputStream() {
		super();
		capacity = CAPACITY;
		filled = new ArrayList<>();
		feed();
	}

	public CompilableOutputStream(int capacity) {
		super();
		this.capacity = capacity;
		filled = new ArrayList<>();
		feed();
	}


	public byte[] compile(){
		
		// close
		if(buffer!=null){
			buffer.flip();
			filled.add(buffer);
		}
		
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


	private void feed(){
		if(buffer!=null){
			buffer.flip();
			filled.add(buffer);
		}
		buffer = ByteBuffer.wrap(new byte[capacity]);
	}
	
	
	private void allocate(int size){
		if(buffer.remaining()<size){
			feed();
		}
	}


	/*
	public void sendBytes(byte[] bytes) throws IOException{
		allocate(bytes.length);

	}
	 */

	public void sendBoolean(boolean b) throws IOException{
		allocate(1);
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
	public void putUInt8(int value) throws IOException{
		allocate(1);
		buffer.put((byte) (value & 0xff));
	}

	@Override
	public void putUInt16(int value){
		allocate(2);
		buffer.putShort((short) (value & 0xffff));
	}


	@Override
	public void putInt16(short value) throws IOException {
		allocate(2);
		buffer.putShort(value);
	}


	@Override
	public void putUInt32(int value){
		allocate(4);
		buffer.putInt((int) (value & 0x7fffffff));
	}


	@Override
	public void putInt32(int value) throws IOException {
		allocate(4);
		buffer.putInt((int) (value & 0x7fffffff));	
	}


	@Override
	public void putInt64(long value){
		allocate(8);
		buffer.putLong(value);
	}


	public void putFloat32(double value){
		putFloat32((float) value); 
	}

	@Override
	public void putFloat32(float value){
		allocate(4);
		buffer.putFloat(value);
	}

	@Override
	public void putFloat64(double value){
		allocate(8);
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
			byte[] bytes = value.getBytes("UTF-8");

			// we skip the first two bytes, but add to pass our own length
			int length = bytes.length;
			if(length>2147483647){
				throw new IOException("String arg size is exceeding 2^31-1 (length is encoded in 4 bytes).");
			}
			putUInt32(length);

			putByteArray(bytes);
		}
		else{ // null
			putUInt16(0); // empty string
		}
	}



	@Override
	public void putByteArray(byte[] bytes) throws IOException {
		// /!\ No block allocation
		int offset = 0, length = bytes.length, space;
		while(length>0) {
			space = buffer.remaining();

			// not enough space
			if(space<length) {
				buffer.put(bytes, offset, space);
				length-=space;
				offset+=space;
				feed();
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
