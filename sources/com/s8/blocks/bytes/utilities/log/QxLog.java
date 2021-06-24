package com.s8.blocks.bytes.utilities.log;

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