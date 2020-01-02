package com.qx.level0.utilities.loader;

import com.qx.level0.lang.xml.annotation.XML_SetElement;
import com.qx.level0.lang.xml.annotation.XML_Type;

@XML_Type(name = "code-base", isRoot = true)
public class XmlQxCodeBase {

	private XmlQxModule[] modules;
	
	/**
	 * 
	 * @param modules
	 */
	@XML_SetElement(tag = "modules")
	public void setModules(XmlQxModule[] modules) {
		this.modules = modules;
	}
	
	public QxCodeBase deserialize() {
		if(modules!=null) {
			int n = modules.length;
			QxModuleLoader[] loaders = new QxModuleLoader[n];
			for(int i=0; i<n; i++) {
				loaders[i] = modules[i].getLoader();
			}
			return new QxCodeBase(loaders);	
		}
		else {
			return new QxCodeBase(new QxModuleLoader[]{});
		}
	}
	
}
