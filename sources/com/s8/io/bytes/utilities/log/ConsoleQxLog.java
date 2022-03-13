package com.s8.io.bytes.utilities.log;


/**
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class ConsoleQxLog extends QxLog {

	@Override
	public void log(String message) {
		System.out.println(message);
	}

	@Override
	public void log(Class<?> type, String message) {
		System.out.println("["+type.getName()+"]: "+message);
	}

	@Override
	public void log(Exception exception) {
		System.err.println(exception.getMessage());
	}

}
