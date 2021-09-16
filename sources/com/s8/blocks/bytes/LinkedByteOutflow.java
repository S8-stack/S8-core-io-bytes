package com.s8.blocks.bytes;

import java.nio.ByteBuffer;


/**
 * 
 * @author pierreconvert
 *
 */
public class LinkedByteOutflow extends AutoByteOutflow {

	
	/** first node of the linked chain */
	private LinkedBytes head;

	
	/** last node of the linked chain */
	private LinkedBytes tail;

	
	/** chunk capacity */
	private int capacity;

	
	/** bytecount */
	private int bytecount;

	
	/**
	 * 
	 * @param capacity
	 */
	public LinkedByteOutflow(int capacity) {
		super();
		initialize(0, capacity);
	}
	
	
	/**
	 * 
	 * @param capacity
	 */
	public LinkedByteOutflow(int initialOffset, int capacity) {
		super();
		initialize(initialOffset, capacity);
	}


	/**
	 * 
	 * @param initialOffset
	 * @param capacity
	 */
	private void initialize(int initialOffset, int capacity) {
		this.capacity = capacity;
		this.bytecount = 0;

		// first feed
		head = new LinkedBytes(new byte[capacity+initialOffset], initialOffset, capacity);
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
		LinkedBytes nextLink = new LinkedBytes(capacity);

		// append to tail
		tail.next = nextLink;
		tail = nextLink;

		// feed buffer
		buffer = ByteBuffer.wrap(nextLink.bytes);

		// hard swap, so always succesful in pushing bytes
		return true;
	}


	/**
	 * 
	 * @return
	 */
	public LinkedBytes getHead() {
		return head;
	}

	
	/**
	 * 
	 * @return
	 */
	public int getSize() {
		return bytecount;
	}
}
