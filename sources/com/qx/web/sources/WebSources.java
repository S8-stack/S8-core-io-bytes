package com.qx.web.sources;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.qx.lang.xml.XML_Context;
import com.qx.web.sources.js.JS_Module;
import com.qx.web.sources.js.JS_Source;

/**
 * 
 * @author pc
 *
 */
public class WebSources {

	public final static String WEB_SOURCES_FILENAME = "web-sources.xml";

	private String moduleName;
	
	private List<WebSource> sources;
	
	private JS_Module jsModule;

	public WebSources(String moduleName, WebSourceLoader loader) {
		super();
		
		this.sources = new ArrayList<>();
		this.jsModule = new JS_Module();
		
		try(InputStream inputStream = loader.getResource(WEB_SOURCES_FILENAME)){
			if(inputStream!=null) {
				BundledWebSourcesDescriptor bundle = (BundledWebSourcesDescriptor) getDesciptorXmlContext().deserialize(inputStream);
				bundle.load(this, loader, null);
			}
			else {
				throw new Exception("Failed to load "+WEB_SOURCES_FILENAME);
			}
			inputStream.close();
		}
		catch (IOException e) {
			System.err.println("[WebResourcesBase] Failed to load: "+moduleName+", due to "+e.getMessage());
			e.printStackTrace();
		} 
		catch (Exception e) {
			System.err.println("[WebResourcesBase] Failed to load: "+moduleName+", due to "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String getModuleName() {
		return moduleName;
	}

	
	public void add(WebSource source, boolean isVerbose) {
		if(isVerbose) {
			System.out.println(source.getWebPathname()+" is added (pointing towards: "+source.getLocalPathname()+")");
		}
		sources.add(source);
		
		// push js to js-module
		if(source.getType()==JS_Source.TYPE) {
			jsModule.add((JS_Source) source);	
		}
	}
	
	public String JS_getCatalog() {
		return jsModule.getCatalog();
	}


	private static XML_Context CONTEXT;

	private XML_Context getDesciptorXmlContext() {
		if(CONTEXT==null) {
			try {
				CONTEXT = new XML_Context(WebSourcesDescriptor.class);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return CONTEXT;
	}

}
