package com.qx.back.base.test.index;

import com.qx.base.index.QxUIntMap;
import com.qx.base.index.QxUInt;

public class TestIndexMap02 {

	public static void main(String[] args) {
		
		int nElements = 1000000;
	
		QxUIntMap map = new QxUIntMap();
		
		for(int i=0; i<nElements; i++) {
			map.put(new QxUInt(8, i*10+20), Integer.valueOf(i));
		}
		
		Integer object;
		for(int i=0; i<nElements; i++) {
			object = (Integer) map.get(new QxUInt(8, i*10+20));
			if(i!=object) {
				System.out.println("mismatch");
			}
		}
		

		for(int i=0; i<nElements-12; i++) {
			map.remove(new QxUInt(8, i*10+20));
		}
	}

}
