package com.qx.reactive;



public class LinkedByteArray {

	public byte[] array;
	
	public LinkedByteArray next;
	
	public LinkedByteArray(int length) {
		super();
		this.array = new byte[length];
	}
	
	public LinkedByteArray(byte[] array) {
		super();
		this.array = array;
	}
	
}
