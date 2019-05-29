package com.qx.front.base;

import java.nio.file.Path;
import java.util.List;

public class FrontFile extends FrontResourceDescriptor {


	private String path;
	
	public FrontFile(String path) {
		super();
		this.path = path;
	}

	@Override
	public void register(FrontResourceLoader loader, List<FrontResourceHandle> list) {
		Path resolvedPath = loader.rootPath.resolve(path);
		String resolvedKey = loader.rootKey+'/'+path;
		list.add(new FrontResourceHandle(resolvedKey, resolvedPath));
	}
}
