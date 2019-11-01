package com.qx.base;

import java.io.InputStream;

import com.qx.web.sources.WebSourceLoader;

/**
 * Root point for computing path to further load client-side resources.
 * 
 * @author pc
 *
 */
public class WebSources extends com.qx.web.sources.WebSources {

	public WebSources() {
		super("base", new WebSourceLoader() {
			public @Override InputStream open(String pathname) {
				return WebSources.class.getResourceAsStream(pathname);
			}
		});
	}

}
