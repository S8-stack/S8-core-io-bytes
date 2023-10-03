package com.s8.io.bytes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import com.s8.api.bytes.ByteOutflow;



/**
 * 
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class BaseByteOutflow implements ByteOutflow {


	/**
	 * to be initialized by sub classes
	 */
	protected ByteBuffer buffer;

	/**
	 * 
	 * @param bytecount
	 * @throws IOException 
	 */
	protected abstract void prepare(int bytecount) throws IOException;


	@Override
	public void putByte(byte b) throws IOException {
		prepare(1);
		buffer.put(b);
	}

	@Override
	public void putUInt8(int value) throws IOException {
		prepare(1);
		buffer.put((byte) (value & 0xff));
	}

	@Override
	public void putUInt16(int value) throws IOException{
		prepare(2);
		buffer.put((byte) ((value>>8) & 0xff));
		buffer.put((byte) (value & 0xff));
	}

	@Override
	public void putUInt24(int value) throws IOException{
		prepare(3);
		buffer.put((byte) ((value>>16) & 0xff));
		buffer.put((byte) ((value>>8) & 0xff));
		buffer.put((byte) (value & 0xff));
	}


	@Override
	public void putUInt31(int value) throws IOException {
		prepare(4);
		buffer.putInt(value & 0x7fffffff);
	}

	@Override
	public void putUInt32(long value) throws IOException {
		prepare(4);
		buffer.put((byte) ((value >> 24) & 0xffL));
		buffer.put((byte) ((value >> 16) & 0xffL));
		buffer.put((byte) ((value >> 8) & 0xffL));
		buffer.put((byte) (value & 0xffL));	
	}



	@Override
	public void putUInt40(long value) throws IOException {
		prepare(5);
		buffer.put((byte) ((value >> 32) & 0xffL));
		buffer.put((byte) ((value >> 24) & 0xffL));
		buffer.put((byte) ((value >> 16) & 0xffL));
		buffer.put((byte) ((value >> 8) & 0xffL));
		buffer.put((byte) (value & 0xff));
	}


	@Override
	public void putUInt48(long value) throws IOException {
		prepare(6);
		buffer.put((byte) (0x40 | 0x06));
		buffer.put((byte) ((value >> 40) & 0xffL));
		buffer.put((byte) ((value >> 32) & 0xffL));
		buffer.put((byte) ((value >> 24) & 0xffL));
		buffer.put((byte) ((value >> 16) & 0xffL));
		buffer.put((byte) ((value >> 8) & 0xffL));
		buffer.put((byte) (value & 0xff));
	}


	@Override
	public void putUInt53(long value) throws IOException {
		prepare(5);
		buffer.put((byte) ((value>>48) & 0x1f)); // only 5 last bits
		buffer.put((byte) ((value>>40) & 0xff));
		buffer.put((byte) ((value>>32) & 0xff));
		buffer.put((byte) ((value>>24) & 0xff));
		buffer.put((byte) ((value>>16) & 0xff));
		buffer.put((byte) ((value>>8) & 0xff));
		buffer.put((byte) (value & 0xff));
	}


	@Override
	public void putUInt56(long value) throws IOException {
		prepare(7);
		buffer.put((byte) ((value >> 48) & 0xffL));
		buffer.put((byte) ((value >> 40) & 0xffL));
		buffer.put((byte) ((value >> 32) & 0xffL));
		buffer.put((byte) ((value >> 24) & 0xffL));
		buffer.put((byte) ((value >> 16) & 0xffL));
		buffer.put((byte) ((value >> 8) & 0xffL));
		buffer.put((byte) (value & 0xff));
	}


	@Override
	public void putUInt64(long value) throws IOException {
		prepare(8);
		buffer.put((byte) ((value >> 56) & 0x7fL));
		buffer.put((byte) ((value >> 48) & 0xffL));
		buffer.put((byte) ((value >> 40) & 0xffL));
		buffer.put((byte) ((value >> 32) & 0xffL));
		buffer.put((byte) ((value >> 24) & 0xffL));
		buffer.put((byte) ((value >> 16) & 0xffL));
		buffer.put((byte) ((value >> 8) & 0xffL));
		buffer.put((byte) (value & 0xff));
	}




	@Override
	public void putInt8(byte value) throws IOException {
		prepare(1);
		buffer.put(value);
	}


	@Override
	public void putInt16(short value) throws IOException {
		prepare(2);
		buffer.putShort(value);
	}



	@Override
	public void putInt32(int value) throws IOException {
		prepare(4);
		buffer.putInt(value);	
	}


	@Override
	public void putInt64(long value) throws IOException {
		prepare(8);
		buffer.putLong(value);
	}


	@Override
	public void putUInt7x(long value) throws IOException {


		/*
		 * 0x40: 01000000
		 * 0x80: 10000000
		 * 0xc0: 11000000
		 * 0x3f = 0b111111
		 */

		if(value < 0) {
			value = - value; // take the opposite (so -1 is not 2^63+1 as in JAVA encoding)
			if((value & 0x3f) == value) {
				prepare(1);
				buffer.put((byte) ((value & 0x3fL) | 0x40));
			}
			else {
				throw new IOException("This code is not defined");
			}
		}
		else {
			int b = (int) (value & 0x3fL);
			value = (value >> 6);

			while(value != 0x00L) {

				// push previous byte
				prepare(1);
				buffer.put((byte) (b | 0x80));

				// compute next byte
				b = (int) (value & 0x7f);
				value = (value >> 7);
			}	
			// push final byte
			prepare(1);
			buffer.put((byte) b);
		}
	}


	public void putFloat32(double value) throws IOException {
		putFloat32((float) value); 
	}

	@Override
	public void putFloat32(float value) throws IOException {
		prepare(4);
		buffer.putFloat(value);
	}

	@Override
	public void putFloat64(double value) throws IOException {
		prepare(8);
		buffer.putDouble(value);
	}


	/**
	 * Default is UTF-8 (most compact solution).
	 * @param value
	 * @throws IOException
	 */
	@Override
	public void putStringUTF8(String value) throws IOException {
		if(value!=null){
			byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

			// we skip the first two bytes, but add to pass our own length
			int bytecount = bytes.length;
			/*
			if(length>2147483647){
				throw new IOException("String arg size is exceeding 2^31-1 (length is encoded in 4 bytes).");
			}
			 */
			putUInt7x(bytecount);

			putByteArray(bytes);
		}
		else{ // null
			putUInt7x(-1); // empty string
		}
	}



}
