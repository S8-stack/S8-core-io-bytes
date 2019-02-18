package com.qx.reactive;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TODO /!\ THREAD SECURITY TO BE IMPLEMENTED
 * @author pc
 *
 */
public class SocketQxOutflow implements QxOutflow {

	public final static boolean IS_SAFETY_CHECKS_ENABLED = true;

	private AsynchronousSocketChannel channel;

	private AtomicBoolean isOutflowing;

	private final Object headLock = new Object();


	

	private int capacity;

	/**
	 * insert here
	 */
	private Node head;

	private Node tail;


	/**
	 * 
	 * @param capacity
	 */
	public SocketQxOutflow(int capacity) {
		super();
		this.capacity = capacity;
		this.isOutflowing = new AtomicBoolean(false);
		head = new Node();
		tail = new Node();
	}


	public void push(byte[] bytes) {
		OutputByteArray outputByteArray = new OutputByteArray(bytes);
		boolean isFullyPulled = outputByteArray.pull(tail.buffer);
		while(!isFullyPulled) {
			isFullyPulled = outputByteArray.pull(tail.buffer);
		}
	}

	public void push(long value) {
		ensure(8);
	}


	public void push(SocketQxOutflow outflow) {
		tail.next = outflow.head;
		tail.isOutflowable.set(true);
		tail = outflow.tail;
	}


	private void ensure(int nBytes) {
		if(IS_SAFETY_CHECKS_ENABLED && nBytes>capacity) {
			throw new RuntimeException("Cannto ensure more capacity");
		}

		/*
		 * OPTIMIZATION EXPLANATION: since the target capacity of this outflow is roughly
		 * 1024 and the target ensure nBytre is in the range [4, 8], we find it more simple
		 * to move to next buffer if there is not enough place, therefore we don't have to 
		 * build intermediary buffers. The only (super small) penalty is that we are writing 
		 * buffer of roughly 1024-[4...8] instead of pure 1024-length buffers. 
		 */
		if(tail.buffer.remaining()<nBytes) {
			Node next = new Node();
			tail.next = next;

			/* note that hasNext has to be set to true ONLY ATFER next field has been 
			 * defined to ensure thread safety
			 */
			tail.isOutflowable.set(true);
			tail = next;

			// try sending
			send();
		}
	}

	private void send() {

		synchronized (headLock) {

			if(!isOutflowing.get() // not already outflowing (remark: if already outflowing,
					// callback method will played send, so no pblm.
					&& head.isOutflowable.get()) { // has something to outflow
				
				Node node = head;
				head = node.next;
				channel.write(node.buffer, 1000, TimeUnit.SECONDS, null, new CompletionHandler<Integer, Void>() {

					@Override
					public void completed(Integer result, Void attachment) {
						isOutflowing.set(false); // done outflowing
						
						/*
						 * The next send call will show a thread synchronisation fight among:
						 * - this thread (called back by the underlying channel group threads pool)
						 * - the thread using the QxOutflow
						 */
						send();
					}

					@Override
					public void failed(Throwable exc, Void attachment) {
						// TODO Auto-generated method stub

					}
				});
			}
		}
	}


	@Override
	public void push(QxOutflow value) {
		this.tail
	}


	@Override
	public void push(short value) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void push(int value) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void push(float value) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void push(double value) {
		// TODO Auto-generated method stub
		
	}
}
