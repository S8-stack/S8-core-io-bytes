package com.qx.reactive.output;

import java.nio.ByteBuffer;
import java.util.Queue;

/**
 * 
 * @author pc
 *
 */
public interface QxActiveOutput {

	
	/**
	 * 
	 * @param buffer
	 */
	public void on(Queue<ByteBuffer> buffer);
	
}
