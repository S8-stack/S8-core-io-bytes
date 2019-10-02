package com.qx.base.files;

import java.nio.file.Path;

public class QxFileReadingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8157708439342941308L;
	
	
	public Path path;

	public QxFileReadingException(Path path, String message) {
		super(message);
		this.path = path;
	}
	
	/**
	 * Degraded version for later definition of path
	 * @param message
	 */
	public QxFileReadingException(String message) {
		super(message);
	}
}
