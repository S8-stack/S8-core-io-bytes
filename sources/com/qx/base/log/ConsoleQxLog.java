package com.qx.base.log;

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
