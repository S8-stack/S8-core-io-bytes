package com.s8.io.bytes.api;

/**
 * Memory footprint byte count proxy.
 * 
 * @author pc
 *
 */
public class ByteCount {
	
	


	public long value;
	
	public void add(long nBytes) {
		value+=nBytes;
	}
}
