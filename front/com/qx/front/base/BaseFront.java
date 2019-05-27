package com.qx.front.base;

/**
 * Root point for computing path to further load client-side resources.
 * 
 * @author pc
 *
 */
public class BaseFront {

	public final static FrontResourceLoader LOADER = 
			new FrontResourceLoader(BaseFront.class, "/base/", new FrontResourceDescriptor[] {
					new FrontFile("test.css")
			});

}
