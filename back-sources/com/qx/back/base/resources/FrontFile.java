package com.qx.back.base.resources;

import java.nio.file.Path;

public class FrontFile extends FrontResourceDescriptor {


	private String path;
	
	private boolean isCaching;
	
	private int fragmentLength;
	
	public FrontFile(String path, boolean isCaching, int fragmentLength) {
		super();
		this.path = path;
		this.isCaching = isCaching;
		this.fragmentLength = fragmentLength;
	}
	
	public FrontFile(String path, boolean isCaching) {
		super();
		this.path = path;
		this.isCaching = isCaching;
		this.fragmentLength = 8192;
	}

	@Override
	public void register(FrontResourceBase base, Path rootPath, String rootPathname) {
		Path resolvedPath = rootPath.resolve(path);
		String resolvedPathname = rootPathname+'/'+path;
		base.add(new FrontResource(resolvedPath, resolvedPathname, fragmentLength, isCaching));
	}
}
