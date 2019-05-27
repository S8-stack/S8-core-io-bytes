package com.qx.back.base.test.back.io.units;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestUnit {

	public static void main(String[] args) {
		String regex = "([mcdkMG])?([a-zA-Z]*)(-?[1-9])?";
		Pattern pattern = Pattern.compile(regex);
		String input = "kW2";
		Matcher matcher = pattern.matcher(input);
		System.out.println(matcher.matches());
		int gc = matcher.groupCount();
		for(int i=0; i<gc+1; i++){
			System.out.println(matcher.group(i));
		}
	}

}
