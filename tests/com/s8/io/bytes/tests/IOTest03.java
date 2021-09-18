package com.s8.io.bytes.tests;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.s8.alpha.bytes.ByteInflow;
import com.s8.blocks.bytes.demos.d0.FatDemoFile02;
import com.s8.blocks.bytes.linked.LinkedByteInflow;
import com.s8.blocks.bytes.linked.LinkedByteOutflow;
import com.s8.blocks.bytes.linked.LinkedBytes;
import com.s8.blocks.bytes.linked.LinkedBytesDiskReading;
import com.s8.blocks.bytes.linked.LinkedBytesDiskWriting;


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
		LinkedBytesDiskWriting writing = new LinkedBytesDiskWriting(head, path, true) {

			@Override
			public void onSucceed() {
				System.out.println("Write succeed");
			}

			@Override
			public void onFailed() {
				System.out.println("Write failed");
			}
		};
		writing.write();




		/**
		 * 
		 */
		LinkedBytesDiskReading reading = new LinkedBytesDiskReading(path, true) {

			@Override
			public void onSucceed(LinkedBytes head) {
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

			@Override
			public void onFailed(IOException exception) {
				System.out.println("Read failed");	
			}
		};
		
		reading.read();
	}

}
