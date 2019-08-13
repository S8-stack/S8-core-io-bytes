package com.qx.base.resources;

import java.io.InputStream;


/**
 * 
 * @author pc
 *
 */
public interface QxLocalLoader {

	/**
	 * 
	 * @param pathname
	 * @return
	 */
	public InputStream getResource(String pathname);
	
}
