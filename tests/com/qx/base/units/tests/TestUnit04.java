package com.qx.base.units.tests;

import com.qx.base.units.QxScientificUnit;

public class TestUnit04 {

	public static void main(String[] args) {
		
		// input = "W.m-2.K-2.A-4";
		QxScientificUnit unit = new QxScientificUnit("kg.m-1");
		
		System.out.println(unit.fromIS(1.0));
	}

}
