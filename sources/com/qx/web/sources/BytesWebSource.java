package com.qx.web.sources;

import java.io.IOException;

import com.qx.base.bytes.BytesChainLink;
import com.qx.web.mime.MIME_Type;

/**
 * <p>Raw source file, served without transformations</p>
 * @author pc
 *
 */
public class BytesWebSource extends WebSource {

	private MIME_Type type;

	//private long timestamp;

	private boolean isLoaded;

	private Payload payload;

	/**
	 * sync lock
	 */
	private Object lock;


	/**
	 * payload
	 */


	public BytesWebSource(WebSourceLoader loader, 
			WebSourceAccessLevel level,
			String packagePathname, 
			String webPathname, 
			boolean isCached,
			int fragmentLength) {
		super(loader, level, packagePathname, webPathname, isCached, fragmentLength);

		lock = new Object();
		type = MIME_Type.find(webPathname);

	}


	/*
	public void setTimestamp(long timestamp) {
		reload();
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}

	 */


	@Override
	public MIME_Type getType() {
		return type;
	}


	@Override
	public Status load() {
		// avoid calling synchronized method is already loaded
		if(!isLoaded) { 
			// sync block
			synchronized(lock) {
				// prevent awaken sync block to re-sync
				if(!isLoaded) {

					payload = new Payload();

					if(isCached()) {
						isLoaded = true;	
					}
				}
			}
		}
		return payload.status;
	}


	/**
	 * 
	 * @return file size in bytes
	 */
	@Override
	public long getBytecount() {
		return payload.bytecount;
	}


	/**
	 * 
	 * @throws IOException
	 */
	public BytesChainLink getHead() {
		return payload.head;
	}

}
