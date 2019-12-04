package com.qx.back.base.test.index;

import com.qx.level0.utilities.index.QxIndex;
import com.qx.level0.utilities.index.QxMap;

public class TestIndexMap02 {

	public static void main(String[] args) {
		
		int nElements = 1000000;
	
		QxMap map = new QxMap();
		
		for(int i=0; i<nElements; i++) {
			map.put(QxIndex.fromInt(8, i*10+20), Integer.valueOf(i));
		}
		
		Integer object;
		for(int i=0; i<nElements; i++) {
			object = (Integer) map.get(QxIndex.fromInt(8, i*10+20));
			if(i!=object) {
				System.out.println("mismatch");
			}
		}
		

		for(int i=0; i<nElements-12; i++) {
			map.remove(QxIndex.fromInt(8, i*10+20));
		}
	}

}
