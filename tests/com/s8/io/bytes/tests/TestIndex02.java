package com.s8.io.bytes.tests;

import com.s8.blocks.helium.utilities.index.QxIndex;

public class TestIndex02 {

	public static void main(String[] args) {
		
		QxIndex i0 = new QxIndex(new byte[] {
				(byte) 0x00, (byte) 0x01, (byte) 0x10, (byte) 0x01,
				(byte) 0xff, (byte) 0x23, (byte) 0xa1, (byte) 0xff
		});
		
		QxIndex i1 = QxIndex.fromHexadecimal("00011001ff23a1ff");

		System.out.println(i0.equals(i1));
		
		
	}

}
