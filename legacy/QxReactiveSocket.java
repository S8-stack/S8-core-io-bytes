package com.qx.reactive;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;

import com.qx.reactive.input.QxIOFlow;


/**
 * This class is intended for scenario presentation rather than actual use in prod.
 * 
 * @author pc
 *
 */
public abstract class QxReactiveSocket {

	private AsynchronousSocketChannel channel;

	private long timeout;

	private QxIOFlow state;

	private ByteBuffer inputBuffer;

	public QxReactiveSocket(
			AsynchronousSocketChannel channel, 
			int bufferLength,
			long timeout,
			QxIOFlow start) {
		super();
		this.channel = channel;
		this.timeout = timeout;
		this.state = start;
		inputBuffer = ByteBuffer.allocate(bufferLength);
		
		listen();
	}

	private void listen() {

		/*
		 * Every time we start a new reading, buffer is cleared. Buffer capacity must not be 
		 * seen as the target number of bytes to be read, but rather like the maximum amount 
		 * that can be read. QxReactiveSocket performs "equally" fast if a lot a small (ie, of
		 * limit well below bufferLength) are submitted, rather than big ones.
		 */
		inputBuffer.clear();

		channel.read(inputBuffer, timeout, TimeUnit.SECONDS, null, new CompletionHandler<Integer, Void>() {

			@Override
			public void completed(Integer result, Void attachment) {

				// test for end of stream reached
				if(result==-1) {
					close();
				}

				// flip buffer to prepare it for reading
				inputBuffer.flip();
				
				// consume buffer data
				if(!onInput()){
					
					// if not terminated, then one more time...
					listen();
				}
			}

			@Override
			public void failed(Throwable exc, Void attachment) {
				close();
			}
		});


	}

	private void close() {
		try {
			channel.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		onClosed();
	}


	private boolean onInput() {
		boolean isTerminated = false;
		while(!isTerminated && inputBuffer.hasRemaining()) {
			isTerminated = state.on(inputBuffer);
		}
		return isTerminated;
	}

	/**
	 * callback method for further action once the underlying channel has been closed
	 */
	public abstract void onClosed();
}
