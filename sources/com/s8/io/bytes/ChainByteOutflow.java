package com.s8.io.bytes;

import java.nio.ByteBuffer;


public class ChainByteOutflow extends AutoByteOutflow {

	private QxBytes head;

	private QxBytes tail;

	private int capacity;

	private int bytecount;

	public ChainByteOutflow(int capacity) {
		super();
		this.capacity = capacity;
		bytecount = 0;

		// first feed
		head = new QxBytes(new byte[capacity]);
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
		QxBytes nextLink = new QxBytes(capacity);

		// append to tail
		tail.next = nextLink;
		tail = nextLink;

		// feed buffer
		buffer = ByteBuffer.wrap(nextLink.bytes);

		// hard swap, so always succesful in pushing bytes
		return true;
	}


	public QxBytes getHead() {
		return head;
	}

	public int getSize() {
		return bytecount;
	}


}
