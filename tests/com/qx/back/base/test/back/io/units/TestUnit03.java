package com.qx.back.base.test.back.io.units;

import com.qx.back.base.io.units.QxScientificUnit;

public class TestUnit03 {

	public static void main(String[] args) {
		
		// input = "W.m-2.K-2.A-4";
		QxScientificUnit unit = new QxScientificUnit("J.kg-1.mol-2");
		
		System.out.println(unit.fromIS(1.0));
	}

}
