package com.qx.reactive.use;

import com.qx.reactive.input2.QxInflow2;

public class ReadingWithQxInflow_Test01 {

	public static int b;

	public static double a;

	public static void main(String[] args) {



	}


	public static class Reader {

		private int a, b;

		public void read(QxInflow2 inflow2) {

			QxInflow2 inflow = new QxInflow2();
			inflow.pullInteger(i -> { 
				a = i;
				inflow.pullInteger(i2 -> { 
					b = i2;});
			});
		}
	}
}
