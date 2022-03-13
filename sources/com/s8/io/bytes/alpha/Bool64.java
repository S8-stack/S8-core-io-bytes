package com.s8.io.bytes.alpha;


/**
 * Copyright © 2016 Riptyde4
 * 
 * @author pierreconvert
 *
 */
public class Bool64 {

	
	/**
	 * Available event channels
	 */
	public final static long 
	
	BIT00 = 0x1L, 
	BIT01 = 0x2L, 
	BIT02 = 0x4L, 
	BIT03 = 0x8L,
	
	BIT04 = 0x10L, 
	BIT05 = 0x20L, 
	BIT06 = 0x40L, 
	BIT07 = 0x80L, 

	BIT08 = 0x100L, 
	BIT09 = 0x200L, 
	BIT10 = 0x400L, 
	BIT11 = 0x800L,

	BIT12 = 0x1000L, 
	BIT13 = 0x2000L, 
	BIT14 = 0x4000L, 
	BIT15 = 0x8000L,

	BIT16 = 0x10000L, 
	BIT17 = 0x20000L, 
	BIT18 = 0x40000L, 
	BIT19 = 0x80000L,

	BIT20 = 0x100000L, 
	BIT21 = 0x200000L, 
	BIT22 = 0x400000L, 
	BIT23 = 0x800000L,

	BIT24 = 0x1000000L, 
	BIT25 = 0x2000000L, 
	BIT26 = 0x4000000L, 
	BIT27 = 0x8000000L,

	BIT28 = 0x10000000L, 
	BIT29 = 0x20000000L, 
	BIT30 = 0x40000000L, 
	BIT31 = 0x80000000L,

	BIT32 = 0x100000000L, 
	BIT33 = 0x200000000L, 
	BIT34 = 0x400000000L, 
	BIT35 = 0x800000000L,

	BIT36 = 0x1000000000L, 
	BIT37 = 0x2000000000L, 
	BIT38 = 0x4000000000L, 
	BIT39 = 0x8000000000L,

	BIT40 = 0x10000000000L, 
	BIT41 = 0x20000000000L, 
	BIT42 = 0x40000000000L, 
	BIT43 = 0x80000000000L,

	BIT44 = 0x100000000000L, 
	BIT45 = 0x200000000000L, 
	BIT46 = 0x400000000000L, 
	BIT47 = 0x800000000000L,

	BIT48 = 0x1000000000000L, 
	BIT49 = 0x2000000000000L, 
	BIT51 = 0x4000000000000L, 
	BIT52 = 0x8000000000000L,

	BIT53 = 0x10000000000000L, 
	BIT54 = 0x20000000000000L, 
	BIT55 = 0x40000000000000L, 
	BIT56 = 0x80000000000000L,

	BIT57 = 0x100000000000000L, 
	BIT58 = 0x200000000000000L, 
	BIT59 = 0x400000000000000L, 
	BIT60 = 0x800000000000000L,

	BIT61 = 0x1000000000000000L, 
	BIT62 = 0x2000000000000000L, 
	BIT63 = 0x4000000000000000L,
	
	ALL_BITS = 0x8fffffffffffffffL;
	
	
	
	private final long value;
	
	public Bool64(long value) {
		this.value = value;
	}
	
	/**
	 * 
	 * @param mask
	 * @param event
	 * @return
	 */
	public boolean has(long mask) {
		return (value & mask) == mask;
	}
	
	
	public static boolean has(long value, long mask) {
		return (value & mask) == mask;
	}
	
	public boolean hasOneOf(long... masks) {
		int n = masks.length;
		long mask;
		for(int i=0; i<n; i++) {
			mask = masks[i];
			if((value & mask) == mask) {
				return true;
			}
		}
		return false;
	}


	/**
	 * 
	 * @param masks
	 * @return
	 */
	public boolean hasAllOf(long... masks) {
		int n = masks.length;
		long mask;
		for(int i=0; i<n; i++) {
			mask = masks[i];
			if((value & mask) != mask){
				return false;
			}
		}
		return true;
	}

}
