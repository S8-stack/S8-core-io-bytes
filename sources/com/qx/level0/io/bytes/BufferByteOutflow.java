package com.qx.level0.io.bytes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import com.s8.api.objects.ByteOutflow;

public class BufferByteOutflow implements ByteOutflow {

	public final static int CAPACITY = 1024;

	private ByteBuffer buffer;

	public BufferByteOutflow(ByteBuffer buffer) {
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
	public void putUInt53(long value) throws IOException {
		buffer.put((byte) ((value>>48) & 0x1f)); // only 5 last bits
		buffer.put((byte) ((value>>40) & 0xff));
		buffer.put((byte) ((value>>32) & 0xff));
		buffer.put((byte) ((value>>24) & 0xff));
		buffer.put((byte) ((value>>16) & 0xff));
		buffer.put((byte) ((value>>8) & 0xff));
		buffer.put((byte) (value & 0xff));
	}


	@Override
	public void putUInt(int value) throws IOException {
		if(value<0x80) { // single byte encoding
			buffer.put((byte) (value & 0x7f));
		}
		else if(value<0x4000) { // two bytes encoding
			buffer.put((byte) (((value>>7) & 0x7f) | 0x80));
			buffer.put((byte) (value & 0x7f));
		}
		else if(value<0x200000) { // three bytes encoding
			buffer.put((byte) (((value>>14) & 0x7f) | 0x80));
			buffer.put((byte) (((value>>7) & 0x7f) | 0x80));
			buffer.put((byte) (value & 0x7f));
		}
		else if(value<0x10000000) { // four bytes encoding
			buffer.put((byte) (((value>>21) & 0x7f) | 0x80));
			buffer.put((byte) (((value>>14) & 0x7f) | 0x80));
			buffer.put((byte) (((value>>7) & 0x7f) | 0x80));
			buffer.put((byte) (value & 0x7f));
		}
		else { // five bytes encoding
			buffer.put((byte) (((value>>28) & 0x07) | 0x80));
			buffer.put((byte) (((value>>21) & 0x7f) | 0x80));
			buffer.put((byte) (((value>>14) & 0x7f) | 0x80));
			buffer.put((byte) (((value>>7) & 0x7f) | 0x80));
			buffer.put((byte) (value & 0x7f));
		}
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
	public void putString(String value) throws IOException{
		if(value!=null){
			byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

			// we skip the first two bytes, but add to pass our own length
			int bytecount = bytes.length;
			if(bytecount>2147483647){
				throw new IOException("String arg size is exceeding 2^31-1 "
						+ "(length is encoded in 4 bytes max).");
			}
			putUInt(bytecount);

			putByteArray(bytes);
		}
		else{ // null
			putUInt(0); // empty string
		}
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
