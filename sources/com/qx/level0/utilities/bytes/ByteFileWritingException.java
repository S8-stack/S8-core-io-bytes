package com.qx.level0.utilities.bytes;

import java.nio.file.Path;

public class ByteFileWritingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8157708439342941308L;
	
	
	public Path path;

	public ByteFileWritingException(Path path, String message) {
		super(message);
		this.path = path;
	}
	
	/**
	 * Degraded version for later definition of path
	 * @param message
	 */
	public ByteFileWritingException(String message) {
		super(message);
	}
}
