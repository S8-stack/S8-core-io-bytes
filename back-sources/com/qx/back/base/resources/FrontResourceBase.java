package com.qx.back.base.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 
 * @author pc
 *
 */
public class FrontResourceBase {

	private Map<String, FrontResource> resources;

	public FrontResourceBase() {
		super();
	}
	
	
	public void initialize(FrontResourceLoader... loaders) {
		this.resources = new HashMap<>();
		for(FrontResourceLoader loader : loaders) {
			loader.acquire(this);
		}
	}
	
	
	public void add(FrontResource resource) {
		resources.put(resource.getPathname(), resource);
	}


	public FrontResource get(String pathname) {
		return resources.get(pathname);
	}
	
	public void traverse(BiConsumer<String, FrontResource> consumer) {
		resources.forEach(consumer);
	}

}
