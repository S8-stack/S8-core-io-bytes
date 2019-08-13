package com.qx.base.resources;

import java.nio.file.Path;

import com.qx.lang.xml.annotation.XML_SetElement;
import com.qx.lang.xml.annotation.XML_Type;

@XML_Type(name="web-loader", sub={})
public class WebResourcesBundle {

	private String rootPathname;
	
	private Path rootPath;

	public String webRootPathname;

	private WebResourceDescriptor[] descriptors;


	@XML_SetElement(name="package")
	public void setRootPathname(String pathname) {
		if(pathname==null) {
			pathname = "";
		}
		this.rootPathname = pathname;
	}

	@XML_SetElement(name="web")
	public void setWebRootPathname(String webPathname) {
		if(webPathname==null) {
			webPathname = "";
		}
		this.webRootPathname = webPathname;
	}

	@XML_SetElement(name="resources")
	public void setDescriptors(WebResourceDescriptor[] descriptors) {
		this.descriptors = descriptors;
	}


	public void load(WebResourcesBase base, QxModuleResourceLoader loader, boolean isVerbose) {
		this.rootPath = loader.getResourcePath(rootPathname);
		for(WebResourceDescriptor descriptor : descriptors) {
			descriptor.load(base, rootPath, webRootPathname, isVerbose);
		}
	}

}
