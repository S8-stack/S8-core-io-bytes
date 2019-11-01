package com.qx.web.sources;

import com.qx.lang.xml.annotation.XML_Type;

@XML_Type(name="bytes", sub={})
public class BytesWebSourceDescriptor extends WebSourceDescriptor {

	
	@Override
	public void load(WebSources sources, WebSourceLoader loader, Computed computed) throws Exception {
		
		computed = settings.override(computed);
		
		WebSource resource = new BytesWebSource(
				loader,
				computed.level,
				computed.packagePath, 
				computed.webPath, 
				computed.isCached,
				computed.bufferCapacity);
		
		sources.add(resource, computed.isVerbose);		
		
	}
}
