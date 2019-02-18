package com.qx.reactive;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Optimized for Vectored I/O
 * @author pc
 *
 */
public class ByteChain {

	private int defaultCapacity;
	
	private ByteBuffer buffer;
	
	
	private ByteChain next;

	
	private AtomicBoolean isNextAvailable;
	
	/**
	 * length since the start of the head buffer
	 */
	private int length;
	
	/**
	 * reference to head for insertion
	 */
	private ByteChain head;
	
	public ByteChain(int defaultCapacity) {
		super();
		this.defaultCapacity = defaultCapacity;
		buffer = ByteBuffer.allocate(defaultCapacity);
		isNextAvailable = new AtomicBoolean(false);
	}
	
	public ByteChain(int defaultCapacity, int specificCapacity) {
		super();
		this.defaultCapacity = defaultCapacity;
		buffer = ByteBuffer.allocate(specificCapacity);
	}
	
	
	/**
	 * 
	 * @return defualt capacity used for new Chain created
	 */
	public int getDefaultCapacity() {
		return defaultCapacity;
	}
	

	/**
	 * @return a flag indicating if the wrapped <code>ByteBuffer</code> can be used for 
	 * writing.
	 */
	public boolean isOutflowable() {
		return isOutflowable.get();
	}
	

	/**
	 * Mark this <code>QxBytesOutflow</code> are closed for writing operation, and release 
	 * access to the underlying <code>ByteBuffer</code>.
	 */
	public void setOutflowable() {
		isOutflowable.set(true);
	}
	
	/**
	 * Note that availability of the buffer of an <code>QxBytesOutflow</code> has 
	 * to be tested prior to using <code>getBuffer()</code> method by calling 
	 * <code>isOutflowable()</code> method on it.
	 * If not available, <code>getBuffer()</code> will return null.
	 * @return the underlying buffer for writing operation.
	 */
	public ByteBuffer getBuffer() {
		if(this.isOutflowable.get()) {
			return buffer;
		}
		else {
			return null;
		}
	}
	
	/**
	 * 
	 * @return a flag indicating if the next element is now available for access
	 */
	public boolean hasNext() {
		
	}
	
	
	public void append(ByteChain chain){
		next = chain.head;
		chain.head = head;
		chain.length+=length;
	}
	
	
	
	/**
	 * Note that 
	 * @return the next <code>QxBytesOutflow</code>.
	 */
	public ByteChain next() {
		if(isNextAvailable.get()) {
			return next;
		}
		else {
			return null;
		}
	}
	
	
	public ByteChain next(boolean isOutflowable) {
		
		/* prepare next node */
		ByteChain next = new ByteChain(defaultCapacity);
		
		/* append next node */
		this.next = next;
		
		/* set next as available. NOTE that the order is significant for thread safety */
		this.isNextAvailable.set(isOutflowable);
		
		/* set current as outflowable. NOTE that the order is significant for thread safety */
		this.isOutflowable.set(isOutflowable);
		
		return next;
	}
	
	public ByteChain appendNext() {
		ByteChain next = new ByteChain(defaultCapacity);
		this.next = next;
		return next;
	}
	
}
