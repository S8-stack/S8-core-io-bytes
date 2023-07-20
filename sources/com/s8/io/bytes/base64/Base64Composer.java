package com.s8.io.bytes.base64;

import java.nio.charset.StandardCharsets;

public class Base64Composer {



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
	

	public Base64Composer(String prefix){
		prefixBytes = prefix.getBytes(StandardCharsets.UTF_8);
		prefixLength = prefixBytes.length;
	}


	/**
	 * 
	 * @param value
	 * @return
	 */
	public String generate(long value) {
		
		int nDigits = 0;
		if(value < POW06) { nDigits = 1; }
		else if(value < POW12) { nDigits = 2; }
		else if(value < POW18) { nDigits = 3; }
		else if(value < POW24) { nDigits = 4; }
		else if(value < POW30) { nDigits = 5; }
		else if(value < POW36) { nDigits = 6; }
		else if(value < POW42) { nDigits = 7; }
		else if(value < POW48) { nDigits = 8; }
		else if(value < POW54) { nDigits = 9; }
		else if(value < POW60) { nDigits = 10; }
		
		
		int n = prefixLength + nDigits;
		
		char[] chars = new char[n];
		
		// copy prefix
		for(int i = 0; i < prefixLength; i++) { 
			chars[i] = (char) prefixBytes[i]; 
		}
		
		// index
		for(int index = 0; index < nDigits; index++) {
			chars[n-1-index] = ENCODING[(int) (value & 0x3f)];
			value >>= 6;
		}
		return new String(chars);
	}
}
