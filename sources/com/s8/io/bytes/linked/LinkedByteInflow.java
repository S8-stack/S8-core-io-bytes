package com.s8.io.bytes.linked;

import java.io.IOException;

import com.s8.api.bytes.ByteOutflow;
import com.s8.io.bytes.AutoByteInflow;



/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class LinkedByteInflow extends AutoByteInflow {

	
	/** first node of the linked chain */
	private LinkedBytes head;

	
	/** bytecount */
	private long exhaustedChunksBytecount;

	
	/**
	 * 
	 * @param capacity
	 */
	public LinkedByteInflow(LinkedBytes head) {
		super();
		this.head = head;
		buffer = head.wrap();
		exhaustedChunksBytecount = 0;
	}
	
	
	@Override
	public void pull() {
		exhaustedChunksBytecount+=head.length;
		head = head.next;
		buffer = head.wrap();
	}

	@Override
	public void allocate(int bytecount) throws IOException {
		
		/* normal case */
		if(buffer.remaining() < bytecount) {
			if(buffer.remaining()>0) {
				throw new IOException("Scrapped end of buffer");
			}
			pull();
		}
	}
	

	/**
	 * 
	 * @return
	 */
	public LinkedBytes getHead() {
		return head;
	}

	


	@Override
	public long getCount() {
		return exhaustedChunksBytecount+buffer.position();
	}



	@Override
	public void startRecording(ByteOutflow outflow) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void stopRecording() throws IOException {
		// TODO Auto-generated method stub
		
	}


}
