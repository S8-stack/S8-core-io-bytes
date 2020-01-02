package com.qx.level0.utilities.loader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.qx.level0.lang.xml.annotation.XML_SetValue;
import com.qx.level0.lang.xml.annotation.XML_Type;

@XML_Type(name = "module")
public class XmlQxModule {

	private String className;
	
	private QxModuleLoader loader;
	
	@XML_SetValue
	public void setClassname(String name) {
		this.className = name;
	}
	
	/**
	 * Loader might be null
	 * @return
	 */
	public QxModuleLoader getLoader(){
		if(loader==null) {
			try {
				Class<?> loaderClass = Class.forName(className);
				Constructor<?> loaderConstructor = loaderClass.getConstructor(new Class<?>[] {});
				loader = (QxModuleLoader) loaderConstructor.newInstance(new Object[] {});	
			} 
			catch (ClassNotFoundException 
					| NoSuchMethodException 
					| SecurityException 
					| InstantiationException
					| IllegalAccessException 
					| IllegalArgumentException 
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return loader;
	}
	
}
