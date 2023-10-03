package com.s8.blocks.bytes.demos.d0;

import java.io.IOException;

import com.s8.api.bytes.ByteInflow;
import com.s8.api.bytes.ByteOutflow;

public class FatDemoFile02 {


	public long[] values;

	public int[] formats;



	/**
	 * Not serialized
	 */
	public long checkSum;

	public FatDemoFile02() {
		super();
	}



	public void generateData(int n) {
		values = new long[n];
		formats = new int[n];
		checkSum = 0;

		long value;
		int format;
		for(int i=0; i<n; i++) {
			value = (long) (Math.random()*2087);
			format = (int) (Math.random()*3);
			if(format==0) {
				checkSum += value;
			}
			values[i] = value;
			formats[i] = format;
		}
	}


	
	/**
	 * 
	 * @param outflow
	 * @throws IOException
	 */
	public void serialize(ByteOutflow outflow) throws IOException {
		int n = values.length;
		outflow.putUInt32(n);
		for(int i=0; i<n; i++) {

			int format = formats[i];

			outflow.putUInt8(format);

			switch(format) {
			case 0 : // 8  bytes
				outflow.putInt64(values[i]); 
				break;

			case 1 : // 4 bytes
				outflow.putInt32((int) values[i]); 
				break;

			case 2 : // 1 byte
				outflow.putUInt8(((short) values[i]) & 0xff); 
				break;
			}
		}
	}



	/**
	 * 
	 * @param inflow
	 * @return
	 * @throws IOException 
	 */
	public static FatDemoFile02 deserialize(ByteInflow inflow) throws IOException {
		int n = inflow.getUInt31();

		int[] formats = new int[n];
		long[] values = new long[n];
		long sum = 0;


		for(int i=0; i<n; i++) {
			int format = inflow.getUInt8();
			formats[i] = format; 

			switch(format) {

			case 0 :
				long val = inflow.getInt64();
				values[i] = val;
				sum+=val;
				break;

			case 1: values[i] = inflow.getInt32(); break;

			case 2: values[i] = inflow.getUInt8(); break;
			
			default: throw new IOException("Unsupported file format");
			}
		}
		
		FatDemoFile02 file = new FatDemoFile02();
		file.formats = formats;
		file.values = values;
		file.checkSum = sum;
		
		return file;
	}
	
	
	public boolean match(FatDemoFile02 right) {
		int n;
		if((n = values.length)!=right.values.length) {
			return false;
		}
		
		for(int i=0; i<n; i++) {
			if(formats[i] == 0 && (values[i]!=right.values[i])) {
				return false;
			}
		}
		
		return true;
	}

}
