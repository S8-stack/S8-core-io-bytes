package com.s8.io.bytes.base64;

import java.nio.charset.StandardCharsets;

public class Base64IdGenerator {



	/**
	 * the enconding characters
	 */
	public final static char[] ENCODING = new char[] {
			'0', '1', '2', '3', '4', '5', '6', '7', 
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 
			'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 
			'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 
			'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
			'U', 'V', 'W', 'X', 'Y', 'Z', '$', '&'
	};


	public final static long 
	POW06 = 64L,
	POW12 = 4096L,
	POW18 = 262144L,
	POW24 = 16777216L,
	POW30 = 1073741824L,
	POW36 = 68719476736L,
	POW42 = 4398046511104L, 
	POW48 = 281474976710656L,
	POW54 = 18014398509481984L ,
	POW60 = 1152921504606846976L;

	
	private int prefixLength;
	
	private byte[] prefixBytes;
	
	private long lastAssignedIndex;

	/**
	 * 
	 * @param prefix
	 * @param nextAssignableIndex
	 */
	public Base64IdGenerator(String prefix, long lastAssignedIndex){
		prefixBytes = prefix.getBytes(StandardCharsets.UTF_8);
		prefixLength = prefixBytes.length;
		this.lastAssignedIndex = lastAssignedIndex;
	}

	
	public long getLastAssignedIndex() {
		return lastAssignedIndex;
	}
	
	

	/**
	 * 
	 * @param value
	 * @return
	 */
	public String generate() {
		
		long index = ++lastAssignedIndex;
		
		int nDigits = 0;
		if(index < POW06) { nDigits = 1; }
		else if(index < POW12) { nDigits = 2; }
		else if(index < POW18) { nDigits = 3; }
		else if(index < POW24) { nDigits = 4; }
		else if(index < POW30) { nDigits = 5; }
		else if(index < POW36) { nDigits = 6; }
		else if(index < POW42) { nDigits = 7; }
		else if(index < POW48) { nDigits = 8; }
		else if(index < POW54) { nDigits = 9; }
		else if(index < POW60) { nDigits = 10; }
		
		
		int n = prefixLength + nDigits;
		
		char[] chars = new char[n];
		
		// copy prefix
		for(int i = 0; i < prefixLength; i++) { 
			chars[i] = (char) prefixBytes[i]; 
		}
		
		// index
		for(int i = 0; i < nDigits; i++) {
			chars[n-1 - i] = ENCODING[(int) (index & 0x3f)];
			index >>= 6;
		}
		return new String(chars);
	}
	
	
}
