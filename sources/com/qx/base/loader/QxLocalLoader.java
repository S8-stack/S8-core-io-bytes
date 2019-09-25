package com.qx.base.loader;

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
