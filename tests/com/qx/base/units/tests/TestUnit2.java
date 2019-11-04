package com.qx.base.units.tests;

import java.util.regex.Matcher;

import com.qx.level0.utilities.units.SI_Unit;

public class TestUnit2 {

	public static void main(String[] args) {
		
		
		String input = "mmHg.kJ.g-1.A2.cSt-3.BTU-2.muPa-2.T-1.kmol-2";
		
		// input = "W.m-2.K-2.A-4";
		
		Matcher matcher = SI_Unit.PATTERN.matcher(input);
		boolean isContinuing = matcher.find();
		while(isContinuing){
			System.out.print("expression: "+matcher.group(0)+", ");
			System.out.print("unprefixable: "+matcher.group(1)+", ");
			System.out.print("prefix: "+matcher.group(2)+", ");
			System.out.print("unit: "+matcher.group(3)+", ");
			System.out.print("exponent: "+matcher.group(4)+"\n");
			isContinuing = matcher.find();
		}
	}

}
