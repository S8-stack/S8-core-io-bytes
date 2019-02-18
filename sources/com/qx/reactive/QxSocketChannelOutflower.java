package com.qx.reactive;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;

public class QxSocketChannelOutflower {
	
	private AsynchronousSocketChannel channel;
	
	private QxOutflow outflow;
	
	private long timeout;
	
	public QxSocketChannelOutflower(AsynchronousSocketChannel channel, QxOutflow outflow, long timeout) {
		this.channel = channel;
		this.outflow = outflow;
		this.timeout = timeout;
	}
	
	
	/**
	 * 
	 */
	public void start() {
		ByteBuffer buffer = outflow.pull();
		if(buffer!=null) {
			channel.write(buffer, timeout, TimeUnit.SECONDS, null, new CompletionHandler<Integer, Void>() {

				@Override
				public void completed(Integer result, Void attachment) {
					start();
				}

				@Override
				public void failed(Throwable exc, Void attachment) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	
}
