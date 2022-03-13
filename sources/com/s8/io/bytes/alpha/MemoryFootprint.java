package com.s8.io.bytes.alpha;



/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class MemoryFootprint {
	
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
	public final static long OBJECT_ALLOCATION_BYTECOUNT = 32;
	
	public final static long OBJECT_REFERENCE_BYTECOUNT = 8;
	
	
	
	/**
	 * 
	 */
	public final static long ENTRY_BYTECOUNT = 20;

	private long nInstances;
	
	private long nReferences;
	
	private long nEntries;
	
	private long nBytesStored;
	
	
	public void reportInstance() {
		nInstances++;
	}
	
	public void reportInstances(int n) {
		nInstances+=n;
	}
	
	public void reportReference() {
		nReferences++;
	}
	
	public void reportReferences(int n) {
		nReferences+=n;
	}
	
	public void reportEntry() {
		nEntries++;
	}
	
	
	public void reportBytes(long payload) {
		nBytesStored+=payload;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public long getBytecount() {
		return nInstances * OBJECT_ALLOCATION_BYTECOUNT 
				+ nReferences * OBJECT_REFERENCE_BYTECOUNT
				+ nEntries * ENTRY_BYTECOUNT 
				+ nBytesStored;
	}
	
}
