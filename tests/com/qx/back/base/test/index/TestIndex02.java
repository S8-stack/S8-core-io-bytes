package com.qx.back.base.test.index;

import com.qx.base.index.QxUInt;

public class TestIndex02 {

	public static void main(String[] args) {
		
		QxUInt i0 = new QxUInt(new byte[] {
				(byte) 0x00, (byte) 0x01, (byte) 0x10, (byte) 0x01,
				(byte) 0xff, (byte) 0x23, (byte) 0xa1, (byte) 0xff
		});
		
		QxUInt i1 = new QxUInt("00011001ff23a1ff");

		System.out.println(i0.equals(i1));
		
		
	}

}
