package com.qx.front.base;

import com.qx.back.base.resources.FrontFolder;
import com.qx.back.base.resources.FrontResourceDescriptor;
import com.qx.back.base.resources.FrontResourceLoader;

/**
 * Root point for computing path to further load client-side resources.
 * 
 * @author pc
 *
 */
public class BaseFront {

	public final static FrontResourceLoader LOADER = 
			new FrontResourceLoader(BaseFront.class, "/base", 
					new FrontResourceDescriptor[] {
							new FrontFolder("", 8192, true)
			});

}
