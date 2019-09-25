package com.qx.base.loader;

import java.io.InputStream;



/**
 * Transitive approach to get access rights to resources within a package
 * @author pc
 *
 */
public abstract class QxModuleResourceLoader {

	
	public QxModuleResourceLoader() {
		super();
	}
	
	
	public abstract InputStream getResource(String pathname);


	public abstract String getTargetName();
	
	
}
