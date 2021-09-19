package com.s8.blocks.bytes.demos.d0;

import java.io.IOException;

import com.s8.alpha.bytes.ByteInflow;
import com.s8.alpha.bytes.ByteOutflow;

public class FatDemoFile02 {


	public double[] values;

	public int[] formats;



	/**
	 * Not serialized
	 */
	public double checkSum;

	public FatDemoFile02() {
		super();
	}



	public void generateData(int n) {
		values = new double[n];
		formats = new int[n];
		checkSum = 0;

		double value;
		int format;
		for(int i=0; i<n; i++) {
			value = Math.random()*208009.9087;
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
				outflow.putFloat64(values[i]); 
				break;

			case 1 : // 4 bytes
				outflow.putFloat32((float) values[i]); 
				break;

			case 2 : // 1 byte
				outflow.putUInt8(((int) values[i]) & 0xff); 
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
		int n = inflow.getUInt32();

		int[] formats = new int[n];
		double[] values = new double[n];
		double sum = 0;


		for(int i=0; i<n; i++) {
			int format = inflow.getUInt8();
			formats[i] = format; 

			switch(format) {

			case 0 :
				double val = inflow.getFloat64();
				values[i] = val;
				sum+=val;
				break;

			case 1: values[i] = inflow.getFloat32(); break;

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
