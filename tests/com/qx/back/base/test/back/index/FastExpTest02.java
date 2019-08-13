package com.qx.back.base.test.back.index;

import com.qx.base.index.UIntMap;

public class FastExpTest02 {

	public static void main(String[] args) {

		System.out.println("2^1: "+Math.pow(2.0,  1.0)+" = "+UIntMap.powerOfTwo(1));
		System.out.println("2^4: "+Math.pow(2.0,  4.0)+" = "+UIntMap.powerOfTwo(4));
		System.out.println("2^8: "+Math.pow(2.0,  8.0)+" = "+UIntMap.powerOfTwo(8));
		System.out.println("2^12: "+Math.pow(2.0,  12.0)+" = "+UIntMap.powerOfTwo(12));
		System.out.println("2^17: "+Math.pow(2.0,  17.0)+" = "+UIntMap.powerOfTwo(17));
		
		System.out.println("2^6+: "+UIntMap.lowerPowerOfTwoExponent(65));
		System.out.println("2^12+: "+UIntMap.lowerPowerOfTwoExponent(5000));
		System.out.println("2^16+: "+UIntMap.lowerPowerOfTwoExponent(70000));
		
		System.out.println("mask 6: "+UIntMap.mask(6));
		System.out.println("mask 8: "+UIntMap.mask(8));
		System.out.println("mask 12: "+UIntMap.mask(12));
	}

}
