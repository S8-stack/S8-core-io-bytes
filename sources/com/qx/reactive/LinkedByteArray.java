package com.qx.reactive;


/**
 * -Very- simple structure that prevent re-copying bytes array again and again and again (for nothing).
 * Just traverse the ByteArrayChain with a simple:
 * <pre>{@code 
 * LinkedByteArray n;
 * while(n!=null){
 * // process here n.array
 * 	n = n.next;
 * }
 * </pre>
 * 
 * @author pc
 *
 */
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
