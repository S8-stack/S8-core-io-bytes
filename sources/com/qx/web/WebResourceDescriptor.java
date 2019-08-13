package com.qx.web;

import java.nio.file.Path;

import com.qx.lang.xml.annotation.XML_SetAttribute;
import com.qx.lang.xml.annotation.XML_Type;

@XML_Type(name="descriptor", sub = {
		WebResourceFilter.class
})
public abstract class WebResourceDescriptor {


	public int fragmentLength = 8192;

	public boolean isCaching = true;

	@XML_SetAttribute(name="frag")
	public void setFragmentLength(int fragmentLength) {
		this.fragmentLength = fragmentLength;
	}

	@XML_SetAttribute(name="cache")
	public void setCached(boolean isCaching) {
		this.isCaching = isCaching;
	}

	public WebResourceDescriptor() {
		super();
	}	

	public WebResourceDescriptor(int fragmentLength, boolean isCaching) {
		super();
		this.isCaching = isCaching;
		this.fragmentLength = fragmentLength;
	}




	/**
	 * @param base the base to be populated
	 * @param moduleRootPath the path of .class file initiating the loading (used as reference)
	 * @param moduleWebPathname the factorized module web pathname. Can be left to '' for multi-application definitions
	 * @param verbosity flag
	 */
	public abstract void load(
			WebResourceBase base, 
			Path moduleRootPath, 
			String moduleWebPathname, 
			boolean isVerbose);

}
