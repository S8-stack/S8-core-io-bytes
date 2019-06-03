package com.qx.back.base.bytes;

import java.nio.ByteBuffer;


public class ChainByteOutflow extends AutoByteOutflow {

	private BytesChainLink head;

	private BytesChainLink tail;

	private int capacity;

	private int bytecount;
	
	public ChainByteOutflow(int capacity) {
		super();
		this.capacity = capacity;
		bytecount = 0;
	}

	@Override
	protected void feed() {
		if(tail==null) {
			head = new BytesChainLink(new byte[capacity]);
			tail = head;
			
			// feed buffer
			buffer = ByteBuffer.wrap(tail.bytes);
		}
		else {
			// create next tail link
			BytesChainLink nextLink = new BytesChainLink(capacity);

			// append to tail
			tail.next = nextLink;
			tail = nextLink;

			// feed buffer
			buffer = ByteBuffer.wrap(nextLink.bytes);
		}
	}
	
	@Override
	protected void dump() {
		tail.length = buffer.position();
		bytecount+=tail.length;
	}

	public BytesChainLink getHead() {
		return head;
	}
	
	public int getSize() {
		return bytecount;
	}

}
