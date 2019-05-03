package com.qx.reactive.output;

import java.nio.ByteBuffer;
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

	private LinkedByteBuffer current;

	private long timeout;

	public SocketHead(AsynchronousSocketChannel channel, long timeout) {
		super();
		this.channel = channel;
		this.timeout = timeout;
		lock = new Object();
	}


	@Override
	public LinkedByteBuffer initialize(int capacity) {
		current = new LinkedByteBuffer(this, 0, capacity);
		return current;
	}


	private class Handler implements CompletionHandler<Integer, Void> {

		private ByteBuffer buffer;
		/**
		 * 
		 * @param length: payload length
		 */
		public Handler(ByteBuffer buffer) {
			super();
			this.buffer = buffer;
		}
		
		@Override
		public void completed(Integer result, Void attachment) {
			if(result==-1) {
				close();
			}
			else {
				if(!buffer.hasRemaining()) {

					isWritePending.set(false); // done out-flowing
					
					/*
					 * The next send call will show a thread synchronisation fight among:
					 * - this thread (called back by the underlying channel group threads pool)
					 * - the thread using the QxOutflow
					 */
					send();
				}
				else {
					write(this);
				}
			}
		}


		@Override
		public void failed(Throwable exc, Void attachment) {
			close();
		}

	};

	@Override
	public void send() {
		synchronized (lock) {

			if(!isWritePending.get() // not already outflowing (remark: if already outflowing,
					// callback method will played send, so no pblm.
					&& current.isReleased()) { // has something to outflow

				write(new Handler(current.buffer));
				current = current.append();
			}
		}
	}


	private void write(Handler handler) {
		channel.write(handler.buffer, timeout, TimeUnit.SECONDS, null, handler);
	}
	
	
	private void close() {
		// TODO
	}


}
