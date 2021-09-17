package com.s8.blocks.bytes.linked;

import static java.nio.file.StandardOpenOption.READ;

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
public abstract class LinkedBytesDiskReading {


	/**
	 * On success condition
	 */
	public abstract void onSucceed(LinkedBytes head);


	/**
	 * On failed condition
	 */
	public abstract void onFailed(IOException exception);


	/**
	 * max nb attempts
	 */
	private int maxNbAttempts = 64;


	/**
	 * path
	 */
	private final Path path;


	/**
	 * is verbose
	 */
	private final boolean isVerbose;



	/**
	 * 
	 */
	private FileChannel channel;


	/**
	 * 
	 * @param head
	 * @param path
	 * @param isVerbose
	 */
	public LinkedBytesDiskReading(Path path, boolean isVerbose) {
		super();
		this.path = path;
		this.isVerbose = isVerbose;
	}

	
	public void read() {

		try {
			// ensure directories are created
			Path folderpath = path.getParent();
			if(!Files.exists(folderpath)) {
				Files.createDirectories(folderpath);
			}

			// open channel
			channel = FileChannel.open(path, READ);
			
			LinkedBytes head = null, tail = null;

			// parse
			boolean hasNext = true;
			while(hasNext) {
				
				LinkedBytes chunk = parseHeader();
				readBuffer(chunk.wrap());
				hasNext = (chunk.attributes & 0x01) == 0x01;
				
				if(head != null) { 
					tail.next = chunk; tail = chunk;
				}
				else {
					head = chunk; tail = chunk;
				}
			}

			// follow-up
			onSucceed(head);
		} 
		catch (IOException e) {
			if(isVerbose) {
				e.printStackTrace();	
			}
			onFailed(e);
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



	



	private LinkedBytes parseHeader() throws IOException {

		ByteBuffer buffer = ByteBuffer.allocate(LinkedBytesFileFormat.HEADER_BYTECOUNT);
		readBuffer(buffer);
		buffer.flip();
		
		/* <matching tag> */
		byte[] tag = new byte[LinkedBytesFileFormat.HEADER_OPENING_TAG.length];
		buffer.get(tag);
		if(tag[0]!=LinkedBytesFileFormat.HEADER_OPENING_TAG[0] || tag[1]!=LinkedBytesFileFormat.HEADER_OPENING_TAG[1]) {
			throw new IOException("Failed to match HEADER_OPENING_TAG");
		}
		/* </matching tag> */
		
		short attributes = buffer.getShort();
		
		int length = buffer.getInt();
		
		/* <matching tag> */
		tag = new byte[LinkedBytesFileFormat.HEADER_CLOSING_TAG.length];
		buffer.get(tag);
		if(tag[0]!=LinkedBytesFileFormat.HEADER_CLOSING_TAG[0] || tag[1]!=LinkedBytesFileFormat.HEADER_CLOSING_TAG[1]) {
			throw new IOException("Failed to match HEADER_OPENING_TAG");
		}
		/* </matching tag> */
		
		LinkedBytes chunk = new LinkedBytes(length);
		chunk.attributes = attributes;
		
		return chunk;
	}


	/**
	 * 
	 */
	private void readBuffer(ByteBuffer buffer) throws IOException {
		int attemptCount = 0;

		// must at least flush half of the buffer
		while(buffer.hasRemaining() && attemptCount < maxNbAttempts) {
			channel.read(buffer);
			attemptCount++;
		}

		if(attemptCount==maxNbAttempts) {
			throw new IOException("Nb of retrieve attempts exceed limit");
		}
	}

}
