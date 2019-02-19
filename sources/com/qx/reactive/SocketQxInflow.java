package com.qx.reactive;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.qx.reactive.input.QxInflow;
import com.qx.reactive.input.QxInputReactive;

public abstract class SocketQxInflow<A> extends QxInflow {


	private long timeout;

	private TimeUnit timeoutTimeUnit;

	private AtomicBoolean isReceiving;

	private AsynchronousSocketChannel channel;

	private ByteBuffer buffer;

	private A attachment;

	public SocketQxInflow(
			AsynchronousSocketChannel channel, 
			A attachment, 
			int capacity, 
			QxInputReactive state) {
		super();
		this.channel = channel;
		this.attachment = attachment;
		this.buffer = ByteBuffer.allocate(capacity);
		isReceiving = new AtomicBoolean(false);
		
		// set state
		setState(state);
		
		// start the reception
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
					on(buffer);
					receive();
				}
				else {
					receive();
				}
			}

			@Override
			public void failed(Throwable exc, A attachment) {
				onFailed(exc, attachment);
			}
		});
	}
	
	public abstract void onFailed(Throwable exc, A attachment);

}
