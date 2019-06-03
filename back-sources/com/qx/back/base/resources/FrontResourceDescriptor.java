package com.qx.back.base.resources;

import java.nio.file.Path;

public abstract class FrontResourceDescriptor {

	public abstract void register(FrontResourceBase base, Path rootPath, String rootPathname);
	
}
