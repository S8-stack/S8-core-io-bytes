package com.qx.back.base.test.front;

import java.util.ArrayList;
import java.util.List;

import com.qx.front.base.FrontResourceHandle;

public class Test {

	public static void main(String[] args) {
		List<FrontResourceHandle> handles = new ArrayList<FrontResourceHandle>();
		com.qx.front.base.BaseFront.LOADER.register(handles);
		handles.forEach(h->System.out.println(h));
	}

}
