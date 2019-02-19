package com.qx.reactive.output;

import java.nio.ByteBuffer;
import java.util.LinkedList;

public class DebugHead implements QxOutflowHead {
	
	private QxOutflow current;
	
	private LinkedList<ByteBuffer> queue;
	
	public DebugHead(int capacity, LinkedList<ByteBuffer> queue) {
		this.current = new QxOutflow(this, 0, capacity);
		this.queue = queue;
	}
	
	@Override
	public void send() {
		if(current.isReleased() && current.hasNext()) { // has something to outflow
			queue.addLast(current.buffer);
			System.out.println("\n -> [DebugHead] Appending buffer "+current.getIndex());
			current = current.next();
		}
	}

	@Override
	public QxOutflow getCurrent() {
		return current;
	}
	

}
