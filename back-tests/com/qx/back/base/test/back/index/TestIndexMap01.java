package com.qx.back.base.test.back.index;

import com.qx.back.base.index.UnsignedInteger;
import com.qx.back.base.index.UIntMap;

public class TestIndexMap01 {

	public static void main(String[] args) {
		
		int nElements = 1000000;
	
		UIntMap map = new UIntMap();
		
		UnsignedInteger index = new UnsignedInteger(new byte[]{ 
				(byte) 0xaf, (byte) 0x01, (byte) 0x02, (byte) 0x04, 
				(byte) 0xaa, (byte) 0x00, (byte) 0x00, (byte) 0x00 });
		for(int i=0; i<nElements; i++) {
			map.put(index, Integer.valueOf(i));
			index.increment();
		}
		
		
		index = new UnsignedInteger(new byte[]{ 
				(byte) 0xaf, (byte) 0x01, (byte) 0x02, (byte) 0x04, 
				(byte) 0xaa, (byte) 0x00, (byte) 0x00, (byte) 0x00 });
		Integer object;
		for(int i=0; i<nElements; i++) {
			object = (Integer) map.get(index);
			if(i!=object) {
				System.out.println("mismatch");
			}
			index.increment();
		}
		
		index = new UnsignedInteger(new byte[]{ 
				(byte) 0xaf, (byte) 0x01, (byte) 0x02, (byte) 0x04, 
				(byte) 0xaa, (byte) 0x00, (byte) 0x00, (byte) 0x00 });
		
		for(int i=0; i<nElements-12; i++) {
			map.remove(index);
			index.increment();
		}
	}

}
