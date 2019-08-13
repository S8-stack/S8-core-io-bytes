package com.qx.base.resources;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import com.qx.lang.xml.XML_Context;

/**
 * 
 * @author pc
 *
 */
public class WebResourcesBase {

	public final static String WEB_RESOURCES_FILENAME = "web-resources.xml";

	private Map<String, WebResource> resources;

	public WebResourcesBase() {
		super();
		this.resources = new HashMap<>();
	}



	public void load(QxModuleResourceLoader[] accesses, boolean isVerbose) throws Exception {
		XML_Context context = new XML_Context(WebResourcesBundle.class);
		for(QxModuleResourceLoader access : accesses) {
			Path path = access.getResourcePath(WEB_RESOURCES_FILENAME);

			try(InputStream inputStream = new FileInputStream(path.toFile())){
				WebResourcesBundle loader = (WebResourcesBundle) context.deserialize(inputStream);
				inputStream.close();
				loader.load(this, access, isVerbose);	
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("[WebResourcesBase] Failed to load: "+path.toString());
			}
		}
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
