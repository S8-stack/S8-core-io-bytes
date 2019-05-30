package com.qx.back.base.test.back.index;

import com.qx.back.base.index.UnsignedInteger;

public class TestIndex01 {

	public static void main(String[] args) {
		
		byte[] bytes = { (byte) 0x00, (byte) 0x00, (byte) 0xaf };
		
		UnsignedInteger index = new UnsignedInteger(bytes);

		System.out.println("i0->Int32:"+index.toInt32());
		System.out.println("i0->Int32:"+Integer.decode("0xaf"));

		
		bytes = new byte[]{ 
				(byte) 0x00, (byte) 0x00, (byte) 0xaf, (byte) 0x01, 
				(byte) 0x02, (byte) 0x04, (byte) 0xaa, (byte) 0x2d  };
		
		index = new UnsignedInteger(bytes);
		

		System.out.println("i0->Int32:"+index.toInt32());
		System.out.println("i0->Int32:"+Integer.decode("0x0204aa2d"));
		
		System.out.println("i0:"+index);
		index.increment();
		System.out.println("i0++:"+index);
		
		index.increment();
		System.out.println("i0++->Int32:"+index.toInt32());
	}

}
