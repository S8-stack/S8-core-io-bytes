package com.s8.io.bytes.tests.base64;

import java.util.HashSet;

import com.s8.io.bytes.base64.Base64Generator;

public class TestIdgen02 {

	public static void main(String[] args) {

		Base64Generator generator = new Base64Generator("main:");
		System.out.println(generator.generate(12));
		System.out.println(generator.generate(1860));
		System.out.println(generator.generate(1809808909860L));
		System.out.println(generator.generate(108909860L));
		System.out.println(generator.generate(7779760L));
		
		
		long index = 47;
		
		HashSet<String> set = new HashSet<String>();
		for(int i=0; i<256000; i++) {
			set.add(generator.generate(index++));
		}
		System.out.println("set: > "+set.size());
		
		
		
	}

}
