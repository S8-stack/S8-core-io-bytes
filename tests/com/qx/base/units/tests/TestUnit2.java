package com.qx.base.units.tests;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestUnit2 {

	public static void main(String[] args) {
		
		Pattern pattern = Pattern.compile("(p|n|mu|m|c|d|da|h|k|M|G|T|P])?([a-zA-Z]+)(-?[1-9])?(\\.)?");
		
		String input = "kJ.g-1.A2.cSt-3.BTU-2.muPa-2.TO-1.kmol-2";
		
		// input = "W.m-2.K-2.A-4";
		
		Matcher matcher = pattern.matcher(input);
		boolean isContinuing = true;
		while(isContinuing && matcher.find()){
			System.out.print("Expression: "+matcher.group(0)+", ");
			System.out.print("prefix: "+matcher.group(1)+", ");
			System.out.print("unit: "+matcher.group(2)+", ");
			System.out.print("exponent: "+matcher.group(3)+"\n");
			isContinuing = (matcher.group(4)!=null);
		}
	}

}
