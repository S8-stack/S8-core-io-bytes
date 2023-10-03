package com.s8.io.bytes;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.s8.api.bytes.ByteOutflow;

/**
 * <code>ByteBuffer</code>-based <code>ByteInflow</code>
 *
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class BufferByteInflow extends BaseByteInflow {


	private int recordStartPosition;
	
	private ByteOutflow recorder;
	
	public BufferByteInflow(ByteBuffer buffer) {
		super();
		this.buffer = buffer;
	}


	@Override
	public boolean matches(byte[] sequence) throws IOException {
		int length = sequence.length;
		if(buffer.remaining()>=length) {
			byte[] bytes = new byte[length];
			buffer.get(bytes);
			for(int i=0; i<length; i++) {
				if(bytes[i]!=sequence[i]) {
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}



	@Override
	public byte[] getByteArray(int length) {
		byte[] bytes = new byte[length];
		buffer.get(bytes);
		return bytes;
	}





	@Override
	public long getCount() {
		return (long) buffer.position();
	}

	@Override
	public void startRecording(ByteOutflow outflow) {
		recorder = outflow;
		recordStartPosition = buffer.position();
	}

	@Override
	public void stopRecording() throws IOException {
		int position = buffer.position();
		int length = position - recordStartPosition;
		byte[] recordedBytes = new byte[length];
		
		// rewind
		buffer.position(recordStartPosition);
		buffer.get(recordedBytes, 0, length);
		
		// restore position
		buffer.position(position);
		
		// actually record
		recorder.putByteArray(recordedBytes);
		
		// unplug
		recorder = null;
	}


	@Override
	protected void allocate(int bytecount) throws IOException {
		// do nothing...
	}


}
