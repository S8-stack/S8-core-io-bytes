package com.qx.web.sources;

import java.io.InputStream;



/**
 * Transitive approach to get access rights to resources within a package
 * @author pc
 *
 */
public abstract class WebSourceLoader {

	
	public WebSourceLoader() {
		super();
	}
	
	
	public InputStream getResource(String pathname) {
		if(isServerSideSource(pathname)) {
			return null;
		}
		return open(pathname);
	}
	
	
	public abstract InputStream open(String pathname);


	/**
	 * Super double check to prevent inserting a .class file by mistake
	 * @return
	 */
	private static boolean isServerSideSource(String pathname) {
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
