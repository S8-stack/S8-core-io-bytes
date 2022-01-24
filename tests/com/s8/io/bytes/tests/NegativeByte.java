package com.s8.io.bytes.tests;

public class NegativeByte {

	public static void main(String[] args) {

		int value = -32;
		byte b = (byte) (value & 0x3f);
		System.out.print(b);
	}

}
