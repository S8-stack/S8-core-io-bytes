package com.qx.reactive;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

public interface QxOutflow2 {
	
	public class Node {

		public ByteBuffer buffer;

		public AtomicBoolean isOutflowable;

		public Node next;

		public Node() {
			super();
			buffer = ByteBuffer.allocate(capacity);
			isOutflowable = new AtomicBoolean(false);
		}
	}
	
	private int capacity;
	
	public QxOutflow(int capacity) {
		super();
		this.capacity = capacity;
	}
	

	public void push(QxOutflow value);
	
	public void pushByteArray(byte[] value);
	
	public void pushShort(short value);
	
	public void pushInteger(int value);
	
	public void pushLong(long value);
	
	public void pushFloat(float value);
	
	public void pushDouble(double value);
	

}
