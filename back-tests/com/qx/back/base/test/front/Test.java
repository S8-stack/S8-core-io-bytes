package com.qx.back.base.test.front;

import com.qx.back.base.resources.FrontResourceBase;
import com.qx.front.base.BaseFront;


public class Test {

	public static void main(String[] args) {
		FrontResourceBase base = new FrontResourceBase();
		base.initialize(BaseFront.LOADER);
		base.traverse((p,r)->System.out.println(p));
	}

}
