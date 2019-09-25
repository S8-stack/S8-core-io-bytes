package com.qx.base.run;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.qx.lang.xml.XML_Context;
import com.qx.lang.xml.annotation.XML_SetAttribute;
import com.qx.lang.xml.annotation.XML_SetElement;
import com.qx.lang.xml.annotation.XML_Type;


@XML_Type(name="QxRunMode")
public class QxRunMode {

	/**
	 * config name
	 * @author pc
	 *
	 */
	private String name;
	
	private Path path;
	
	private boolean isDebugEnabled;


	@XML_SetAttribute(name="name")
	public void setName(String name) {
		this.name = name;
	}
	
	@XML_SetElement(name="path")
	public void setPath(String pathname) {
		this.path = Paths.get(pathname);
	}
	
	@XML_SetElement(name="debug")
	public void setDebug(boolean flag) {
		this.isDebugEnabled = flag;
	}


	public String getName() {
		return name;
	}

	public Path getPath() {
		return path;
	}

	public Path getPath(String continuation) {
		return path.resolve(continuation);
	}

	public boolean isDebugEnabled() {
		return isDebugEnabled;
	}
	
	@XML_Type(name="QxRunModes")
	public static class Presets {

		private Map<String, QxRunMode> map;

		public Presets() {
			super();
		}

		@XML_SetElement(name="fork")
		public void setEnvironments(QxRunMode[] envs) {
			this.map = new HashMap<String, QxRunMode>();
			if(envs!=null) {
				for(QxRunMode environment : envs) {
					map.put(environment.name, environment);
				}
			}
		}
		
		public QxRunMode get(String name) {
			return map.get(name);
		}
	}

	private static Presets presets;

	public final static String PRESETS_FILENAME = "run-modes.xml";

	private static InputStream openInputStream() {
		return new BufferedInputStream(QxRunMode.class.getResourceAsStream(PRESETS_FILENAME));
	}

	public static QxRunMode get(String name) {
		if(presets==null) {
			try (InputStream inputStream = openInputStream()){
				XML_Context context = new XML_Context(QxRunMode.Presets.class);
				presets = (Presets) context.deserialize(inputStream);
				inputStream.close();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return presets.get(name);
	}
}
