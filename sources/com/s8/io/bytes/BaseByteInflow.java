package com.s8.io.bytes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import com.s8.api.bytes.ByteInflow;



/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class BaseByteInflow implements ByteInflow {

	
	
	protected ByteBuffer buffer;
	
	

	protected abstract void allocate(int bytecount) throws IOException;
	
	
	
	@Override
	public byte getByte() throws IOException {
		allocate(1);
		return buffer.get();
	}


	@Override
	public boolean[] getFlags8() throws IOException {
		allocate(1);
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
	public long getUInt7x() throws IOException {

		/*
		 * 0x40: 01000000
		 * 0x80: 10000000
		 * 0xc0: 11000000
		 */
		
		// pull first byte
		int b = getUInt8();
		
		if((b & 0x40L) == 0x40L) {
			return  -(b & 0x3fL); // no shift
		}
		else {
			// compute value
			
			long value = b & 0x3fL; // no shift
			int shift = 6;
			
			while((b & 0x80L) == 0x80L) {
				
				// pull next byte
				b = getUInt8();
				
				// recompute value
				
				value = ((b & 0x7fL) << shift) | value;
				shift += 7;
			}
			return value;
		}
	}
	
	

	@Override
	public int getUInt8() throws IOException {
		allocate(1);
		return ((int) buffer.get()) & 0xff;
	}


	@Override
	public int getUInt16() throws IOException {
		allocate(2);
		byte b0 = buffer.get();
		byte b1 = buffer.get();
		return ((b0 & 0xff) << 8 ) | (b1 & 0xff);
	}



	

	@Override
	public int getUInt24() throws IOException {
		// byte array is automatically handling the buffer feeding
		byte[] bytes = getByteArray(3); 
		return (bytes[1] & 0xff) << 16 | 
				(bytes[2] & 0xff) << 8 | 
				(bytes[3] & 0xff);
	}
	

	@Override
	public int getUInt31() throws IOException {
		// byte array is automatically handling the buffer feeding
		byte[] bytes = getByteArray(4);
		return (int) (
				(bytes[0] & 0x7f) << 24 | 
				(bytes[1] & 0xff) << 16 | 
				(bytes[2] & 0xff) << 8 | 
				(bytes[3] & 0xff));
	}

	
	@Override
	public int getUInt32() throws IOException {
		// byte array is automatically handling the buffer feeding
		byte[] bytes = getByteArray(4);
		return (
				(bytes[0] & 0xff) << 24 | 
				(bytes[1] & 0xff) << 16 | 
				(bytes[2] & 0xff) << 8 | 
				(bytes[3] & 0xff));
	}
	


	@Override
	public long getUInt40() throws IOException {
		// byte array is automatically handling the buffer feeding
		byte[] bytes = getByteArray(5);
		return (bytes[0] & 0xffL) << 32 | 
				(bytes[1] & 0xffL) << 24 | 
				(bytes[2] & 0xffL) << 16 | 
				(bytes[3] & 0xffL) << 8 | 
				(bytes[4] & 0xffL);
	}


	@Override
	public long getUInt48() throws IOException {
		// byte array is automatically handling the buffer feeding
		byte[] bytes = getByteArray(6);
		return (bytes[0] & 0xffL) << 40 | 
				(bytes[1] & 0xffL) << 32 | 
				(bytes[2] & 0xffL) << 24 | 
				(bytes[3] & 0xffL) << 16 | 
				(bytes[4] & 0xffL) << 8 | 
				(bytes[5] & 0xffL);
	}


	@Override
	public long getUInt56() throws IOException {
		// byte array is automatically handling the buffer feeding
		byte[] bytes = getByteArray(7);
		return (bytes[0] & 0xffL) << 48 | 
				(bytes[1] & 0xffL) << 40 | 
				(bytes[2] & 0xffL) << 32 | 
				(bytes[3] & 0xffL) << 24 | 
				(bytes[4] & 0xffL) << 16 | 
				(bytes[5] & 0xffL) << 8 | 
				(bytes[6] & 0xffL);
	}


	@Override
	public long getUInt64() throws IOException {
		// byte array is automatically handling the buffer feeding
		byte[] bytes = getByteArray(8);
		return (bytes[0] & 0x7fL) << 56 | 
				(bytes[1] & 0xffL) << 48 | 
				(bytes[2] & 0xffL) << 40 | 
				(bytes[3] & 0xffL) << 32 | 
				(bytes[4] & 0xffL) << 24 | 
				(bytes[5] & 0xffL) << 16 | 
				(bytes[6] & 0xffL) << 8 | 
				(bytes[7] & 0xffL);
	}
	
	

	@Override
	public byte getInt8() throws IOException {
		allocate(1);
		/*
		 * In Java, byte is an 8-bit signed (positive and negative) data type, values from -128 (-2^7) to 127 (2^7-1)
		 */
		return buffer.get();
	}
	
	@Override
	public short getInt16() throws IOException {
		allocate(2);
		return buffer.getShort();
	}


	@Override
	public int getInt32() throws IOException {
		allocate(4);
		return buffer.getInt();
	}
	

	@Override
	public long getInt64() throws IOException {
		allocate(8);
		return buffer.getLong();
	}
	
	


	@Override
	public float getFloat32() throws IOException {
		allocate(4);
		return buffer.getFloat();
	}
	

	@Override
	public double getFloat64() throws IOException {
		allocate(8);
		return buffer.getDouble();
	}
	
	/**
	 * Max <code>String</code> length is 65536
	 * @return String
	 * @throws IOException 
	 */
	@Override
	public String getStringUTF8() throws IOException {

		// read unsigned int
		int bytecount = (int) getUInt7x();

		// retrieve all bytes
		if(bytecount >= 0) {
			byte[] bytes = getByteArray(bytecount);
			return new String(bytes, StandardCharsets.UTF_8);	
		}
		else {
			return null;
		}
	}
	

	

}
