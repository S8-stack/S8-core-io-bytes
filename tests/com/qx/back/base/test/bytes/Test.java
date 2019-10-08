package com.qx.back.base.test.bytes;

public class Test {

	public static void main(String[] args) {
		int a = 0x8f;
		int b = a;
		System.out.println((byte) a);
		System.out.println((a & 0x80) == 0x80);
		System.out.println(b);
		System.out.println((b & 0x80) == 0x80);
	}

}
