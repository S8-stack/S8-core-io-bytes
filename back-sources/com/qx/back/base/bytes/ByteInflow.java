package com.qx.back.base.bytes;

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
 * @author pc
 *
 */
public interface ByteInflow {


	/**
	 * @return next byte
	 * @throws IOException 
	 */
	public byte getByte() throws IOException;


	/**
	 * length passed as argument
	 * 
	 * @param n next bytes, where n is the length of the <code>bytes</code> array
	 * @throws IOException 
	 */
	public byte[] getByteArray(int length) throws IOException;


	/**
	 * @return the next Unsigned Integer 8 bits (1 byte)
	 * @throws IOException
	 */
	public int getUInt8() throws IOException;


	/**
	 * boolean
	 * @return
	 * @throws IOException
	 */
	boolean[] getFlagsBlock() throws IOException;


	/**
	 * @return the next Unsigned Integer 16 bits (2 bytes)
	 * @throws IOException
	 */
	public int getUInt16() throws IOException;


	/**
	 * @return the next Signed Integer 16 bits (2 bytes)
	 * @throws IOException
	 */
	public short getInt16() throws IOException;


	/**
	 * @return the next Unsigned Integer 31 bits (4 bytes, first bit ignored)
	 * @throws IOException
	 */
	public int getUInt31() throws IOException;


	/**
	 * @return the next Signed Integer 32 bits (4 bytes)
	 * @throws IOException
	 */
	public int getInt32() throws IOException;


	/**
	 * Number of elements is encoded as an UInt16 before starting the first value.
	 * The max number of elements is max(UInt16) = 2^16 = 65536.
	 * 
	 * @return the next Signed Integer 32 bits Array (4 bytes for each value).
	 * @throws IOException
	 */
	public int[] getInt32Array() throws IOException;

	/**
	 * @return the next Signed Integer 64 bits (8 bytes)
	 * @throws IOException
	 */
	public long getInt64() throws IOException;


	/**
	 * Number of elements is encoded as an UInt16 before starting the first value.
	 * The max number of elements is max(UInt16) = 2^16 = 65536.
	 * 
	 * @return the next Signed Integer 64 bits Array (8 bytes for each value).
	 * @throws IOException
	 */
	public long[] getInt64Array() throws IOException;


	/**
	 * @return the next Float 32 bits (4 bytes)
	 * @throws IOException
	 */
	public float getFloat32() throws IOException;


	/**
	 * Number of elements is encoded as an UInt16 before starting the first value.
	 * The max number of elements is max(UInt16) = 2^16 = 65536.
	 * 
	 * @return the next Float 32 bits Array (4 bytes for each value).
	 * @throws IOException
	 */
	public float[] getFloat32Array() throws IOException;


	/**
	 * @return the next Float 64 bits (8 bytes)
	 * @throws IOException
	 */
	public double getFloat64() throws IOException;


	/**
	 * Number of elements is encoded as an UInt16 before starting the first value.
	 * The max number of elements is max(UInt16) = 2^16 = 65536.
	 * 
	 * @return the next Float 64 bits Array (8 bytes for each value).
	 * @throws IOException
	 */
	public double[] getFloat64Array() throws IOException;


	/**
	 * String is encoded in UTF-8. Max length is max(UInt16) = 2^16 = 65536.
	 * 
	 * @return the next String.
	 * @throws IOException
	 */
	public String getString() throws IOException;


}