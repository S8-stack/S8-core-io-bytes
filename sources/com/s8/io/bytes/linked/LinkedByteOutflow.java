package com.s8.io.bytes.linked;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.s8.io.bytes.AutoByteOutflow;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class LinkedByteOutflow extends AutoByteOutflow {

	
	public final static int DEFAULT_CAPACITY = 1024;
	
	/** first node of the linked chain */
	private LinkedBytes head;

	
	/** last node of the linked chain */
	private LinkedBytes tail;

	
	/** chunk capacity */
	private int capacity;

	
	/** bytecount */
	private int bytecount;
	
	private boolean isClosed;

	
	/**
	 * 
	 * @param capacity
	 */
	public LinkedByteOutflow() {
		super();
		initialize(0, DEFAULT_CAPACITY);
	}
	
	
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
		
		isClosed = false;
	}
	

	@Override
	public boolean push() {

		// dump current link
		tail.length = buffer.position();
		bytecount+=tail.length;

		// create next tail link
		
		
		/**
		 * BIG trick: we increase capacity every time we re-feed -> auto-adaptative
		 */
		capacity *= 2;
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
	 * @throws IOException 
	 */
	public LinkedBytes getHead() throws IOException {
		if(!isClosed) {
			
			// dump current link
			tail.length = buffer.position();
			bytecount += tail.length;
			
			isClosed = true;
			
			return head;
		}
		else {
			throw new IOException("Already closed");
		}
	}

	
	/**
	 * 
	 * @return
	 */
	public int getSize() {
		return bytecount;
	}


	@Override
	public void setCapacity(int capacity) {
		// change capacity that will be used to build next link
		this.capacity = capacity;
	}


}
