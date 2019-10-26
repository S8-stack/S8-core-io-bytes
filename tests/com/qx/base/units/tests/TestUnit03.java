package com.qx.base.units.tests;

import com.qx.base.units.SI_Unit;
import com.qx.base.units.SI_Unit.WrongUnitFormat;

public class TestUnit03 {

	public static void main(String[] args) throws WrongUnitFormat {
		
		// input = "W.m-2.K-2.A-4";
		SI_Unit unit = new SI_Unit("J.kg-1.mol-2");
		
		System.out.println(unit.convert(1.0));
	}

}
