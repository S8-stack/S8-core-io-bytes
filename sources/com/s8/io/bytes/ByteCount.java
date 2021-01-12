package com.s8.io.bytes;

/**
 * Memory footprint byte count proxy.
 * 
 * @author pc
 *
 */
public class ByteCount {
	
	/**
	 * <p> Proxy of object allocation in RAM: object. Memory footprint+ </p>
	 * <p>
	 * From:
	 * https://stackoverflow.com/questions/258120/what-is-the-memory-consumption-of-an-object-in-java:
	 * </p>
	 * <i> In a modern 64-bit JDK, an object has a 12-byte header, padded to a
	 * multiple of 8 bytes, so the minimum object size is 16 bytes. For 32-bit JVMs,
	 * the overhead is 8 bytes, padded to a multiple of 4 bytes. (From Dmitry
	 * Spikhalskiy's answer, Jayen's answer, and JavaWorld.) </i>
	 * <p>
	 */
	public final static long OBJECT_ALLOCATION = 32;
	
	
	/**
	 * 
	 */
	public final static long OBJECT_REFERENCE = 32;
	
	/**
	 * 
	 */
	public final static long MAP_ENTRY = 20;


	public long value;
	
	public void add(long nBytes) {
		value+=nBytes;
	}
}
