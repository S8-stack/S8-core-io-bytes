package com.s8.io.bytes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * <code>ByteBuffer</code>-based <code>ByteInflow</code>
 * @author pc
 *
 */
public class BufferByteInflow implements ByteInflow {

	private ByteBuffer buffer;

	public BufferByteInflow(ByteBuffer buffer) {
		super();
		this.buffer = buffer;
	}

	@Override
	public byte getByte() {
		return buffer.get();
	}


	@Override
	public boolean isMatching(byte[] sequence) throws IOException {
		int length = sequence.length;
		if(buffer.remaining()>=length) {
			byte[] bytes = new byte[length];
			buffer.get(bytes);
			for(int i=0; i<length; i++) {
				if(bytes[i]!=sequence[i]) {
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}


	@Override
	public int getUInt8() throws IOException {
		return buffer.get() & 0xff;
	}

	@Override
	public boolean[] getFlags8() throws IOException {
		boolean[] flags = new boolean[8];
		byte b = buffer.get();
		flags[0] = (b & 0x80) == 0x80;
		flags[1] = (b & 0x40) == 0x40;
		flags[2] = (b & 0x20) == 0x20;
		flags[3] = (b & 0x10) == 0x10;
		flags[4] = (b & 0x08) == 0x08;
		flags[5] = (b & 0x04) == 0x04;
		flags[6] = (b & 0x02) == 0x02;
		flags[7] = (b & 0x01) == 0x01;
		return flags;
	}


	@Override
	public byte[] getByteArray(int length) {
		byte[] bytes = new byte[length];
		buffer.get(bytes);
		return bytes;
	}



	@Override
	public int getUInt16() throws IOException {
		byte b0 = buffer.get();
		byte b1 = buffer.get();
		return ((b0 & 0xff) << 8 ) | (b1 & 0xff);
	}


	@Override
	public short getInt16() {
		return buffer.getShort();
	}



	@Override
	public int getUInt31() throws IOException {
		byte[] bytes = getByteArray(4);
		return (int) (
				(bytes[0] & 0x7f) << 24 | 
				(bytes[1] & 0xff) << 16 | 
				(bytes[2] & 0xff) << 8 | 
				(bytes[3] & 0xff));
	}

	@Override
	public int getInt32() {
		return buffer.getInt();
	}


	@Override
	public int[] getInt32Array() throws IOException {
		// retrieve length
		int length = getUInt32();

		int[] array = new int[length];
		for(int i=0; i<length; i++) {
			array[i] = buffer.getInt();
		}
		return array;
	}

	@Override
	public int getUInt32() throws IOException {
		byte[] bytes = getByteArray(4);
		return (int) (
				(bytes[0] & 0x7f) << 24 | 
				(bytes[1] & 0xff) << 16 | 
				(bytes[2] & 0xff) << 8 | 
				(bytes[3] & 0xff));
	}


	@Override
	public long getInt64() {
		return buffer.getLong();
	}


	@Override
	public long[] getInt64Array() throws IOException {
		// retrieve length
		int length = getUInt32();

		long[] array = new long[length];
		for(int i=0; i<length; i++) {
			array[i] = buffer.getLong();
		}
		return array;
	}


	@Override
	public int getUInt() {
		byte b = buffer.get(); // first byte
		if((b & 0x80) == 0x80) {
			int value = b & 0x7f;
			b = buffer.get(); // second byte
			if((b & 0x80) == 0x80) {
				value = (value << 7) | (b & 0x7f);
				b = buffer.get(); // third byte
				if((b & 0x80) == 0x80) {
					value = (value << 7) | (b & 0x7f);
					b = buffer.get(); // fourth byte
					if((b & 0x80) == 0x80) {
						value = (value << 7) | (b & 0x7f);
						b = buffer.get(); // fifth byte (final one)
						return (value << 7) | (b & 0x7f);
					}
					else { // fourth byte is matching 0x7f mask
						return (value << 7) | b;
					}
				}
				else { // third byte is matching 0x7f mask
					return (value << 7) | b;
				}
			}
			else { // second byte is matching 0x7f mask
				return (value << 7) | b;
			}
		}
		else { // first byte is matching 0x7f mask
			return b;
		}
	}


	@Override
	public float getFloat32() {
		return buffer.getFloat();
	}


	@Override
	public float[] getFloat32Array() throws IOException {
		int length = getUInt32();
		float[] array = new float[length];
		for(int i=0; i<length; i++) {
			array[i] = buffer.getFloat();
		}
		return array;
	}


	@Override
	public double getFloat64() {
		return buffer.getDouble();
	}


	@Override
	public double[] getFloat64Array() throws IOException {
		int length = getUInt32();
		double[] array = new double[length];
		for(int i=0; i<length; i++) {
			array[i] = buffer.getDouble();
		}
		return array;
	}


	/**
	 * Max <code>String</code> length is 65536
	 * @return String
	 * @throws IOException 
	 */
	@Override
	public String getString() throws IOException {

		// read unsigned int
		int bytecount = getUInt();

		// retrieve all bytes
		byte[] bytes = getByteArray(bytecount);
		return new String(bytes, StandardCharsets.UTF_8);
	}

}
