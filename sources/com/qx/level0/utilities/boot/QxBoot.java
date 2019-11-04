package com.qx.level0.utilities.boot;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.qx.level0.lang.xml.annotation.XML_SetAttribute;
import com.qx.level0.lang.xml.annotation.XML_SetElement;
import com.qx.level0.lang.xml.annotation.XML_Type;
import com.qx.level0.lang.xml.XML_Context;


@com.qx.level0.lang.xml.annotation.XML_Type(name="QxBoot")
public class QxBoot {

	/**
	 * config name
	 * @author pc
	 *
	 */
	private String name;
	
	private Path root;
	
	private boolean isDebugEnabled;


	@XML_SetAttribute(name="name")
	public void setName(String name) {
		this.name = name;
	}
	
	@XML_SetElement(name="path")
	public void setPath(String pathname) {
		this.root = Paths.get(pathname);
	}
	
	@XML_SetElement(name="debug")
	public void setDebug(boolean flag) {
		this.isDebugEnabled = flag;
	}


	public String getName() {
		return name;
	}

	public Path getRootPath() {
		return root;
	}

	public Path getPath(String continuation) {
		return root.resolve(continuation);
	}

	public boolean isDebugEnabled() {
		return isDebugEnabled;
	}
	
	@XML_Type(name="QxBoots")
	public static class Presets {

		private Map<String, QxBoot> map;

		public Presets() {
			super();
		}

		@XML_SetElement(name="fork")
		public void setEnvironments(QxBoot[] envs) {
			this.map = new HashMap<String, QxBoot>();
			if(envs!=null) {
				for(QxBoot environment : envs) {
					map.put(environment.name, environment);
				}
			}
		}
		
		public QxBoot get(String name) {
			return map.get(name);
		}
	}

	private static Presets presets;

	public final static String PRESETS_FILENAME = "boots.xml";

	private static InputStream openInputStream() {
		return new BufferedInputStream(QxBoot.class.getResourceAsStream(PRESETS_FILENAME));
	}

	public static QxBoot get(String name) throws Exception {
		if(presets==null) {
			try (InputStream inputStream = openInputStream()){
				XML_Context context = new XML_Context(QxBoot.Presets.class);
				presets = (Presets) context.deserialize(inputStream);
				inputStream.close();
			} 
			catch (Exception e) {
				e.printStackTrace();
				throw new Exception("Cannot load run mode "+name+" due to "+e.getMessage());
			}
		}
		
		QxBoot runMode = presets.get(name);
		if(runMode==null) {
			throw new Exception("Cannot find run mode: "+name);
		}
		
		return runMode;
	}
	
	
	/**
	 * Assume that args only contains a single object, which is the mode name
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static QxBoot boot(String[] args) throws Exception {
		if(args==null || args.length!=1) {
			throw new Exception("Wrong number of arguments");
		}
		String runModeName = args[0];
		return QxBoot.get(runModeName);
	}
}
