package com.qx.base;

import java.io.InputStream;

import com.qx.base.loader.QxModuleResourceLoader;

/**
 * Root point for computing path to further load client-side resources.
 * 
 * @author pc
 *
 */
public class QxModule {

	public final static QxModuleResourceLoader LOADER = new QxModuleResourceLoader() {

		@Override
		public InputStream getResource(String pathname) {
			return QxModule.class.getResourceAsStream(pathname);
		}

		@Override
		public String getTargetName() {
			return QxModule.class.getName();
		}
	};
}
