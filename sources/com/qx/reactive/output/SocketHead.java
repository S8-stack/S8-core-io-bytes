package com.qx.reactive.output;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 
 * @author pc
 *
 */
public class SocketHead implements QxOutflowHead {

	private final Object lock;

	private AsynchronousSocketChannel channel;
	
	private AtomicBoolean isWritePending;
	
	private QxOutflow current;
	
	private long timeout;

	public SocketHead(AsynchronousSocketChannel channel, int capacity, long timeout) {
		super();
		this.channel = channel;
		this.timeout = timeout;
		lock = new Object();
		current = new QxOutflow(this, 0, capacity);
	}

	@Override
	public QxOutflow getCurrent() {
		return current;
	}
	
	@Override
	public void send() {
		synchronized (lock) {

			if(!isWritePending.get() // not already outflowing (remark: if already outflowing,
					// callback method will played send, so no pblm.
					&& current.isReleased()) { // has something to outflow
				
				
				channel.write(current.buffer, timeout, TimeUnit.SECONDS, null, 
						new CompletionHandler<Integer, Void>() {

					@Override
					public void completed(Integer result, Void attachment) {
						isWritePending.set(false); // done out-flowing
						
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
				
				current = current.next();
			}
		}
	}

	
}
