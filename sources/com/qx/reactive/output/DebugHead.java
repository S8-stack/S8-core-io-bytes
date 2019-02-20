package com.qx.reactive.output;

import java.nio.ByteBuffer;
import java.util.LinkedList;

public class DebugHead implements QxOutflowHead {
	
	private boolean isVerbose;
	
	private LinkedByteBuffer current;
	
	private LinkedList<ByteBuffer> queue;
	
	public DebugHead(LinkedList<ByteBuffer> queue, boolean isVerbose) {
		this.queue = queue;
		this.isVerbose = isVerbose;
	}
	
	@Override
	public void send() {
		if(current.isReleased() && current.hasNext()) { // has something to outflow
			queue.addLast(current.buffer);
			if(isVerbose) {
				System.out.println("\n -> [DebugHead] Appending buffer "+current.getIndex());	
			}
			current = current.getNext();
		}
	}

	@Override
	public LinkedByteBuffer initialize(int capacity) {
		return current = new LinkedByteBuffer(this, 0, capacity);
	}
	
}
