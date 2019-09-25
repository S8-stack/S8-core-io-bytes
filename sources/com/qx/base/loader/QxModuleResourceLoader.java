package com.qx.base.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;



/**
 * Transitive approach to get access rights to resources within a package
 * @author pc
 *
 */
public abstract class QxModuleResourceLoader {

	
	public QxModuleResourceLoader() {
		super();
	}
	
	
	public abstract URL getResource(String pathname);
	
	
	public Path getResourcePath(String pathname) {
		try {
			URL url = getResource(pathname);
			if(url!=null) {
				return Paths.get(url.toURI());	
			}
			else {
				return null;
			}
		} 
		catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public InputStream getResourceAsStream(String pathname) {
		try {
			File file = new File(getResource(pathname).toURI());
			return new FileInputStream(file);
		} 
		catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
