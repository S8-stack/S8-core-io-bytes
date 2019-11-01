package com.qx.web.sources;

import com.qx.lang.xml.annotation.XML_SetElement;
import com.qx.lang.xml.annotation.XML_Type;

@XML_Type(name="bundle", sub={})
public class BundledWebSourcesDescriptor extends WebSourcesDescriptor {

	private String packagePath;

	private String webPath;

	private WebSourcesDescriptor[] descriptors;
	
	@XML_SetElement(name="package")
	public void setPackagePath(String pathname) {
		if(pathname!=null && pathname.length()==0) {
			pathname = null;
		}
		this.packagePath = pathname;
	}

	@XML_SetElement(name="web")
	public void setWebPathname(String pathname) {
		if(pathname!=null && pathname.length()==0) {
			pathname = null;
		}
		this.webPath = pathname;
	}
	
	@XML_SetElement(name="sources")
	public void setFiles(WebSourcesDescriptor[] files) {
		this.descriptors = files;
	}


	@Override
	public void load(WebSources sources, WebSourceLoader loader,
			Computed computed) throws Exception {
		
		if(computed!=null) {
			computed = settings.override(computed);	
		}
		else {
			computed = settings.compute();
		}
		
		if(descriptors!=null) {
			for(WebSourcesDescriptor descriptor : descriptors) {
				descriptor.load(sources, loader, computed);
			}
		}
	}

	@Override
	public String getPackagePath() {
		return packagePath;
	}

	@Override
	public String getWebPath() {
		return webPath;
	}

}
