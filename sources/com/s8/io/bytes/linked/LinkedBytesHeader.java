package com.s8.io.bytes.linked;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;


/**
 * Only for I/O parsing 
 * 
 * @author pierreconvert
 *
 */
class LinkedBytesHeader {
	


	public final static int HEADER_BYTECOUNT = 10;

	public final static byte[] HEADER_OPENING_TAG = "<:".getBytes(StandardCharsets.US_ASCII);

	public final static byte[] HEADER_CLOSING_TAG = "/>".getBytes(StandardCharsets.US_ASCII);


	
	
	private final boolean hasNext;

	private final int length;
	
	
	

	public LinkedBytesHeader(boolean hasNext, int length) {
		super();
		this.hasNext = hasNext;
		this.length = length;
	}



	/**
	 * 
	 * @param channel
	 * @return
	 * @throws IOException
	 */
	public static LinkedBytesHeader parse(FileChannel channel) throws IOException {

		ByteBuffer buffer = ByteBuffer.allocate(HEADER_BYTECOUNT);
		LinkedBytesIO.readBuffer(channel, buffer);
		buffer.flip();
		
		/* <matching tag> */
		byte[] tag = new byte[HEADER_OPENING_TAG.length];
		buffer.get(tag);
		if(tag[0]!=HEADER_OPENING_TAG[0] || tag[1]!=HEADER_OPENING_TAG[1]) {
			throw new IOException("Failed to match HEADER_OPENING_TAG");
		}
		/* </matching tag> */
		
		short attributes = buffer.getShort();
		boolean hasNext = ((attributes & 0x01) == 0x01);
		
		int length = buffer.getInt();
		
		/* <matching tag> */
		tag = new byte[HEADER_CLOSING_TAG.length];
		buffer.get(tag);
		if(tag[0]!=HEADER_CLOSING_TAG[0] || tag[1]!=HEADER_CLOSING_TAG[1]) {
			throw new IOException("Failed to match HEADER_OPENING_TAG");
		}
		/* </matching tag> */
		
		return new LinkedBytesHeader(hasNext, length);
	}
	
	
	
	public int getLength() {
		return length;
	}
	
	
	public boolean hasNext() {
		return hasNext;
	}
	
	

	
	/**
	 * 
	 * @param head
	 * @return
	 * @throws IOException
	 */
	public void compose(FileChannel channel) throws IOException {

		short flags = 0x00;
		if(hasNext) { flags |= 0x01; }
		
		ByteBuffer header = ByteBuffer.allocate(HEADER_BYTECOUNT);
		header.put(HEADER_OPENING_TAG);
		header.putShort(flags);
		header.putInt(length);
		header.put(HEADER_CLOSING_TAG);
		
		header.flip();
		
		LinkedBytesIO.writeBuffer(channel, header);
	}
	

	
}
