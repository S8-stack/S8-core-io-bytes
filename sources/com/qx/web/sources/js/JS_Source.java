package com.qx.web.sources.js;

import java.io.IOException;

import com.qx.base.bytes.BytesChainLink;
import com.qx.web.mime.MIME_Type;
import com.qx.web.sources.WebSource;
import com.qx.web.sources.WebSourceLoader;
import com.qx.web.sources.WebSourceAccessLevel;


/**
 * 
 * @author pc
 *
 */
public class JS_Source extends WebSource {
	
	public final static MIME_Type TYPE = MIME_Type.get("application/javascript");
	

	private boolean isLoaded;

	private Payload payload;

	/**
	 * sync lock
	 */
	private Object lock;
	
	public JS_Source(
			WebSourceLoader loader, 
			WebSourceAccessLevel level,
			String packagePathname, 
			String webPathname, 
			boolean isCached,
			int fragmentLength) {
		super(loader, level, packagePathname, webPathname, isCached, fragmentLength);
		
		lock = new Object();	
	}
	


	@Override
	public MIME_Type getType() {
		return TYPE;
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
