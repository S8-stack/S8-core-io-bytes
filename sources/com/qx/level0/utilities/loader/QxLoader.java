package com.qx.level0.utilities.loader;

import java.io.InputStream;


/**
 * <p>
 * <code>QxLoader</code> is an equivalent of a <b>module</b>.
 * </p>
 * @author pc
 *
 */
public interface QxLoader {


	/**
	 * 
	 * @return module name
	 */
	public abstract String getName();
	
	
	/**
	 * 
	 * @param pathname
	 * @return
	 */
	public abstract InputStream getResource(String pathname);	


}
