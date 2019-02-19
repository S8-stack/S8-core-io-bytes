package com.qx.reactive.output;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author pc
 */
public class QxOutflow {
	
	private long index;
	
	private long bytecount;

	private QxOutflowHead head;

	private AtomicBoolean isReleased;

	public ByteBuffer buffer;
	
	private QxOutflow next;

	private AtomicBoolean hasNext;


	/**
	 * 
	 * @param capacity
	 */
	public QxOutflow(QxOutflowHead head, long index, int capacity) {
		super();
		this.head = head;
		this.index = index;
		isReleased = new AtomicBoolean(true);
		buffer = ByteBuffer.allocate(capacity);
		hasNext = new AtomicBoolean(false);
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
	 * append a new node and return it
	 */
	public QxOutflow push() {
		QxOutflow nextNode = new QxOutflow(head, index+1, buffer.capacity());
		next = nextNode;
		hasNext.set(true);
		
		// trigger consumption by the head
		head.send();
		
		return nextNode;
	}
	
	
	/**
	 * Notify to the head of this linked list that new nodes are available for consumption
	 */
	public void send() {
		head.send();
	}	

	
	public void resetBytecount() {
		bytecount = 0;
	}
	
	
	public long getByteCount() {
		return bytecount;
	}

	/**
	 * 
	 * @return
	 */
	public QxOutflow next() {
		if(hasNext.get()) {
			return next;
		}
		else {
			return null;
		}
	}
	
	
	public QxOutflow push(byte[] bytes) {
		return push(bytes, 0, bytes.length);
	}


	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public QxOutflow push(byte[] bytes, int offset, int length) {
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
			return push().push(bytes, offset+n, length-n);
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
	public QxOutflow push(byte value) {
		if(buffer.remaining()>=1) {
			buffer.put(value);
			return this;
		}
		else { // create new node
			return push().push(value);
		}
	}
	
	/**
	 * push a short value in the underlying buffer
	 * @param value
	 */
	public QxOutflow push(short value) {
		if(buffer.remaining()>=2) {
			buffer.putShort(value);
			return this;
		}
		else { // create new node
			return push().push(value);
		}
	}
	
	
	public QxOutflow push(int value) {
		if(buffer.remaining()>=4) {
			buffer.putInt(value);
			return this;
		}
		else { // create new node
			return push().push(value);
		}
	}
	
	public QxOutflow push(long value) {
		if(buffer.remaining()>=8) {
			buffer.putLong(value);
			return this;
		}
		else { // create new node
			return push().push(value);
		}
	}
	
	public QxOutflow push(float value) {
		if(buffer.remaining()>=4) {
			buffer.putFloat(value);
			return this;
		}
		else { // create new node
			return push().push(value);
		}
	}
	
	
	public QxOutflow push(double value) {
		if(buffer.remaining()>=8) {
			buffer.putDouble(value);
			return this;
		}
		else { // create new node
			return push().push(value);
		}
	}


	public boolean hasRemaining() {
		return !buffer.hasRemaining();
	}

	
	public static QxOutflow socket(
			AsynchronousSocketChannel channel, 
			int capacity, 
			long timeout) {
		
		QxOutflowHead head = new SocketHead(channel, capacity, timeout);
		return head.getCurrent();
	}
	
	
	public static QxOutflow debug(LinkedList<ByteBuffer> queue, int capacity, boolean isVerbose) {
		QxOutflowHead head = new DebugHead(capacity, queue, isVerbose);
		return head.getCurrent();
	}
	

}
