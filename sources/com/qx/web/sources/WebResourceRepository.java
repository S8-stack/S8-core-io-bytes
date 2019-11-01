package com.qx.web.sources;

import java.lang.reflect.Field;

import com.qx.lang.xml.annotation.XML_SetValue;
import com.qx.lang.xml.annotation.XML_Type;

@XML_Type(name = "repo")
public class WebResourceRepository {


	public final static String MODULE_CLASSNAME = "QxModule";
	
	public final static String LOADER_FIELDNAME = "LOADER";
	
	/**
	 * 
	 */
	private String className;

	@XML_SetValue
	public void setClassName(String name) {
		this.className = name;
	}

	
	/**
	 * @return the loader associated with the repo. Return null if no loader can be
	 *         retrieved.
	 */
	public WebSourceLoader getLoader() {
		try {
			Class<?> type = Class.forName(className+'.'+MODULE_CLASSNAME);
			Field field = type.getField(LOADER_FIELDNAME);
			return (WebSourceLoader) field.get(null);
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		catch (NoSuchFieldException e) {
			e.printStackTrace();
		} 
		catch (SecurityException e) {
			e.printStackTrace();
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}


}
