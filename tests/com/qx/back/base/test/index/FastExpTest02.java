package com.qx.back.base.test.index;

import com.qx.base.index.QxUIntMap;

public class FastExpTest02 {

	public static void main(String[] args) {

		System.out.println("2^1: "+Math.pow(2.0,  1.0)+" = "+QxUIntMap.powerOfTwo(1));
		System.out.println("2^4: "+Math.pow(2.0,  4.0)+" = "+QxUIntMap.powerOfTwo(4));
		System.out.println("2^8: "+Math.pow(2.0,  8.0)+" = "+QxUIntMap.powerOfTwo(8));
		System.out.println("2^12: "+Math.pow(2.0,  12.0)+" = "+QxUIntMap.powerOfTwo(12));
		System.out.println("2^17: "+Math.pow(2.0,  17.0)+" = "+QxUIntMap.powerOfTwo(17));
		
		System.out.println("2^6+: "+QxUIntMap.lowerPowerOfTwoExponent(65));
		System.out.println("2^12+: "+QxUIntMap.lowerPowerOfTwoExponent(5000));
		System.out.println("2^16+: "+QxUIntMap.lowerPowerOfTwoExponent(70000));
		
		System.out.println("mask 6: "+QxUIntMap.mask(6));
		System.out.println("mask 8: "+QxUIntMap.mask(8));
		System.out.println("mask 12: "+QxUIntMap.mask(12));
	}

}
