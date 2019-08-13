package com.qx.back.base.test.resources;

import java.util.regex.Pattern;

public class TestingRegex02 {

	public static void main(String[] args) {
		Pattern pattern = Pattern.compile(
				"(control/[/\\w-]*|"
				+ "graphics/[/\\w-]*|"
				+ "maths/[/\\w-]*|"
				+ "objects/[/\\w-]*|"
				+ "render/[/\\w-]*|" 
				+ "shapes/[/\\w-]*|"
				+ "util/[/\\w-]*|"
				+ "view/[/\\w-]*|"
				+ "[\\w-]*)"
				+ "\\.(js|png|jpg)");
		
		
		
		
		pattern = Pattern.compile(
				"(toto/[/\\w-]*|util/[/\\w-]*)\\.(js|png|jpg)");
		
		System.out.println(pattern.matcher("util/STRUCT_MapChain.js").matches());
	}

}
