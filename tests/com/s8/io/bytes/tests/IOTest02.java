package com.s8.io.bytes.tests;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.s8.alpha.utilities.bytes.ByteInflow;
import com.s8.io.bytes.linked.LinkedByteInflow;
import com.s8.io.bytes.linked.LinkedByteOutflow;
import com.s8.io.bytes.linked.LinkedBytes;
import com.s8.io.bytes.linked.LinkedBytesIO;


/**
 * 
 * @author pierreconvert
 *
 */
public class IOTest02 {


	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {


		int n = 170056;
		double[] values = new double[n];
		int[] formats = new int[n];
		for(int i=0; i<n; i++) {
			values[i] = Math.random()*208009.9087;
			formats[i] = (int) (Math.random()*3);
		}

		int buffering = 997;
		LinkedByteOutflow outflow = new LinkedByteOutflow(buffering);
		for(int i=0; i<n; i++) {
			switch(formats[i]) {
			case 0 : outflow.putFloat64(values[i]); break;
			case 1 : outflow.putFloat32((float) values[i]); break;
			case 2 : outflow.putUInt8(((int) values[i]) & 0xff); break;
			}
		}
		LinkedBytes head = outflow.getHead();
		Path path = Paths.get("data/test_file");
		LinkedBytesIO.write(head, path, true);




		head = LinkedBytesIO.read(path, true);


		System.out.println("Read succeed");
		ByteInflow inflow = new LinkedByteInflow(head);
		double sum = 0;
		try {
			for(int i=0; i<n; i++) {
				switch(formats[i]) {

				case 0 :
					double val0 = inflow.getFloat64();
					if(val0 != values[i]) {
						System.out.println("MISMATCH");
					}	
					break;

				case 1:
					float val1 = inflow.getFloat32();
					sum+=val1;
					break;

				case 2:
					int val2 = inflow.getUInt8();
					sum+=val2;
					break;
				}
			}
			System.out.println("Matching terminated ("+sum+")");
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

}
