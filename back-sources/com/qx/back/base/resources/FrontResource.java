package com.qx.back.base.resources;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import com.qx.back.base.bytes.BytesChainLink;
import com.qx.front.base.BaseFront;

public class FrontResource {
	
	public final static boolean DEBUG_FORCE_RELOADING = true;

	public final static Class<?> ROOT = BaseFront.class;

	public static enum Status {
		OK, NOT_FOUND, LOAD_FAILED;
	}


	private Status status;

	private long fileSize;

	/**
	 * pathanme, acts as id
	 */
	private String pathname;

	private Path path;
	
	private boolean isCaching;

	private MIME_Type MIME_type;

	private long timestamp;

	private boolean isLoaded;

	/**
	 * payload
	 */
	public BytesChainLink head = null;

	private Object lock;

	private int fragmentLength;

	/**
	 * 
	 * @param pathname : relative pathname String
	 * @param path : absolute pathname to the resource
	 * @param fragmentLength : typical fragment length
	 */
	public FrontResource(Path path, String pathname, int fragmentLength, boolean isCaching){
		
		// path
		this.path = path;
		
		// pathname
		this.pathname = pathname;
		
		// caching
		this.isCaching = isCaching;
		
		this.fragmentLength = fragmentLength;
		this.MIME_type = MIME_Type.match(pathname);
		isLoaded = false;
		lock = new Object();
	}
	
	
	public void setFragmentBaseLength(int length) {
		this.fragmentLength = length;
	}
	
	public String getPathname() {
		return pathname;
	}

	private void ensureLoaded() {
		// avoid calling synchronized method is already loaded
		if(!isLoaded) { 
			// sync block
			synchronized(lock) {
				// prevent awaken sync block to re-sync
				if(!isLoaded) {
					if(!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
						status = Status.NOT_FOUND;
					}
					else {
						try {
							fileSize = 0;
							BytesChainLink block = createFragment(), nextBlock;

							InputStream inputStream = Files.newInputStream(path);

							head = block;
							boolean isDone = false;
							int nBytes;
							while(!isDone){

								nBytes = inputStream.read(block.bytes, block.length, block.bytes.length-block.length);

								// end of stream reached
								if(nBytes==-1){ 
									isDone = true;
								}
								else {
									fileSize+=nBytes;
									block.length+=nBytes;

									if(block.offset==block.length){ // full loading of block
										nextBlock = createFragment();
										block.next = nextBlock;
										block = nextBlock;
									}
									// else{ : incomplete reading of block
								}
							}
							inputStream.close();
							status = Status.OK;
						} 
						catch (IOException e) {
							status = Status.LOAD_FAILED;
							e.printStackTrace();
						}
					}

					if(isCaching) {
						isLoaded = true;	
					}
				}
			}
		}
	}


	public void setTimestamp(long timestamp) {
		ensureLoaded();
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public Status getStatus() {
		ensureLoaded();
		return status;
	}


	/**
	 * 
	 * @return file size in bytes
	 */
	public long getLength() {
		ensureLoaded();
		return fileSize;
	}

	public MIME_Type getType() {
		ensureLoaded();
		return MIME_type;
	}

	private BytesChainLink createFragment() {
		return new BytesChainLink(new byte[fragmentLength], 0, 0);
	}

	/**
	 * 
	 * @throws IOException
	 */
	public BytesChainLink getHead() {
		ensureLoaded();
		return head;
	}

}
