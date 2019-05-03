package com.qx.reactive.input;

import java.nio.ByteBuffer;

import com.qx.reactive.InputByteArray;
import com.qx.reactive.OutputByteArray;
import com.qx.reactive.QxIOReactive;



/**
 * <h1>QxInflow</h1>
 * <p>
 * QxInflow is basically a dual flow with:
 * <ul>
 * <li>Flow of input data through <code>ByteBuffer</code></li>
 * <li>Flow of readers parsing the incoming data through the 
 * <code>QxInflowReactive -> on(ByteBuffer input) </code> chaining</li>
 * </ul>
 * </p>
 * @author pc
 *
 */
public abstract class QxIOFlow implements QxIOReactive {


	/**
	 * internal parsing state
	 */
	private QxIOReactive state;



	public QxIOFlow() {
		super();
	}


	public void setState(QxIOReactive state) {
		this.state = state;
	}


	@Override
	public NextMove on(ByteBuffer buffer) {

		/*
		 * if buffer has no remaining bytes, call FEED to get at least a meaningful
		 * nextMove.
		 */
		NextMove move = NextMove.FEED;

		while(buffer.hasRemaining()) {
			move = state.on(buffer);
			switch(move) {
			case ERROR : 
				return NextMove.ERROR;
				
			case STOP : 
				return NextMove.STOP;
			default: /* continue */ break;
			}
		}
		return move;
	}



	public interface PushCallback {
		public abstract NextMove onPushed();
	}

	public interface PullByteCallback {
		public abstract NextMove onPulled(byte b);
	}

	private static class PullByteState implements QxIOReactive {

		private PullByteCallback callback;

		public PullByteState(PullByteCallback callback) {
			super();
			this.callback = callback;
		}

		@Override 
		public QxIOReactive.NextMove on(ByteBuffer buffer) {
			if(buffer.hasRemaining()) {
				return callback.onPulled(buffer.get());
			}
			else {
				return NextMove.FEED;	
			}
		}		
	}

	private static class PushByteState implements QxIOReactive {

		private byte b;
		private PushCallback callback;

		public PushByteState(byte b, PushCallback callback) {
			super();
			this.b = b;
			this.callback = callback;
		}

		@Override 
		public QxIOReactive.NextMove on(ByteBuffer buffer) {
			if(buffer.hasRemaining()) {
				buffer.put(b);
				return callback.onPushed();
			}
			else {
				return NextMove.FEED;	
			}
		}		
	}

	public NextMove pullByte(PullByteCallback callback) {
		setState(new PullByteState(callback));
		return NextMove.FEED; // need to feed to get buffer data
	}


	public NextMove pushByte(byte b, PushCallback callback) {
		setState(new PushByteState(b, callback));
		return NextMove.FEED; // need to feed to get buffer data
	}


	public interface PullByteArrayCallback {
		public abstract NextMove onPulled(byte[] bytes);
	}

	private static class PullByteArrayState implements QxIOReactive {

		private InputByteArray bytes;
		private PullByteArrayCallback callback;

		public PullByteArrayState(int length, PullByteArrayCallback callback) {
			super();
			this.bytes = new InputByteArray(length);
			this.callback = callback;
		}

		@Override 
		public QxIOReactive.NextMove on(ByteBuffer buffer) {
			bytes.pull(buffer);
			if(bytes.isFilled()) {
				return callback.onPulled(bytes.getBytes());
			}
			else {
				return NextMove.FEED;	
			}
		}		
	}
	
	private static class PushByteArrayState implements QxIOReactive {

		private OutputByteArray bytes;
		private PushCallback callback;

		public PushByteArrayState(byte[] array, PushCallback callback) {
			super();
			this.bytes = new OutputByteArray(array);
			this.callback = callback;
		}

		@Override 
		public QxIOReactive.NextMove on(ByteBuffer buffer) {
			if(bytes.push(buffer)) {
				return callback.onPushed();
			}
			else {
				return NextMove.FEED;	
			}
		}		
	}

	public NextMove pullByteArray(int length, PullByteArrayCallback callback) {
		setState(new PullByteArrayState(length, callback));
		return NextMove.FEED;
	}
	
	public NextMove pushByteArray(byte[] array, PushCallback callback) {
		setState(new PushByteArrayState(array, callback));
		return NextMove.FEED;
	}


	public interface PullIntegerCallback {
		public abstract NextMove onPulled(int value);
	}

	public void pullInteger(PullIntegerCallback callback) {
		pullByteArray(4, bytes -> callback.onPulled(ByteBuffer.wrap(bytes).getInt()));
	}

	public interface PullDoubleCallback {
		public abstract NextMove onPulled(double value);
	}

	public void pullInteger(PullDoubleCallback callback) {
		pullByteArray(8, bytes -> callback.onPulled(ByteBuffer.wrap(bytes).getDouble()));
	}

}
