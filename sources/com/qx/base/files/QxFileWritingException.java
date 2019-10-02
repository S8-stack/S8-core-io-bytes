package com.qx.base.files;

import java.nio.file.Path;

public class QxFileWritingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8157708439342941308L;
	
	
	public Path path;

	public QxFileWritingException(Path path, String message) {
		super(message);
		this.path = path;
	}
	
	/**
	 * Degraded version for later definition of path
	 * @param message
	 */
	public QxFileWritingException(String message) {
		super(message);
	}
}
