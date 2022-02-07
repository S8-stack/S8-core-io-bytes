package com.s8.io.bytes.tests;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.s8.blocks.helium.BufferByteInflow;
import com.s8.blocks.helium.BufferByteOutflow;

public class TestUInt7X {

	public static void main(String[] args) throws Exception {
		
		ByteBuffer buffer = java.nio.ByteBuffer.allocate(1024);
		
		BufferByteOutflow output = new BufferByteOutflow(buffer);
		output.putUInt7x(12);
		output.putUInt7x(1235);
		output.putUInt7x(1235786876);
		output.putUInt7x(12386876);
		output.putUInt7x(65386);

		buffer.flip();
		BufferByteInflow input = new BufferByteInflow(buffer);
		System.out.println(input.getUInt7x());
		System.out.println(input.getUInt7x());
		System.out.println(input.getUInt7x());
		System.out.println(input.getUInt7x());
		System.out.println(input.getUInt7x());


		testCorrectness();

		testPerformance01(1);
		testPerformance01(1000);
		testPerformance01(1000000);
		testPerformance01(10000000);

	}



	public static void testCorrectness() throws Exception {
		int n = 1024*1024;
		ByteBuffer buffer = ByteBuffer.allocate(6*n);
		BufferByteOutflow output;
		BufferByteInflow input;
		int offset=0, index;
		while(offset<2e9) {
			System.out.println("testing correctness for range "+offset+" to "+(offset+n-1));
			buffer.clear();

			// fill
			index = offset;
			output = new BufferByteOutflow(buffer);
			for(int i=0; i<n; i++) {
				output.putUInt7x(index++);
			}

			// check
			buffer.flip();
			index = offset;
			input = new BufferByteInflow(buffer);
			for(int i=0; i<n; i++) {
				if(input.getUInt7x()!=index++) {
					throw new Exception("Bug with index="+index);
				}
			}
			offset+=17*n;
		}
	}

	public static void testPerformance01(int factor) throws IOException {
		int n = 1024*1024;
		ByteBuffer buffer = java.nio.ByteBuffer.allocate(4*n);
		BufferByteOutflow output;


		long t = System.nanoTime();
		for(int k=0; k<1000; k++) {
			output = new BufferByteOutflow(buffer);
			for(int i=0; i<n; i++) {
				output.putUInt32(factor*i%8);
			}
			buffer.clear();
		}

		System.out.println("testPerformance-"+factor+" > per putUInt32: "+(System.nanoTime()-t)/n+ " ns");

		buffer = java.nio.ByteBuffer.allocate(4*n);
		output = new BufferByteOutflow(buffer);
		t = System.nanoTime();
		for(int k=0; k<1000; k++) {
			output = new BufferByteOutflow(buffer);
			for(int i=0; i<n; i++) {
				output.putUInt7x(factor*i%8);
			}
			buffer.clear();
		}
		System.out.println("testPerformance-"+factor+" > per putUIntE: "+(System.nanoTime()-t)/n+ " ns");
	}


}
