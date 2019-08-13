package com.qx.web;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.qx.lang.xml.annotation.XML_SetElement;
import com.qx.lang.xml.annotation.XML_Type;

@XML_Type(name="web-loader", sub={})
public class WebLoader {

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


	public void load(WebResourceBase base, Class<?> type, boolean isVerbose) {
		
		this.rootPath = Paths.get(type.getResource(rootPathname).getPath());
		for(WebResourceDescriptor descriptor : descriptors) {
			descriptor.load(base, rootPath, webRootPathname, isVerbose);
		}
	}

}
