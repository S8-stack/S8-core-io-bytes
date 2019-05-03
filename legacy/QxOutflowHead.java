package com.qx.reactive.output;

public interface QxOutflowHead {
	
	public abstract void send();
	
	public abstract LinkedByteBuffer initialize(int capacity);
	
}
