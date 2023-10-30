package com.s8.io.bytes.tests;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.s8.core.io.bytes.BufferByteInflow;
import com.s8.core.io.bytes.BufferByteOutflow;

public class UInt7XTest02 {

	public static void main(String[] args) throws Exception {
		
		testCorrectness(true);

	}


	
	
	public static void testCorrectness(boolean isVerbose) throws Exception {
		PseudoRandomGenerator g = new PseudoRandomGenerator(10982970978230928L, 88337L, 121367L);
		
		testNegative(isVerbose);
		
		testPositive(g, 0xffL, 1024, isVerbose);
		testPositive(g, 0xffffL, 1024, isVerbose);
		testPositive(g, 0xffffffL, 1024, isVerbose);
		testPositive(g, 0xfffffffL, 1024, isVerbose);
		testPositive(g, 0x7ffffffffffL, 1024, isVerbose);
		testPositive(g, 0x7fffffffffffffffL, 1024, isVerbose);
	}
	

	
	public static void testNegative(boolean isVerbose) throws Exception {
		
		int length = 64;
		
		// allocate 16MB buffer
		ByteBuffer buffer = ByteBuffer.allocate(10*length);
		long[] benchmark = new long[length];
		
		
		BufferByteOutflow output = new BufferByteOutflow(buffer);
	
		// filling-in
		for(int i=0; i<length; i++) {
			long number = -i;
			if(isVerbose) {
				System.out.println(number);
			}
			output.putUInt7x(number);
			benchmark[i] = number;
		}
		
		buffer.flip();

		BufferByteInflow input =new BufferByteInflow(buffer);
		for(int i=0; i<length; i++) {
			if(input.getUInt7x() != benchmark[i]) {
				throw new IOException("Non matching number: "+benchmark[i]);
			}
		}
	}

	public static void testPositive(PseudoRandomGenerator g, long mask, int length, boolean isVerbose) throws Exception {
	
		// allocate 16MB buffer
		ByteBuffer buffer = ByteBuffer.allocate(10*length);
		long[] benchmark = new long[length];
		
		
		BufferByteOutflow output = new BufferByteOutflow(buffer);
	
		// filling-in
		for(int i=0; i<length; i++) {
			long number = (g.generate() & mask);
			if(isVerbose) {
				System.out.println(number);
			}
			output.putUInt7x(number);
			benchmark[i] = number;
		}
		
		buffer.flip();

		BufferByteInflow input =new BufferByteInflow(buffer);
		for(int i=0; i<length; i++) {
			if(input.getUInt7x() != benchmark[i]) {
				throw new IOException("Non matching number: "+benchmark[i]);
			}
		}
	
	}



	private static class PseudoRandomGenerator {
		
		private long seed, a, b;

		public PseudoRandomGenerator(long seed, long a, long b) {
			super();
			this.seed = seed;
			this.a = a;
			this.b = b;
		}
		
		public long generate() {
			return (seed = (a * seed) + b); 
		}
	}
}
