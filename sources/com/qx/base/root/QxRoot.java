package com.qx.base.root;

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


@XML_Type(name="QxRoot")
public class QxRoot {

	/**
	 * config name
	 * @author pc
	 *
	 */
	private String name;
	
	private Path path;


	@XML_SetAttribute(name="name")
	public void setName(String name) {
		this.name = name;
	}
	
	@XML_SetElement(name="path")
	public void setPath(String pathname) {
		this.path = Paths.get(pathname);
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

	@XML_Type(name="QxRootPresets")
	public static class Presets {

		private Map<String, QxRoot> map;

		public Presets() {
			super();
		}

		@XML_SetElement(name="fork")
		public void setEnvironments(QxRoot[] envs) {
			this.map = new HashMap<String, QxRoot>();
			if(envs!=null) {
				for(QxRoot environment : envs) {
					map.put(environment.name, environment);
				}
			}
		}
		
		public QxRoot get(String name) {
			return map.get(name);
		}
	}

	private static Presets presets;

	public final static String PRESETS_FILENAME = "roots.xml";

	private static InputStream openInputStream() {
		return new BufferedInputStream(QxRoot.class.getResourceAsStream(PRESETS_FILENAME));
	}

	public static QxRoot get(String name) {
		if(presets==null) {
			try (InputStream inputStream = openInputStream()){
				XML_Context context = new XML_Context(QxRoot.Presets.class);
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
