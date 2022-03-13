package com.s8.io.bytes.utilities.log;



/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class QxLog {

	
	/**
	 * 
	 * @param message
	 */
	public abstract void log(String message);
	
	
	/**
	 * 
	 * @param type
	 * @param message
	 */
	public abstract void log(Class<?> type, String message);
	
	
	/**
	 * 
	 * @param exception
	 */
	public abstract void log(Exception exception);
	
}
