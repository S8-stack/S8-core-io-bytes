package com.s8.blocks.bytes.linked;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 
 * @author pierreconvert
 *
 */
public abstract class LinkedBytesDiskWriting {

	

	/**
	 * max nb attempts
	 */
	public final static int MAX_NB_ATTEMPTS = 256;
	


	/**
	 * head
	 */
	private final LinkedBytes head;
	

	/**
	 * path
	 */
	private final Path path;


	/**
	 * is verbose
	 */
	private final boolean isVerbose;

	
	private FileChannel channel = null;

	
	
	/**
	 * 
	 * @param head
	 * @param path
	 * @param isVerbose
	 * @throws IOException 
	 */
	public LinkedBytesDiskWriting(LinkedBytes head, Path path, boolean isVerbose) {
		super();
		this.head = head;
		this.path = path;
		this.isVerbose = isVerbose;
	}


	public void write() {
		
		try {
			// ensure directories are created
			Path folderpath = path.getParent();
			if(!Files.exists(folderpath)) {
				Files.createDirectories(folderpath);
			}
			channel = FileChannel.open(path, CREATE, WRITE).truncate(0);

			
			
			LinkedBytes chunk = head;
			boolean isSending = true;

			// is sending
			while(isSending && chunk != null) {
				ByteBuffer buffer = chunk.wrap();
				
				// write header
				composeHeader(chunk);
				
				// write body
				writeBuffer(buffer);

				// move to next chunk
				chunk = chunk.next;
			}


			if(isSending) {
				onSucceed();
			}
			else {
				onFailed();
			}

		} catch (IOException e) {
			if(isVerbose) {
				e.printStackTrace();	
			}
			onFailed();
		}
		finally {
			if(channel!=null) {
				try {
					channel.close();
				} 
				catch (IOException e) {
					if(isVerbose) {
						e.printStackTrace();	
					}
				}
			}
		}
	}

	
	
	
	/**
	 * 
	 * @param head
	 * @return
	 * @throws IOException
	 */
	public void composeHeader(LinkedBytes chunk) throws IOException {

		short flags = 0x00;
		if(chunk.next!=null) {
			flags |= 0x01;
		}
		
		ByteBuffer header = ByteBuffer.allocate(LinkedBytesFileFormat.HEADER_BYTECOUNT);
		header.put(LinkedBytesFileFormat.HEADER_OPENING_TAG);
		header.putShort(flags);
		header.putInt(chunk.length);
		header.put(LinkedBytesFileFormat.HEADER_CLOSING_TAG);
		
		header.flip();
		
		writeBuffer(header);
	}
	
	private void writeBuffer(ByteBuffer buffer) throws IOException {
		
		int iAttempt = 0;

		// must at least flush half of the buffer
		while(buffer.hasRemaining() && iAttempt < MAX_NB_ATTEMPTS) {
			channel.write(buffer);
			iAttempt++;
		}

		// nRetries
		 if(iAttempt == MAX_NB_ATTEMPTS) {
			 throw new IOException("Max write attempts exceeded");
		 }
	}


	/**
	 * On success condition
	 */
	public abstract void onSucceed();


	/**
	 * On failed condition
	 */
	public abstract void onFailed();

}
