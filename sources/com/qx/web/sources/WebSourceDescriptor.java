package com.qx.web.sources;

import com.qx.lang.xml.annotation.XML_SetValue;
import com.qx.lang.xml.annotation.XML_Type;
import com.qx.web.sources.js.JS_SourceDescriptor;

@XML_Type(name="source", sub={
		BytesWebSourceDescriptor.class,
		JS_SourceDescriptor.class
})
public abstract class WebSourceDescriptor extends WebSourcesDescriptor {

	private String pathname;
	
	@XML_SetValue
	public void setPathname(String pathname) {
		this.pathname = pathname;
	}

	@Override
	public String getPackagePath() {
		return pathname;
	}

	@Override
	public String getWebPath() {
		return pathname;
	}

}
