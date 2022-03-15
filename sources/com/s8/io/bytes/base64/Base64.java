package com.s8.io.bytes.base64;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class Base64 {
	
	
	public static void main(String[] args) {
		
		System.out.println("is working: "+testing(100000));
		
	}
	
	
	private static boolean testing(int n) {
		long x, y;

		x = System.nanoTime() & 0xFFFFFFFFFFFFFFFl;
		
		for(int i=0; i<n; i++) {
			x = (x * 87628769777L + 98279872987L) & 0xFFFFFFFFFFFFFFFl;
			String encoded = Base64.encode(x);	
			y = Base64.decode(encoded);
			
			System.out.println("x: "+x+", y: "+y+", encoding: "+encoded);
			
			if(x!=y) {
				return false;
			}
		}
		return true;
	}

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
	
	
	public static String encode(long value) {
		char[] encoded = new char[10];
		for(int index=0; index<10; index++) {
			encoded[9-index] = ENCODING[(int) (value & 0x3f)];
			value >>= 6;
		}
		return new String(encoded);
	}
	
	
	public static long decode(String value) {
		long decoded = 0;
		for(int i=0; i<10; i++) {
			int c = decode(value.charAt(i));
			decoded <<= 6;
			decoded |= c;
		}
		return decoded;
	}
	
	
	
	
	
	public static int decode(char c) {
		switch(c) {
		
		case '0': return 0;
		case '1': return 1;
		case '2': return 2;
		case '3': return 3;
		case '4': return 4;
		case '5': return 5;
		case '6': return 6;
		case '7': return 7;
		
		case '8': return 8;
		case '9': return 9;
		case 'a': return 10;
		case 'b': return 11;
		case 'c': return 12;
		case 'd': return 13;
		case 'e': return 14;
		case 'f': return 15;
		
		case 'g': return 16;
		case 'h': return 17;
		case 'i': return 18;
		case 'j': return 19;
		case 'k': return 20;
		case 'l': return 21;
		case 'm': return 22;
		case 'n': return 23;
		
		case 'o': return 24;
		case 'p': return 25;
		case 'q': return 26;
		case 'r': return 27;
		case 's': return 28;
		case 't': return 29;
		case 'u': return 30;
		case 'v': return 31;
		
		case 'w': return 32;
		case 'x': return 33;
		case 'y': return 34;
		case 'z': return 35;
		case 'A': return 36;
		case 'B': return 37;
		case 'C': return 38;
		case 'D': return 39; 
		
		case 'E': return 40;
		case 'F': return 41;
		case 'G': return 42;
		case 'H': return 43;
		case 'I': return 44;
		case 'J': return 45;
		case 'K': return 46;
		case 'L': return 47;
		
		case 'M': return 48;
		case 'N': return 49;
		case 'O': return 50;
		case 'P': return 51;
		case 'Q': return 52;
		case 'R': return 53;
		case 'S': return 54;
		case 'T': return 55;
		
		case 'U': return 56;
		case 'V': return 57;
		case 'W': return 58;
		case 'X': return 59;
		case 'Y': return 60;
		case 'Z': return 61;
		case '$': return 62;
		case '&': return 63;
		
		default : throw new IllegalArgumentException("Illegal char: "+c);
		}
	}

}
