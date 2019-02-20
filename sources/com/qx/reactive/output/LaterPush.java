package com.qx.reactive.output;

public class LaterPush {
	
	
	private LinkedByteBuffer node;

	private int position;
	
	private int length;
	
	public LaterPush(LinkedByteBuffer node, int position, int length) {
		super();
		this.node = node;
		this.position = position;
		this.length = length;
	}

	
}
