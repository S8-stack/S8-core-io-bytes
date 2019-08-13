package com.qx.base.resources;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import com.qx.base.bytes.BytesChainLink;

public class WebResource {
	
	public final static boolean DEBUG_FORCE_RELOADING = true;


	public static enum Status {
		OK, NOT_FOUND, LOAD_FAILED;
	}


	private Status status;

	private long bytecount;

	/**
	 * pathanme (from web side), acts as id
	 */
	private String webPathname;

	
	/**
	 * local pathname to retrieve the resource
	 */
	private Path localPath;
	
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

	
	public WebResource(Path path, String pathname, int fragmentLength, boolean isCaching){
		
		// path
		this.localPath = path;
		
		// pathname
		this.webPathname = pathname;
		
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
	
	public String getWebPathname() {
		return webPathname;
	}
	
	public String getLocalPathname() {
		return localPath.toString();
	}

	private void ensureLoaded() {
		// avoid calling synchronized method is already loaded
		if(!isLoaded) { 
			// sync block
			synchronized(lock) {
				// prevent awaken sync block to re-sync
				if(!isLoaded) {
					if(!Files.exists(localPath, LinkOption.NOFOLLOW_LINKS)) {
						status = Status.NOT_FOUND;
					}
					else {
						try {
							bytecount = 0;
							BytesChainLink block = createFragment(), nextBlock;

							InputStream inputStream = Files.newInputStream(localPath);

							head = block;
							boolean isDone = false;
							int nBytes;
							int capacity = block.bytes.length, position = block.offset;
							while(!isDone){

								nBytes = inputStream.read(block.bytes, position, capacity-position);
								// end of stream reached
								if(nBytes==-1){ 
									isDone = true;
								}
								else {
									position+=nBytes;
									block.length+=nBytes;
									bytecount+=nBytes;

									if(position==capacity){ // full loading of block
										
										// create next block
										nextBlock = createFragment();
										
										// link next block
										block.next = nextBlock;
										
										// reset scope vars
										block = nextBlock;
										capacity = block.bytes.length;
										position = block.offset;
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
		return bytecount;
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

	@Override
	public String toString() {
		return webPathname;
	}


	public Path getPath() {
		return localPath;
	}
	
	
	public boolean isServerSideSource() {
		String pathname = localPath.toString();
		int n = pathname.length();
		String extension = pathname.substring(n-6, n);
		if(extension.equals(".class")) {
			return true;
		}
		else {
			return false;
		}
	}
}
