package com.qx.front.base;

import java.nio.file.Path;

public class FrontResourceHandle {

	public String pathname;
	
	public Path path;

	public FrontResourceHandle(String pathname, Path path) {
		super();
		this.pathname = pathname;
		this.path = path;
	}
	
	@Override
	public String toString() {
		return pathname+" -> "+path.toString();
	}
	
}
