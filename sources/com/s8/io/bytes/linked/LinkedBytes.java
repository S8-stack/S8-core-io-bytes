package com.s8.io.bytes.linked;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * -Very- simple structure that prevent re-copying bytes array again and again for nothing.
 * Just traverse the ByteArrayChain with a simple:
 * <pre>{@code 
 * Bytes chunk;
 * while(chunk!=null){
 * // process here chunk.bytes
 * 	chunk = chunk.next;
 * }
 * </pre>
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class LinkedBytes {

	
	/**
	 * attributes
	 */
	public short attributes;
	

	/**
	 * the underlying bytes array of this link
	 */
	public byte[] bytes;


	/**
	 * Filled part bytes start at <code>offset</code>
	 */
	public int offset;

	/**
	 * Filled part of bytes is <code>length</code> long.
	 */
	public int length;

	/**
	 * 
	 */
	public LinkedBytes next;

	public LinkedBytes(int capacity) {
		super();
		this.bytes = new byte[capacity];
		this.offset = 0;
		this.length = capacity;
	}

	public LinkedBytes(byte[] bytes) {
		super();
		this.bytes = bytes;
		this.offset = 0;
		this.length = bytes.length;
	}

	public LinkedBytes(byte[] bytes, int offset, int length) {
		super();
		this.bytes = bytes;
		this.offset = offset;
		this.length = length;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public ByteBuffer wrap() {
		return ByteBuffer.wrap(bytes, offset, length);
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
		LinkedBytes fragment = this;
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



	/**
	 * Flatten to byte Array
	 * 
	 * @return the byte array
	 */
	public byte[] toByteArray() {
		LinkedBytes fragment = this;

		int length=0;
		while(fragment!=null) {
			length+=fragment.length;
			fragment=fragment.next;
		}

		byte[] array = new byte[length];
		int index=0;
		fragment = this;
		while(fragment!=null) {
			byte[] fragmentBytes = fragment.bytes;
			int i0 = fragment.offset, i1= fragment.offset+fragment.length;
			for(int i=i0; i<i1; i++) {
				array[index++] = fragmentBytes[i];
			}
		}
		return array;
	}


	public LinkedBytes recut(int fragmentLength) {
		LinkedBytes chain1 = this;
		LinkedBytes chain2 = new LinkedBytes(new byte[fragmentLength], 0, 0);
		LinkedBytes head = chain2;

		int i2=0, i1 = chain1.offset;
		int nWritableBytes2 = fragmentLength, nReadableBytes1 = chain1.length;

		int nTransferredBytes;
		byte[] bytes2 = chain2.bytes, bytes1 = chain1.bytes;

		boolean isNextRequired2 = false;
		boolean isNextRequired1 = false;
		while(chain1!=null) {
			
			// chain 1 is limiting
			if(nWritableBytes2>nReadableBytes1) {
				isNextRequired2 = false;
				isNextRequired1 = true;
				nTransferredBytes = nReadableBytes1;
			}
			
			// chain 2 is limiting
			else if(nWritableBytes2<nReadableBytes1) {
				isNextRequired2 = true;
				isNextRequired1 = false;
				nTransferredBytes = nWritableBytes2;
			}
			// both chains are limiting
			else { // if(n0 == n1)
				isNextRequired2 = true;
				isNextRequired1 = true;
				nTransferredBytes = nWritableBytes2;
			}

			for(int i=0; i<nTransferredBytes; i++) {
				bytes2[i2++] = bytes1[i1++]; 
			}
			chain2.length+=nTransferredBytes;

			// move to next fragment on chain 2
			if(isNextRequired2) {
				chain2.next = new LinkedBytes(new byte[fragmentLength], 0, 0);
				chain2 = chain2.next;
				i2 = 0; // no offset
				nWritableBytes2 = fragmentLength;
				bytes2 = chain2.bytes;
			}
			else {
				nWritableBytes2-=nTransferredBytes;	
			}

			// move to next fragment on chain 1
			if(isNextRequired1) {
				chain1 = chain1.next;
				if(chain1!=null) {
					i1 = chain1.offset;
					nReadableBytes1 = chain1.length;
					bytes1 = chain1.bytes;	
				}
			}
			else {
				nReadableBytes1-=nTransferredBytes;
			}
		}
		
		return head;
	}
	
	
	/**
	 * retrieve tail of this chain
	 * @return
	 */
	public LinkedBytes tail() {
		LinkedBytes tail = this;
		while(tail.next!=null) {
			tail = tail.next;
		}
		return tail;
	}
	
	
	/**
	 * Append chain to this chain and return the tail chain link
	 * @param chain
	 * @return
	 */
	public LinkedBytes append(LinkedBytes chain) {
		LinkedBytes tail = tail();
		tail.next = chain;
		return tail.tail();
	}
	

	
	/**
	 * 
	 * @return
	 */
	public long getBytecount() {
		LinkedBytes link = this;
		long bytecount = 0;
		while(link!=null) {
			bytecount+=link.length;
			link = link.next;
		}
		return bytecount;
	}
	
	
	
	/**
	 * deep copy
	 * @return
	 */
	public LinkedBytes copy() {
		LinkedBytes chain1 = this;
		LinkedBytes head2 = null, chain2 = null, next2;
		while(chain1!=null) {
			next2 = new LinkedBytes(chain1.bytes, chain1.offset, chain1.length);
			if(chain2!=null) {
				chain2.next = next2;
				chain2 = next2;
			}
			else {
				chain2 = next2;
				head2 = chain2;
			}			
			chain1 = chain1.next;
		}
		return head2;
	}
	
	
	/**
	 * read this chain as an 
	 * @return
	 */
	public String toString_UTF8() {
		return new String(bytes, offset, length, StandardCharsets.UTF_8);
	}
	
	/**
	 * read this chain as an 
	 * @return
	 */
	public String unrollToString_UTF8() {
		StringBuilder builder = new StringBuilder();
		LinkedBytes chain = this;
		while(chain!=null) {
			builder.append(chain.toString_UTF8());
			chain = chain.next;
		}
		return builder.toString();
	}
	
	
	public static LinkedBytes fromString_UTF8(String str) {
		byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
		return new LinkedBytes(bytes);
	}
}
