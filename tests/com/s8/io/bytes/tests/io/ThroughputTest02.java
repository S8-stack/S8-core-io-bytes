package com.s8.io.bytes.tests.io;

import java.io.IOException;
import java.nio.file.Path;

import com.s8.blocks.bytes.linked.LinkedBytes;
import com.s8.blocks.bytes.linked.LinkedBytesIO;

public class ThroughputTest02 {


	public static void main(String[] args) throws IOException {


		LinkedBytes head = null, tail = null;
		int chunksize = 1024;
		int nChunks = 100000;
		int seed = 1987;
		for(int i=0; i<nChunks; i++) {
			LinkedBytes link = new LinkedBytes(chunksize);
			
			byte[] bytes = link.bytes;
			for(int j=0; j<chunksize; j++) {
				bytes[j] = (byte) seed;
				seed = 31*seed +17;
			}
			if(head == null) {
				head = link;
				tail = link;
			}
			else {
				tail.next = link;
				tail = link;
			}
		}

		long time = System.nanoTime();
		LinkedBytesIO.write(head, Path.of("data/perf/test.io"), true);
		time = System.nanoTime() - time;
		System.out.println("Time: "+time+"ns");
		double speed = ((double) nChunks*chunksize)/((double) time) * 1e-6 * 1e9;
		System.out.println("Throughput: "+((int) speed)+" MB/s, "+((int) (speed*8))+" Mb/s.");
		
	}
}
