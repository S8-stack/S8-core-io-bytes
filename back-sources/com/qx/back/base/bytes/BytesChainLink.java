package com.qx.back.base.bytes;

import java.nio.ByteBuffer;

/**
 * -Very- simple structure that prevent re-copying bytes array again and again for nothing.
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
public class BytesChainLink {


	public byte[] bytes;

	public int offset;

	public int length;

	public BytesChainLink next;

	public BytesChainLink(int capacity) {
		super();
		this.bytes = new byte[capacity];
		this.offset = 0;
		this.length = bytes.length;
	}
	
	public BytesChainLink(byte[] bytes) {
		super();
		this.bytes = bytes;
		this.offset = 0;
		this.length = bytes.length;
	}

	public BytesChainLink(byte[] bytes, int offset, int length) {
		super();
		this.bytes = bytes;
		this.offset = offset;
		this.length = length;
	}


	/**
	 * Useful utility function to flatten a chain-linked list of HTTP2_Fragment into
	 * a single ByteBuffer. Note that most of the time (99.99%), there is only one
	 * fragment so operation is trivial. That's why the reminaing part (0.01%) is
	 * not optimized.
	 * 
	 * @return a flattened ByteInflow from this fragment as a head
	 */
	public ByteBuffer flatten() {
		BytesChainLink fragment = this;
		if(fragment.next==null) {
			return ByteBuffer.wrap(fragment.bytes, fragment.offset, fragment.length);
		}
		else {
			int length=0;
			while(fragment!=null) {
				length+=fragment.length;
				fragment=fragment.next;
			}

			ByteBuffer buffer = ByteBuffer.allocate(length);
			fragment = this;
			while(fragment!=null) {
				buffer.put(fragment.bytes, fragment.offset, fragment.length);
				fragment=fragment.next;
			}
			return buffer;
		}
	}
}
