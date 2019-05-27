package com.qx.front.base;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FrontResourceLoader {
	
	public Path rootPath;
	
	public String rootKey;
	
	private FrontResourceDescriptor[] descriptors;
	
	public FrontResourceLoader(Class<?> type, String rootKey, FrontResourceDescriptor[] descriptors) {
		super();
		this.rootPath = Paths.get(type.getResource("").getPath());
		this.rootKey = rootKey;
		this.descriptors = descriptors;
	}
	
	
	public void register(List<FrontResourceHandle> list) {
		for(FrontResourceDescriptor descriptor : descriptors) {
			descriptor.register(this, list);
		}
	}
	
}
