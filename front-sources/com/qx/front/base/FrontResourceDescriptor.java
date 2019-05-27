package com.qx.front.base;

import java.util.List;

public abstract class FrontResourceDescriptor {

	public abstract void register(FrontResourceLoader loader, List<FrontResourceHandle> list);
	
}
