package com.s8.io.bytes;

import java.io.IOException;
import java.nio.file.Path;



/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class ByteFileReadingException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8157708439342941308L;
	
	
	public Path path;

	public ByteFileReadingException(Path path, String message) {
		super(message);
		this.path = path;
	}
	
	/**
	 * Degraded version for later definition of path
	 * @param message
	 */
	public ByteFileReadingException(String message) {
		super(message);
	}
}
