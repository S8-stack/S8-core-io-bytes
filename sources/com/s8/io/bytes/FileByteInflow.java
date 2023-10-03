package com.s8.io.bytes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.s8.api.bytes.ByteOutflow;



/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class FileByteInflow extends AutoByteInflow {

	private FileChannel channel;
	
	private ByteOutflow recorder = null;
	
	private long count;

	private boolean isEndOfFileReached;

	private int recordStartPosition;

	public FileByteInflow(FileChannel channel, int bufferingSize) throws IOException {
		super();
		this.channel = channel;
		buffer = ByteBuffer.allocate(bufferingSize);
		count = 0;
	}


	private final static int N_READ_RETRY = 8;


	
	@Override
	public void allocate(int nBytes) throws IOException {
		if(nBytes>buffer.remaining()) {
			
			// prior to compact, note the position since this the new offset
			count += buffer.position();
			
			// record before compacting
			if(recorder!=null) {
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
				
				recordStartPosition = 0;
			}
			
			// then compact
			buffer.compact();
			
			int retryIndex = 0;
			while(nBytes>buffer.position() && retryIndex<N_READ_RETRY && !isEndOfFileReached) {
				int nBytesRead = channel.read(buffer);
				if(nBytesRead==-1) {
					isEndOfFileReached = true;
				}
				retryIndex++;
			}
			if(nBytes>buffer.position()) {
				throw new IOException("Failed to read from channel");
			}
			buffer.flip();
		}
	}


	public final static int N_RETRIEVE_TRYOUTS = 64;

	
	/**
	 * Note that buffer must have been entirely read at this point, so we can clean 
	 * @throws IOException
	 */
	public void pull() throws IOException {
		buffer.clear();
		
		int nBytesRead = channel.read(buffer);
		if(nBytesRead==-1) {
			isEndOfFileReached = true;
		}
		buffer.flip();
	}



	@Override
	public long getCount() {
		return count;
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
}
