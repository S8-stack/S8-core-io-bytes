package com.qx.reactive.use;

import com.qx.reactive.output.QxOutflow;

public class WritingWithIOReactive_UseCase01 {

	public static void main(String[] args) {

		
		QxOutflow buffer = QxOutflow.createSocketOutflow(null, 1024, 1000);
		buffer.push(12.3).push(1265436).append();
		

	}


}
