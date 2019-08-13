package com.qx.web;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 
 * @author pc
 *
 */
public class WebResourceBase {

	private Map<String, WebResource> resources;

	public WebResourceBase() {
		super();
		this.resources = new HashMap<>();
	}


	/**
	 * If resource has already been defined, the action is discarded
	 * 
	 * @param resource
	 */
	public void add(WebResource resource, boolean isVerbose) {
		String key = resource.getWebPathname();
		
		if(!resource.isServerSideSource()) {
			if(!resources.containsKey(key)) {
				if(isVerbose) {
					System.out.println("[WebResourceBase] added: "+
							resource.getLocalPathname()+
							" accessible as "+resource.getWebPathname());
				}
				resources.put(key, resource);	
			}	
		}
		else {
			// Mega warning
			System.out.println("[WebResourceBase] WARNING: attempt to add source file as a resource.");
			System.out.println("[WebResourceBase] WARNING: "+resource.getLocalPathname()+" is a source file.");
			System.out.println("[WebResourceBase] WARNING: Append action discarded");
		}
	}

	public WebResource get(String pathname) {
		return resources.get(pathname);
	}

	public void traverse(BiConsumer<String, WebResource> consumer) {
		resources.forEach(consumer);
	}

}
