package com.qx.web.sources.js;

import com.qx.lang.xml.annotation.XML_Type;
import com.qx.web.sources.WebSource;
import com.qx.web.sources.WebSourceDescriptor;
import com.qx.web.sources.WebSourceLoader;
import com.qx.web.sources.WebSources;

@XML_Type(name="js", sub={})
public class JS_SourceDescriptor extends WebSourceDescriptor {
	
	@Override
	public void load(WebSources sources, WebSourceLoader loader, Computed computed) throws Exception {
		
		computed = settings.override(computed);
		
		WebSource resource = new JS_Source(
				loader,
				computed.level,
				computed.packagePath, 
				computed.webPath, 
				computed.isCached,
				computed.bufferCapacity);
		
		sources.add(resource, computed.isVerbose);		
	}

}
