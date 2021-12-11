package com.s8.blocks.helium;

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
