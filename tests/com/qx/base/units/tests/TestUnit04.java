package com.qx.base.units.tests;

import com.qx.level0.utilities.units.SI_Unit;
import com.qx.level0.utilities.units.SI_Unit.WrongUnitFormat;

public class TestUnit04 {

	public static void main(String[] args) throws WrongUnitFormat {
		
		// input = "W.m-2.K-2.A-4";
		SI_Unit unit = new SI_Unit("BTU.kg2.m-1.°F-1");
		
		System.out.println(unit.convert(1.0));
		
		unit = new SI_Unit("°F");
		SI_Unit unit2 = new SI_Unit("°C");
		
		System.out.println(unit2.convert(unit.convertBack(57.0)));
		
		unit = new SI_Unit("mmHg.mm-1");
		System.out.println(unit.convertBack(1.0));
		

		System.out.println(SI_Unit.read("10inHgVac"));
		
	}

}
