package com.s8.io.bytes.utilities.index;

import java.util.HashMap;


/**
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class BenchmarkTestIndexMap04 {

	public static void main(String[] args) {

		run_HashMap();
		run_QxMap();
	}

	public static void run_QxMap() {
		for(int iTest=0; iTest<10; iTest++) {
			long t = System.nanoTime();
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
			t = System.nanoTime() - t;
			System.out.println("test for QxMap: "+t/1000000+" ns");	
		}
		
	}

	public static void run_HashMap() {

		for(int iTest=0; iTest<10; iTest++) {
			long t = System.nanoTime();
			int nElements = 1000000;

			HashMap<QxIndex, Integer> map = new HashMap<>();

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
			t = System.nanoTime() - t;
			System.out.println("test for HashMap: "+t/1000000+" ns");	
		}
		
	}

}
