package com.s8.io.bytes;

/**
 * Memory footprint byte count proxy.
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class ByteCount {
	
	


	public long value;
	
	public void add(long nBytes) {
		value+=nBytes;
	}
}
