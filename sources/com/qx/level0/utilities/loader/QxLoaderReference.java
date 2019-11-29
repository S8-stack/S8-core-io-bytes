package com.qx.level0.utilities.loader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.qx.level0.lang.xml.annotation.XML_SetValue;
import com.qx.level0.lang.xml.annotation.XML_Type;

@XML_Type(name = "module")
public class QxLoaderReference {

	private String className;
	
	private QxLoader loader;
	
	@XML_SetValue
	public void setClassname(String name) {
		this.className = name;
	}
	
	public void build() throws 
			ClassNotFoundException, 
			NoSuchMethodException, 
			SecurityException, 
			InstantiationException, 
			IllegalAccessException, 
			IllegalArgumentException, 
			InvocationTargetException {
		
		Class<?> loaderClass = Class.forName(className);
		Constructor<?> loaderConstructor = loaderClass.getConstructor(new Class<?>[] {});
		loader = (QxLoader) loaderConstructor.newInstance(new Object[] {});	
	}
	
	
	/**
	 * Loader might be null
	 * @return
	 */
	public QxLoader getLoader(){
		if(loader==null) {
			try {
				build();
			} catch (ClassNotFoundException 
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
