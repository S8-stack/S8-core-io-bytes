package com.qx.web.sources.tests;

import com.qx.web.mime.MIME_Type;

public class TestMIME {

	public static void main(String[] args) {

		System.out.println(MIME_Type.get("application/octet-stream"));
		System.out.println(MIME_Type.find("my/package/toto-zieu_978.left.js"));
		System.out.println(MIME_Type.find("my/wengl/package/skycube-mountain.left.png"));
		
	}

}
