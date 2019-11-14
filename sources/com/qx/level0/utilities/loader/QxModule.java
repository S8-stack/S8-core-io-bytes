package com.qx.level0.utilities.loader;

import java.io.InputStream;


/**
 * 
 * @author pc
 *
 */
public class QxModule {

	/**
	 * 
	 * @author pc
	 *
	 */
	public static abstract class Loader {

		/**
		 * 
		 * @param pathname
		 * @return
		 */
		public abstract InputStream getResource(String pathname);	
		
	}
	
}
