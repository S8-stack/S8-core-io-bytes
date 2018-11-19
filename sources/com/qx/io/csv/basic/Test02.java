package com.qx.io.csv.basic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test02 {

	public static void main(String[] args) {
		String tag = "id";
		//String regex = "([a-zA-Z0-9\\-_]+) *((\\[[a-zA-Z0-9\\.\\-]\\])?)";
		Pattern pattern = Pattern.compile(CSV_BasicReader.TAG_REGEX);
		Matcher matcher = pattern.matcher(tag);
		System.out.println(matcher.find());
		System.out.println(matcher.group(1));
		System.out.println(matcher.group(2));
		System.out.println(matcher.group(3));
		
	}

}
