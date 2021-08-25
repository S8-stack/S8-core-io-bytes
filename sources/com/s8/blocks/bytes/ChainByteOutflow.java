package com.s8.blocks.bytes;

import java.nio.ByteBuffer;


public class ChainByteOutflow extends AutoByteOutflow {

	private Bytes head;

	private Bytes tail;

	private int capacity;

	private int bytecount;

	public ChainByteOutflow(int capacity) {
		super();
		this.capacity = capacity;
		bytecount = 0;

		// first feed
		head = new Bytes(new byte[capacity]);
		tail = head;

		// feed buffer
		buffer = ByteBuffer.wrap(tail.bytes);
	}

	@Override
	public boolean push() {

		// dump current link
		tail.length = buffer.position();
		bytecount+=tail.length;


		// create next tail link
		Bytes nextLink = new Bytes(capacity);

		// append to tail
		tail.next = nextLink;
		tail = nextLink;

		// feed buffer
		buffer = ByteBuffer.wrap(nextLink.bytes);

		// hard swap, so always succesful in pushing bytes
		return true;
	}


	public Bytes getHead() {
		return head;
	}

	public int getSize() {
		return bytecount;
	}


}
