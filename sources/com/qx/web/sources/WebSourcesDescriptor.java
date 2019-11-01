package com.qx.web.sources;

import com.qx.lang.xml.annotation.XML_SetAttribute;
import com.qx.lang.xml.annotation.XML_Type;

@XML_Type(name="descriptor", sub={ BundledWebSourcesDescriptor.class, WebSourceDescriptor.class })
public abstract class WebSourcesDescriptor {

	public class Computed {

		public String packagePath;

		public String webPath;

		public int bufferCapacity = 8192;

		public boolean isCached = true;

		public boolean isVerbose;
		
		public WebSourceAccessLevel level;

		public Computed(
				String packagePath, 
				String webPath, 
				int bufferCapacity, 
				boolean isCached, 
				boolean isVerbose,
				WebSourceAccessLevel level) {
			super();
			this.packagePath = packagePath;
			this.webPath = webPath;
			this.bufferCapacity = bufferCapacity;
			this.isCached = isCached;
			this.isVerbose = isVerbose;
			this.level = level;
		}
	}

	public class Settings {

		private boolean isBufferCapacitySet = false;
		private int bufferCapacity = 8192;

		private boolean isCachedSet = false;
		private boolean isCached = true;
		
		public boolean isPowerLevelSet = false;
		public WebSourceAccessLevel level = WebSourceAccessLevel.UNREGISTERED;

		public boolean isVerboseSet = false;
		public boolean isVerbose = true;

		public void setBufferCapacity(int capacity) {
			this.isBufferCapacitySet = true;
			this.bufferCapacity = capacity;
		}	
		
		public void setCached(boolean flag) {
			this.isCachedSet = true;
			this.isCached = flag;
		}
		
		public void setPowerLevel(WebSourceAccessLevel level) {
			this.isPowerLevelSet = true;
			this.level = level;
		}
		
		public void setVerbose(boolean flag) {
			this.isVerboseSet = true;
			this.isVerbose = flag;
		}
		
		public Computed override(Computed parent) {
			String packagePath = getPackagePath();
			String webPath = getWebPath();
			return new Computed(
					(packagePath!=null?
							(parent.packagePath!=null?parent.packagePath+'/'+packagePath:packagePath)
							:parent.packagePath),
					(webPath!=null?
							(parent.webPath!=null?parent.webPath+'/'+webPath:webPath)
							:parent.webPath),
					(isBufferCapacitySet?bufferCapacity:parent.bufferCapacity),
					(isCachedSet?isCached:parent.isCached),
					(isVerboseSet?isVerbose:parent.isVerbose),
					(isPowerLevelSet?level:parent.level));
		}

		public Computed compute() {
			return new Computed(
					getPackagePath(), 
					getWebPath(),
					bufferCapacity,
					isCached,
					isVerbose,
					level);
		}
	}


	public Settings settings = new Settings();


	@XML_SetAttribute(name="buffer")
	public void setPreferredBufferCapacity(int capacity) {
		settings.setBufferCapacity(capacity);
	}

	@XML_SetAttribute(name="cache")
	public void setCached(boolean isCached) {
		settings.setCached(isCached);
	}

	@XML_SetAttribute(name="verbose")
	public void setVerbose(boolean isVerbose) {
		settings.setVerbose(isVerbose);
	}
	
	@XML_SetAttribute(name="level")
	public void setLevel(WebSourceAccessLevel level) {
		settings.setPowerLevel(level);
	}


	public abstract String getPackagePath();

	public abstract String getWebPath();

	public abstract void load(WebSources sources,  WebSourceLoader loader,  Computed computed) throws Exception;

}
