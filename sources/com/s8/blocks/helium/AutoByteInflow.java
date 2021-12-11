package com.s8.blocks.helium;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import com.s8.alpha.utilities.bytes.ByteInflow;

/**
 * 
 * @author pierreconvert
 *
 */
public abstract class AutoByteInflow implements ByteInflow {


	protected ByteBuffer buffer;

	/**
	 * 
	 * @param buffer
	 */
	public AutoByteInflow() {
		super();
	}


	public abstract void pull();

	public abstract void prepare(int bytecount) throws IOException;


	@Override
	public boolean isMatching(byte[] sequence) throws IOException {
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
	public byte getByte() throws IOException {
		prepare(1);
		return buffer.get();
	}


	@Override
	public int getUInt8() throws IOException {
		prepare(1);
		return buffer.get() & 0xff;
	}

	@Override
	public boolean[] getFlags8() throws IOException {
		prepare(1);
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



	@Override
	public int getUInt16() throws IOException {
		prepare(2);
		byte b0 = buffer.get();
		byte b1 = buffer.get();
		return ((b0 & 0xff) << 8 ) | (b1 & 0xff);
	}


	@Override
	public short getInt16() throws IOException {
		prepare(2);
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
	public int getInt32() throws IOException {
		prepare(4);
		return buffer.getInt();
	}


	@Override
	public int[] getInt32Array() throws IOException {
		// retrieve length
		prepare(4);
		int length = getUInt32();

		prepare(4*length);
		int[] array = new int[length];
		for(int i=0; i<length; i++) {
			array[i] = buffer.getInt();
		}
		return array;
	}

	@Override
	public int getUInt32() throws IOException {
		prepare(4);
		byte[] bytes = getByteArray(4);
		return (int) (
				(bytes[0] & 0x7f) << 24 | 
				(bytes[1] & 0xff) << 16 | 
				(bytes[2] & 0xff) << 8 | 
				(bytes[3] & 0xff));
	}


	@Override
	public long getInt64() throws IOException {
		prepare(8);
		return buffer.getLong();
	}


	@Override
	public long[] getInt64Array() throws IOException {
		prepare(4);
		// retrieve length
		int length = getUInt32();

		prepare(8*length);
		long[] array = new long[length];
		for(int i=0; i<length; i++) {
			array[i] = buffer.getLong();
		}
		return array;
	}


	@Override
	public int getUInt() throws IOException {
		prepare(1);
		byte b = buffer.get(); // first byte

		if((b & 0x80) == 0x80) {
			int value = b & 0x7f;

			prepare(1);
			b = buffer.get(); // second byte
			if((b & 0x80) == 0x80) {
				value = (value << 7) | (b & 0x7f);

				prepare(1);
				b = buffer.get(); // third byte

				if((b & 0x80) == 0x80) {
					value = (value << 7) | (b & 0x7f);

					prepare(1);
					b = buffer.get(); // fourth byte

					if((b & 0x80) == 0x80) {
						value = (value << 7) | (b & 0x7f);

						prepare(1);
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
	public long getL8UInt() throws IOException {
		prepare(1);
		byte b = buffer.get(); // first byte
		int length = (b & 0xff);
		if(length == 1) {
			byte[] bytes = getByteArray(1);
			return (long) (bytes[0] & 0xff);
		}
		else if(length == 2){
			byte[] bytes = getByteArray(2);
			return (long) (
					(bytes[2] & 0xff) << 8 | 
					(bytes[3] & 0xff));
		}
		else if(length == 3){
			byte[] bytes = getByteArray(3);
			return (long) (
					(bytes[1] & 0xff) << 16 | 
					(bytes[2] & 0xff) << 8 | 
					(bytes[3] & 0xff));
		}
		else if(length == 4){
			byte[] bytes = getByteArray(4);
			return (long) (
					(bytes[0] & 0xff) << 24 | 
					(bytes[1] & 0xff) << 16 | 
					(bytes[2] & 0xff) << 8 | 
					(bytes[3] & 0xff));
		}
		else if(length == 5){
			byte[] bytes = getByteArray(5);
			return (long) (
					(bytes[0] & 0xff) << 32 | 
					(bytes[1] & 0xff) << 24 | 
					(bytes[2] & 0xff) << 16 | 
					(bytes[3] & 0xff) << 8 | 
					(bytes[4] & 0xff));
		}
		else if(length == 6){
			byte[] bytes = getByteArray(6);
			return (long) (
					(bytes[0] & 0xff) << 40 | 
					(bytes[1] & 0xff) << 32 | 
					(bytes[2] & 0xff) << 24 | 
					(bytes[3] & 0xff) << 16 | 
					(bytes[4] & 0xff) << 8 | 
					(bytes[5] & 0xff));
		}
		else if(length == 7){
			byte[] bytes = getByteArray(7);
			return (long) (
					(bytes[0] & 0xff) << 48 | 
					(bytes[1] & 0xff) << 40 | 
					(bytes[2] & 0xff) << 32 | 
					(bytes[3] & 0xff) << 24 | 
					(bytes[4] & 0xff) << 16 | 
					(bytes[5] & 0xff) << 8 | 
					(bytes[6] & 0xff));
		}
		else if(length == 8){
			byte[] bytes = getByteArray(8);
			return (long) (
					(bytes[0] & 0xff) << 56 | 
					(bytes[1] & 0xff) << 48 | 
					(bytes[2] & 0xff) << 40 | 
					(bytes[3] & 0xff) << 32 | 
					(bytes[4] & 0xff) << 24 | 
					(bytes[5] & 0xff) << 16 | 
					(bytes[6] & 0xff) << 8 | 
					(bytes[7] & 0xff));
		}
		else {
			throw new IOException("Illegal L8UInt format: length"+length);
		}
		
	}


	@Override
	public float getFloat32() throws IOException {
		prepare(4);
		return buffer.getFloat();
	}


	@Override
	public float[] getFloat32Array() throws IOException {
		prepare(4);
		int length = getUInt32();

		prepare(4*length);
		float[] array = new float[length];
		for(int i=0; i<length; i++) {
			array[i] = buffer.getFloat();
		}
		return array;
	}


	@Override
	public double getFloat64() throws IOException {
		prepare(8);
		return buffer.getDouble();
	}


	@Override
	public double[] getFloat64Array() throws IOException {
		prepare(4);
		int length = getUInt32();

		prepare(8*length);
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
	public String getL32StringUTF8() throws IOException {

		// read unsigned int
		int bytecount = getUInt32();

		// retrieve all bytes
		byte[] bytes = getByteArray(bytecount);
		return new String(bytes, StandardCharsets.UTF_8);
	}


	@Override
	public String getL8StringASCII() throws IOException {
		// read unsigned int
		int length = getUInt8();

		// retrieve all bytes
		byte[] bytes = getByteArray(length);
		return new String(bytes, StandardCharsets.US_ASCII);
	}


}
