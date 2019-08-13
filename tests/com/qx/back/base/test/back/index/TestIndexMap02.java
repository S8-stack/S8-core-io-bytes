package com.qx.back.base.test.back.index;

import com.qx.base.index.UIntMap;
import com.qx.base.index.UnsignedInteger;

public class TestIndexMap02 {

	public static void main(String[] args) {
		
		int nElements = 1000000;
	
		UIntMap map = new UIntMap();
		
		for(int i=0; i<nElements; i++) {
			map.put(new UnsignedInteger(8, i*10+20), Integer.valueOf(i));
		}
		
		Integer object;
		for(int i=0; i<nElements; i++) {
			object = (Integer) map.get(new UnsignedInteger(8, i*10+20));
			if(i!=object) {
				System.out.println("mismatch");
			}
		}
		

		for(int i=0; i<nElements-12; i++) {
			map.remove(new UnsignedInteger(8, i*10+20));
		}
	}

}
