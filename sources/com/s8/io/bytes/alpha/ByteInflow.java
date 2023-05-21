package com.s8.io.bytes.alpha;

import java.io.IOException;


/**
 * <p>
 * Unified in-flow of bytes with getters for standard primitives.
 * </p>
 * 
 * <p>
 * /!\ All array or String lengths are encoded as UInt32 to reflect their
 * maximum possible length in JAVA.
 * </p>
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public interface ByteInflow {


	public final static int BOOL8_FALSE = 0x37;
	
	public final static int BOOL8_TRUE = 0x53;

	
	/**
	 * 
	 * @return
	 */
	public long getCount();



	/**
	 * @return next byte
	 * @throws IOException 
	 */
	public byte getByte() throws IOException;


	/**
	 * @return next byte
	 * @throws IOException 
	 */
	public default boolean getBool8() throws IOException {
		switch(getUInt8()) {
		case BOOL8_FALSE : return false;
		case BOOL8_TRUE : return true;
		default : throw new IOException("No matching code for boolean value");
		}
	}


	/**
	 * length passed as argument
	 * 
	 * @param n next bytes, where n is the length of the <code>bytes</code> array
	 * @throws IOException 
	 */
	public byte[] getByteArray(int length) throws IOException;



	/**
	 * Tells if the next bytes in the ByteInput are matching the sequence passed as
	 * argument. Note that this method will return an exception if there is not enough
	 * bytes to make the comparison.
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	public boolean matches(byte[] sequence) throws IOException;


	/**
	 * boolean
	 * @return
	 * @throws IOException
	 */
	boolean[] getFlags8() throws IOException;
	

	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public long getUInt7x() throws IOException;




	/**
	 * @return the next Unsigned Integer 8 bits (1 byte)
	 * @throws IOException
	 */
	public int getUInt8() throws IOException;


	/**
	 * @return the next Unsigned Integer 16 bits (2 bytes)
	 * @throws IOException
	 */
	public int getUInt16() throws IOException;


	/**
	 * @return the next Unsigned Integer 16 bits (2 bytes)
	 * @throws IOException
	 */
	public int getUInt24() throws IOException;


	/**
	 * @return the next Unsigned Integer 31 bits (4 bytes, first bit ignored)
	 * @throws IOException
	 */
	public int getUInt31() throws IOException;


	/**
	 * Curretly doing the same as <code>getUInt31</code>
	 * @return
	 * @throws IOException
	 */
	public int getUInt32() throws IOException;



	/**
	 * @return the next Unsigned Integer 16 bits (2 bytes)
	 * @throws IOException
	 */
	public long getUInt40() throws IOException;



	/**
	 * @return the next Unsigned Integer 16 bits (2 bytes)
	 * @throws IOException
	 */
	public long getUInt48() throws IOException;



	/**
	 * @return the next Unsigned Integer 16 bits (2 bytes)
	 * @throws IOException
	 */
	public long getUInt56() throws IOException;


	/**
	 * @return the next Unsigned Integer 16 bits (2 bytes)
	 * @throws IOException
	 */
	public long getUInt64() throws IOException;



	/**
	 * 
	 * @return
	 */
	public byte getInt8() throws IOException;


	/**
	 * @return the next Signed Integer 16 bits (2 bytes)
	 * @throws IOException
	 */
	public short getInt16() throws IOException;

	/**
	 * @return the next Signed Integer 32 bits (4 bytes)
	 * @throws IOException
	 */
	public int getInt32() throws IOException;
	
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public default int[] getInt32Array() throws IOException {
		int n = getInt32();
		if(n >= 0) {
			int[] values = new int[n];
			for(int i = 0; i < n; i++) { values[i] = getInt32(); }
			return values;
		}
		else { return null; } // null
	}


	/**
	 * @return the next Signed Integer 64 bits (8 bytes)
	 * @throws IOException
	 */
	public long getInt64() throws IOException;


	/**
	 * @return the next Float 32 bits (4 bytes)
	 * @throws IOException
	 */
	public float getFloat32() throws IOException;


	/**
	 * @return the next Float 64 bits (8 bytes)
	 * @throws IOException
	 */
	public double getFloat64() throws IOException;
	

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public default double[] getFloat64Array() throws IOException {
		int n = getInt32();
		if(n >= 0) {
			double[] values = new double[n];
			for(int i = 0; i < n; i++) { values[i] = getFloat64(); }
			return values;
		}
		else { return null; } // null
	}



	/**
	 * ASCII char only String, whose length is encoded on 8bits (max length is 256).
	 * @return
	 * @throws IOException
	 */
	public String getStringUTF8() throws IOException;
	
	

	/**
	 * 
	 * @param outflow
	 */
	public void startRecording(ByteOutflow outflow);


	/**
	 * @throws IOException 
	 * 
	 */
	public void stopRecording() throws IOException;


}