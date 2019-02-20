package com.qx.reactive.output;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author pc
 */
public class LinkedByteBuffer {
	
	private QxOutflowHead head;
	
	private long index;
	
	private long bytecount;

	AtomicBoolean isReleased;

	public ByteBuffer buffer;
	
	private LinkedByteBuffer next;

	private AtomicBoolean hasNext;


	/**
	 * 
	 * @param capacity
	 */
	public LinkedByteBuffer(QxOutflowHead head, long index, int capacity) {
		super();
		this.index = index;
		isReleased = new AtomicBoolean(true);
		buffer = ByteBuffer.allocate(capacity);
		hasNext = new AtomicBoolean(false);
		
		this.head = head;
	}


	/**
	 * 
	 * @return 
	 */
	public boolean hasNext() {
		return hasNext.get();
	}

	
	public long getIndex() {
		return index;
	}
	
	/**
	 * 
	 * @return the next node, creating it if necessary
	 */
	public LinkedByteBuffer getNext() {
		if(hasNext.get()) {
			return next;
		}
		else {
			return null;
		}
	}
	
	public LinkedByteBuffer append() {
		LinkedByteBuffer nextNode = new LinkedByteBuffer(head, index+1, buffer.capacity());
		next = nextNode;
		hasNext.set(true);

		// if a new node has been created, notify head
		head.send();	
		return nextNode;	
	}
	
	
	public void resetBytecount() {
		bytecount = 0;
	}
	
	
	public long getByteCount() {
		return bytecount;
	}


	
	public LinkedByteBuffer push(byte[] bytes) {
		return push(bytes, 0, bytes.length);
	}


	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public LinkedByteBuffer push(byte[] bytes, int offset, int length) {
		/*
		if(!isHead) {
			throw new RuntimeException("Not head");
		}
		*/
		if(buffer.remaining()>=length) {
			buffer.put(bytes, offset, length);
			return this;
		}
		else {
			int n = Math.min(buffer.remaining(), length);
			buffer.put(bytes, offset, n);
			return append().push(bytes, offset+n, length-n);
		}
	}
	
	

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public LinkedByteBuffer skip(int length) {
		/*
		if(!isHead) {
			throw new RuntimeException("Not head");
		}
		*/
		if(buffer.remaining()>=length) {
			buffer.position(buffer.position()+length);
			return this;
		}
		else {
			int n = Math.min(buffer.remaining(), length);
			buffer.position(buffer.position()+length);
			return append().skip(length-n);
		}
	}


	public boolean isReleased() {
		return isReleased.get();
	}
	
	
	
	public boolean isFull() {
		return !buffer.hasRemaining();
	}


	/**
	 * push a short value in the underlying buffer
	 * @param value
	 */
	public LinkedByteBuffer push(byte value) {
		if(buffer.remaining()>=1) {
			buffer.put(value);
			return this;
		}
		else { // create new node
			return append().push(value);
		}
	}
	
	/**
	 * push a short value in the underlying buffer
	 * @param value
	 */
	public LinkedByteBuffer push(short value) {
		if(buffer.remaining()>=2) {
			buffer.putShort(value);
			return this;
		}
		else { // create new node
			return append().push(value);
		}
	}
	
	
	public LinkedByteBuffer push(int value) {
		if(buffer.remaining()>=4) {
			buffer.putInt(value);
			return this;
		}
		else { // create new node
			return append().push(value);
		}
	}
	
	public LinkedByteBuffer push(long value) {
		if(buffer.remaining()>=8) {
			buffer.putLong(value);
			return this;
		}
		else { // create new node
			return append().push(value);
		}
	}
	
	public LinkedByteBuffer push(float value) {
		if(buffer.remaining()>=4) {
			buffer.putFloat(value);
			return this;
		}
		else { // create new node
			return append().push(value);
		}
	}
	
	
	public LinkedByteBuffer push(double value) {
		if(buffer.remaining()>=8) {
			buffer.putDouble(value);
			return this;
		}
		else { // create new node
			return append().push(value);
		}
	}


	public boolean hasRemaining() {
		return !buffer.hasRemaining();
	}

}
