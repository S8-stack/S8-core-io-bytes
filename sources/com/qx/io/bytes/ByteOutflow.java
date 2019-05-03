package com.qx.io.bytes;

import java.io.IOException;

public interface ByteOutflow {

	
	/**
	 * Write bytes array. Auto-feed underlying ByteBuffer as necessary.
	 * @param bytes
	 * @throws IOException
	 */
	public void putByteArray(byte[] bytes) throws IOException;

	
	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * @param flags
	 * @throws IOException
	 */
	public void putFlags(boolean[] flags) throws IOException;

	
	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * @param value
	 * @throws IOException
	 */
	public void putInt16(short value) throws IOException;

	
	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * @param value
	 * @throws IOException
	 */
	public void putInt32(int value) throws IOException;
	
	
	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * @param value
	 * @throws IOException
	 */
	public void putUInt8(int value) throws IOException;
	
	
	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * @param value
	 * @throws IOException
	 */
	public void putUInt16(int value) throws IOException;
	
	
	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * @param array
	 * @throws IOException
	 */
	public void putInt32Array(int[] array) throws IOException;

	
	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * @param value
	 * @throws IOException
	 */
	public void putInt64(long value) throws IOException;

	
	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * @param value
	 * @throws IOException
	 */
	public void putFloat32(float value) throws IOException;

	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * @param value
	 * @throws IOException
	 */
	public void putFloat64(double value) throws IOException;

	
	/**
	 * 
	 * @param array
	 * @throws IOException
	 */
	public void putFloat64Array(double[] array) throws IOException;

	
	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * Max <code>String</code> length is 65536
	 * @return String
	 * @throws IOException 
	 */
	public void putString16(String str) throws IOException;
}
