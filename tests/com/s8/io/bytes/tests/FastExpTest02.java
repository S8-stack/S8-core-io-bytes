package com.s8.io.bytes.tests;

import com.s8.io.bytes.utilities.index.QxMap;

public class FastExpTest02 {

	public static void main(String[] args) {

		System.out.println("2^1: "+Math.pow(2.0,  1.0)+" = "+QxMap.powerOfTwo(1));
		System.out.println("2^4: "+Math.pow(2.0,  4.0)+" = "+QxMap.powerOfTwo(4));
		System.out.println("2^8: "+Math.pow(2.0,  8.0)+" = "+QxMap.powerOfTwo(8));
		System.out.println("2^12: "+Math.pow(2.0,  12.0)+" = "+QxMap.powerOfTwo(12));
		System.out.println("2^17: "+Math.pow(2.0,  17.0)+" = "+QxMap.powerOfTwo(17));
		
		System.out.println("2^6+: "+QxMap.lowerPowerOfTwoExponent(65));
		System.out.println("2^12+: "+QxMap.lowerPowerOfTwoExponent(5000));
		System.out.println("2^16+: "+QxMap.lowerPowerOfTwoExponent(70000));
		
		System.out.println("mask 6: "+QxMap.mask(6));
		System.out.println("mask 8: "+QxMap.mask(8));
		System.out.println("mask 12: "+QxMap.mask(12));
	}

}
