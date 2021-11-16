package com.s8.blocks.helium.linked;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 
 * @author pierreconvert
 *
 */
public class LinkedBytesIO {
	
	
	public static LinkedBytes read(Path path, boolean isVerbose) throws IOException {
		return new LinkedBytesIO(path, isVerbose).read();
	}
	
	
	public static void write(LinkedBytes head, Path path, boolean isVerbose) throws IOException {
		new LinkedBytesIO(path, isVerbose).write(head);
	}
	
	

	public final static int HEADER_BYTECOUNT = 10;

	public final static byte[] HEADER_OPENING_TAG = "<:".getBytes(StandardCharsets.US_ASCII);

	public final static byte[] HEADER_CLOSING_TAG = "/>".getBytes(StandardCharsets.US_ASCII);


	/**
	 * max nb attempts
	 */
	public final static int MAX_NB_ATTEMPTS = 256;
	

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
	private LinkedBytesIO(Path path, boolean isVerbose) {
		super();
		this.path = path;
		this.isVerbose = isVerbose;
	}

	
	
	
	
	

	public LinkedBytes read() throws IOException {

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
			return head;
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

		ByteBuffer buffer = ByteBuffer.allocate(HEADER_BYTECOUNT);
		readBuffer(buffer);
		buffer.flip();
		
		/* <matching tag> */
		byte[] tag = new byte[HEADER_OPENING_TAG.length];
		buffer.get(tag);
		if(tag[0]!=HEADER_OPENING_TAG[0] || tag[1]!=HEADER_OPENING_TAG[1]) {
			throw new IOException("Failed to match HEADER_OPENING_TAG");
		}
		/* </matching tag> */
		
		short attributes = buffer.getShort();
		
		int length = buffer.getInt();
		
		/* <matching tag> */
		tag = new byte[HEADER_CLOSING_TAG.length];
		buffer.get(tag);
		if(tag[0]!=HEADER_CLOSING_TAG[0] || tag[1]!=HEADER_CLOSING_TAG[1]) {
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
		
		// attempts count
		int c = 0;

		// must at least flush half of the buffer
		while(buffer.hasRemaining() && c < MAX_NB_ATTEMPTS) {
			channel.read(buffer);
			c++;
		}

		if(c == MAX_NB_ATTEMPTS) {
			throw new IOException("Nb of retrieve attempts exceed limit");
		}
	}
	

	
	/**
	 * 
	 * @return
	 * @throws IOException 
	 */
	public void write(LinkedBytes head) throws IOException {
		
		try {
			// ensure directories are created
			Path folderpath = path.getParent();
			if(!Files.exists(folderpath)) {
				Files.createDirectories(folderpath);
			}
			channel = FileChannel.open(path, CREATE, WRITE);
			
			if(isVerbose) {
				long bytecount = head.getBytecount();
				System.out.println("[LinkedBytesIO] bytecount to be written: "+bytecount);
			}

			LinkedBytes chunk = head;
			// is sending
			while(chunk != null) {
				ByteBuffer buffer = chunk.wrap();
				
				// write header
				pushHeader(chunk);
				
				// write body
				writeBuffer(buffer);

				// move to next chunk
				chunk = chunk.next;
			}
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
	private void pushHeader(LinkedBytes chunk) throws IOException {

		short flags = 0x00;
		if(chunk.next!=null) {
			flags |= 0x01;
		}
		
		ByteBuffer header = ByteBuffer.allocate(HEADER_BYTECOUNT);
		header.put(HEADER_OPENING_TAG);
		header.putShort(flags);
		header.putInt(chunk.length);
		header.put(HEADER_CLOSING_TAG);
		
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


}
