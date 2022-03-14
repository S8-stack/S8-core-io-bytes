package com.s8.io.bytes.tests.base64;

import com.s8.io.bytes.utilities.base64.Base64Generator;

public class TestIdgen02 {

	public static void main(String[] args) {

		Base64Generator generator = new Base64Generator("main:");
		System.out.println(generator.generate(12));
		System.out.println(generator.generate(1860));
		System.out.println(generator.generate(1809808909860L));
		System.out.println(generator.generate(108909860L));
		System.out.println(generator.generate(7779760L));
		
		long code = 0x00L;
		while(code < 0xfffffffL) {
			System.out.println(generator.generate(code));
			code+=1727;
		}
	}

}
