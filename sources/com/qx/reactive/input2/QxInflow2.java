package com.qx.reactive.input2;

import java.nio.ByteBuffer;

import com.qx.reactive.InputByteArray;

public class QxInflow2 {

	
	public abstract class State {
		
		public State next;
		
		public abstract boolean on(ByteBuffer buffer);
		
	}

	public interface OnInteger {

		public void consume(int value);
	}

	
	public class IntegerParsing extends State {
		
		private OnInteger callback;

		private InputByteArray array;
		
		public IntegerParsing(OnInteger callback) {
			super();
			this.callback = callback;
			this.array = new InputByteArray(4);
		}
		
		@Override
		public boolean on(ByteBuffer buffer) {
			boolean isFilled = array.pull(buffer);
			if(isFilled) {
				int value = ByteBuffer.wrap(array.getBytes()).getInt();
				callback.consume(value);
			}
			return false;
		}
	}
	
	
	private State state;
	
	public void pullInteger(OnInteger callback) {
		State next = new IntegerParsing(callback);
		if(state==null) {
			state = next;
		}
		else {
			state.next = null;
		}
	}
	
	
}

