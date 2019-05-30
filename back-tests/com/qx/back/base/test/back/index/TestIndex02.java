package com.qx.back.base.test.back.index;

import com.qx.back.base.index.UnsignedInteger;

public class TestIndex02 {

	public static void main(String[] args) {
		
		UnsignedInteger i0 = new UnsignedInteger(new byte[] {
				(byte) 0x00, (byte) 0x01, (byte) 0x10, (byte) 0x01,
				(byte) 0xff, (byte) 0x23, (byte) 0xa1, (byte) 0xff
		});
		
		UnsignedInteger i1 = new UnsignedInteger("00011001ff23a1ff");

		System.out.println(i0.equals(i1));
		
		
	}

}
