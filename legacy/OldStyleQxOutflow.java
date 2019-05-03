package com.qx.reactive.output;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.LinkedList;


/**
 * @author pc
 */
public class OldStyleQxOutflow {


	private QxOutflowHead head;

	private LinkedByteBuffer current;

	/**
	 * 
	 * @param capacity
	 */
	public OldStyleQxOutflow(QxOutflowHead head, int capacity) {
		super();
		this.head = head;
		this.current = head.initialize(capacity);
	}


	/**
	 * append a new node and return it
	 */
	public void push() {
		current = current.append();
	}


	/**
	 * Notify to the head of this linked list that new nodes are available for consumption
	 */
	public void send() {
		head.send();
	}	


	public void resetBytecount() {
		current.resetBytecount();
	}


	public long getByteCount() {
		return current.getByteCount();
	}


	public void push(byte[] bytes) {
		current = current.push(bytes, 0, bytes.length);
	}


	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public void push(byte[] bytes, int offset, int length) {
		current = current.push(bytes, offset, length);
	}
	
	
	public LaterPush reserve(int length) {
		current.isReleased.set(false);
		LinkedByteBuffer node = current;
		int position = current.buffer.position();
		return new LaterPush(current, position, length);
	}

	
	public void skip(int length) {
		current = current.skip(length);
	}

	/**
	 * push a short value in the underlying buffer
	 * @param value
	 */
	public void push(byte value) {
		current = current.push(value);
	}

	/**
	 * push a short value in the underlying buffer
	 * @param value
	 */
	public void push(short value) {
		current = current.push(value);
	}


	public void push(int value) {
		current = current.push(value);
	}

	public void push(long value) {
		current = current.push(value);
	}

	public void push(float value) {
		current = current.push(value);
	}


	public void push(double value) {
		current = current.push(value);
	}




	public static OldStyleQxOutflow socket(
			AsynchronousSocketChannel channel, 
			int capacity, 
			long timeout) {
		return new OldStyleQxOutflow(new SocketHead(channel, timeout), capacity);
	}


	public static OldStyleQxOutflow debug(LinkedList<ByteBuffer> queue, int capacity, boolean isVerbose) {
		return new OldStyleQxOutflow(new DebugHead(queue, isVerbose), capacity);
	}


}
