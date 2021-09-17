package com.s8.blocks.bytes.linked;

import java.nio.charset.StandardCharsets;

public class LinkedBytesFileFormat {
	
	

	public final static int HEADER_BYTECOUNT = 10;

	public final static byte[] HEADER_OPENING_TAG = "<:".getBytes(StandardCharsets.US_ASCII);

	public final static byte[] HEADER_CLOSING_TAG = "/>".getBytes(StandardCharsets.US_ASCII);

	
	
	public LinkedBytesFileFormat() {
		super();
	}


	/**
	 * 
	 * @param bytecount
	 * @return
	 */
	public static int segment(long bytecount) {

		// adjust capacity
		if(bytecount<2048) {
			return (int) bytecount;
		}
		else if(bytecount<65536) {
			// so capacity > 8192
			return (int) (bytecount/4);
		}
		else {
			return 8192;
		}
	}
	
}
