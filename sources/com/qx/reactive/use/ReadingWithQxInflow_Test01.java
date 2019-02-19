package com.qx.reactive.use;

import com.qx.reactive.input.QxInflow;

public class ReadingWithQxInflow_Test01 {

	public static int b;

	public static double a;

	public static void main(String[] args) {

		QxInflow inflow = new QxInflow() {

		};


	}
	
	public class MyFrameHeader {
		
		// 32bits id
		public int identifier;
		
		// 8bits flags
		public boolean flag1;
		public boolean flag2;
		
		// if flag1, 16bits length
		public int length;
		
		
		public void on(QxInflow inflow) {
			inflow.
			readInt32(val -> identifier).
			readFlags( b -> { flag1&0x01==0x01; if(flag1) {
				return 2;
			})
			
		}
		
		
	}

}
