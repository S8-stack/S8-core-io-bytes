package com.qx.reactive.use;

import com.qx.reactive.output.OldStyleQxOutflow;

public class WritingWithIOReactive_UseCase01 {

	public static void main(String[] args) {

		
		OldStyleQxOutflow buffer = OldStyleQxOutflow.createSocketOutflow(null, 1024, 1000);
		buffer.push(12.3).push(1265436).append();
		

	}


}
