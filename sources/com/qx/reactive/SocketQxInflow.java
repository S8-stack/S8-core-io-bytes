package com.qx.reactive;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SocketQxInflow<A> {


	private long timeout;

	private TimeUnit timeoutTimeUnit;

	private AtomicBoolean isReceiving;

	private AsynchronousSocketChannel channel;

	private ByteBuffer buffer;

	private QxInflow state;

	private Object lock;

	private A attachment;

	public SocketQxInflow(AsynchronousSocketChannel channel, A attachment, int capacity, QxInflow state) {
		super();
		this.channel = channel;
		this.attachment = attachment;
		lock = new Object();
		this.buffer = ByteBuffer.allocate(capacity);
		isReceiving = new AtomicBoolean(false);
		this.state = state;
		receive();
	}

	private void receive() {
		isReceiving.set(true);
		channel.read(buffer, timeout, timeoutTimeUnit, attachment, new CompletionHandler<Integer, A>() {

			@Override
			public void completed(Integer result, A attachment) {
				
				if(result==-1) {
					// TODO close channel
				}
				else if(result>0) {
					buffer.flip();	
					state.on(buffer);
					
				}
				
			}

			@Override
			public void failed(Throwable exc, A attachment) {
				// TODO Auto-generated method stub

			}
		});
	}

}
