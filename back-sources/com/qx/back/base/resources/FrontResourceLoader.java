package com.qx.back.base.resources;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FrontResourceLoader {
	
	public Path rootPath;
	
	public String rootPathname;
	
	private FrontResourceDescriptor[] descriptors;
	
	public FrontResourceLoader(
			Class<?> type,
			String rootPathname, 
			FrontResourceDescriptor[] descriptors) {
		super();
		this.rootPath = Paths.get(type.getResource("").getPath());
		this.rootPathname = rootPathname;
		this.descriptors = descriptors;
	}
	
	
	public void load(FrontResourceBase base) {
		for(FrontResourceDescriptor descriptor : descriptors) {
			descriptor.register(base, rootPath, rootPathname);
		}
	}
	
}
