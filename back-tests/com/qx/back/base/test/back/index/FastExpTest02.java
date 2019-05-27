package com.qx.back.base.test.back.index;

import com.qx.back.base.index.QxIndexMap;

public class FastExpTest02 {

	public static void main(String[] args) {

		System.out.println("2^1: "+Math.pow(2.0,  1.0)+" = "+QxIndexMap.powerOfTwo(1));
		System.out.println("2^4: "+Math.pow(2.0,  4.0)+" = "+QxIndexMap.powerOfTwo(4));
		System.out.println("2^8: "+Math.pow(2.0,  8.0)+" = "+QxIndexMap.powerOfTwo(8));
		System.out.println("2^12: "+Math.pow(2.0,  12.0)+" = "+QxIndexMap.powerOfTwo(12));
		System.out.println("2^17: "+Math.pow(2.0,  17.0)+" = "+QxIndexMap.powerOfTwo(17));
		
		System.out.println("2^6+: "+QxIndexMap.lowerPowerOfTwoExponent(65));
		System.out.println("2^12+: "+QxIndexMap.lowerPowerOfTwoExponent(5000));
		System.out.println("2^16+: "+QxIndexMap.lowerPowerOfTwoExponent(70000));
		
		System.out.println("mask 6: "+QxIndexMap.mask(6));
		System.out.println("mask 8: "+QxIndexMap.mask(8));
		System.out.println("mask 12: "+QxIndexMap.mask(12));
	}

}
