package com.qx.reactive.input;

import java.nio.ByteBuffer;

import com.qx.reactive.InputByteArray;



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
public abstract class QxInflow {


	/**
	 * internal parsing state
	 */
	private QxInputReactive state;



	public QxInflow() {
		super();
	}


	public void setState(QxInputReactive state) {
		this.state = state;
	}
	
	
	public void on(ByteBuffer buffer) {
		while(buffer.hasRemaining() && state!=null) {
			state = state.on(buffer);
		}
	}

	
	
	

	public static abstract class PullByteArray implements QxInputReactive {

		private QxInputReactive next;
		
		private InputByteArray bytes;
		

		public PullByteArray(int length) {
			super();
			this.bytes = new InputByteArray(length);
		}

		
		public @Override QxInputReactive on(ByteBuffer buffer) {
			bytes.pull(buffer);
			if(bytes.isFilled()) {
				onFilled(bytes.getArray());
				return next;	
			}
			else {
				return this;
			}
		}
		
		public abstract void onFilled(byte[] bytes);

	}


	public static abstract class PullInteger extends PullByteArray {

		public PullInteger() {
			super(4);
		}

		public @Override void onFilled(byte[] bytes) {
			onPulled(ByteBuffer.wrap(bytes).getInt());
		}

		public abstract void onPulled(int value);
	}
	
	public static abstract class PullDouble extends PullByteArray {

		public PullDouble() {
			super(8);
		}

		public @Override void onFilled(byte[] bytes) {
			onPulled(ByteBuffer.wrap(bytes).getDouble());
		}

		public abstract void onPulled(double value);
	}
	

}
