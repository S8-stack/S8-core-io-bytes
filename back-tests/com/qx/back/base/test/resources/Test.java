package com.qx.back.base.test.resources;

import java.nio.ByteBuffer;

import com.qx.back.base.resources.FrontResource;
import com.qx.back.base.resources.FrontResourceBase;
import com.qx.front.base.BaseFront;


public class Test {

	public static void main(String[] args) {
		FrontResourceBase base = new FrontResourceBase();
		base.initialize(BaseFront.LOADER);
		base.traverse((p,r)->System.out.println(p));
		FrontResource r = base.get("/base/large-pic.jpg");
		System.out.println("loaded: "+r.getHead());
		
		ByteBuffer buffer = r.getHead().flatten();
		System.out.println("bytecount: "+buffer.position());
		
		
	}

}
