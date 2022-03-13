package com.s8.io.bytes.tests;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.s8.blocks.bytes.demos.d0.FatDemoFile02;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.linked.LinkedByteInflow;
import com.s8.io.bytes.linked.LinkedByteOutflow;
import com.s8.io.bytes.linked.LinkedBytes;
import com.s8.io.bytes.linked.LinkedBytesIO;


/**
 * 
 * @author pierreconvert
 *
 */
public class IOTest03 {


	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {


		int n = 170056;
		FatDemoFile02 file01 = new FatDemoFile02();
		file01.generateData(n);


		// serialize
		int buffering = 997;
		LinkedByteOutflow outflow = new LinkedByteOutflow(buffering);
		file01.serialize(outflow);


		// write
		LinkedBytes head = outflow.getHead();
		Path path = Paths.get("data/test_file");
		LinkedBytesIO.write(head, path, true);
		System.out.println("Write succeed");



		/**
		 * 
		 */
		head = LinkedBytesIO.read(path, true);
		try {
			System.out.println("Read succeed");
			ByteInflow inflow = new LinkedByteInflow(head);
			FatDemoFile02 file02 = FatDemoFile02.deserialize(inflow);

			System.out.println("Are files matching: "+file01.match(file02));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
