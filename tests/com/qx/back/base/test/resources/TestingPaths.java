package com.qx.back.base.test.resources;

import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.qx.base.resources.WebResourceFilter;


public class TestingPaths {

	public static void main(String[] args) {

		String[] paths = {
				"/qx-base/front-sources/com/qx/front/base/large-pic.jpg",
				"/qx-base/back-sources/com/qx/front/base/large-pic.jpg",
				"/qx-base/back-sources/com/qx/front/base/pic.png",
				"/qx-base/net-sources/com/qx/front/base/large-pic.js"
		};


		//String regex = "/qx-base/(back|net)-sources/com/qx/front/base/.*\\.(js|png)";
		String regex2 = ".*\\.(js|jpg)";

		Pattern pattern1 = Pattern.compile(regex2);
		System.out.println("Pattern 1:");		
		for(String path : paths) {
			Matcher matcher = pattern1.matcher(path);
			System.out.println((matcher.find()?"PASSED":"FAILED")+": "+path);		
		}


		String gpp = "*/{back,net}-$/*.{js,png}";
		String regex3 = WebResourceFilter.preprocessRegex(gpp);
		Pattern pattern2 = Pattern.compile(regex3);
		System.out.println("Pattern 2:");	
		for(String path : paths) {
			Matcher matcher = pattern2.matcher(path);
			System.out.println((matcher.find()?"PASSED":"FAILED")+": "+path);		
		}

		System.out.println(Paths.get(TestingPaths.class.getResource("").getPath()));
	}


	public final static String preprocessRegex(String regex) {
		regex = regex.replace("**", "[/\\w-]+");
		regex = regex.replace("*", "[\\w-]+");
		regex = regex.replace(".", "\\.");
		regex = regex.replace("{", "(");
		regex = regex.replace("}", ")");
		regex = regex.replace(",", "|");
		return regex;
	}

}
