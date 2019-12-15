package com.qx.level0.utilities.units;

import java.util.HashMap;
import java.util.Map;


public enum SI_BaseUnit {
	
	
	NONE("-", false, 1, 0),

	// pressure
	PASCAL("Pa", true, 1, 0),

	// bar
	ABSOLUTE_BAR("bara", true, 1e5, 0),
	ABSOLUTE_GAUGE("barg", true, 1e5, 1e5),
	BAR("bar", true, 1e5, 0),

	// PSI (prevent psig to be interpreted as "ps..." -> pico seconds
	PSIA("PSIA", true, 6894.7572931783, 0),
	PSIG("PSIG", true, 6894.7572931783, 1e5),
	PSI("PSI", true, 6894.7572931783, 0),
	MILLIMETER_OF_MERCURY("mmHg", false, 133.322387415, 0.0),
	// order important for capturing
	INCH_OF_MERCURY_VACCUM("inHgVac", false, -3386.389, 1.0e5),
	INCH_OF_MERCURY("inHg", false, 3386.389, 0.0),

	// molar (placed first since 'mol' beginning is the same as 'm' meter
	MOLE("mol", true, 1.0, 0),


	// length
	METER("m", true, 1, 0),
	INCH("in", false, 0.0254, 0),

	// angle
	RADIAN("rad", true, 1, 0),
	DEGREE("deg", true, Math.PI/180.0, 0),

	// temperature
	KELVIN("K", true, 1.0, 0.0),
	CELCIUS_DEGREE("°C", false, 1, 273.15),
	FAHRENHEIT_DEGREE_PROPORTIONAL("°F_", false, 5.0/9.0, 0.0),
	FAHRENHEIT_DEGREE("°F", false, 5.0/9.0, 255.37222222),
	



	// weight
	GRAM("g", true, 1e-3, 0),
	TON("T", true, 1e3, 0),
	POUND("lb", false, 0.45359237, 0),

	// Force
	NEWTON("N", true, 1, 0),

	// Ampere
	AMPERE("A", true, 1, 0),

	// Ampere
	VOLT("V", true, 1, 0),

	// Volt-Ampere
	VOLT_AMPERE("VA", true, 1, 0),

	// power
	WATT("W", true, 1, 0),
	HORSE_POWER("HP", false, 745.699872, 0.0),

	// energy
	JOULE("J", true, 1, 0),
	WATT_HOUR("Wh", true, 3600, 0),
	/** American defintion */
	AMERICAN_BTU("BTU", true, 1054.804, 0),

	// viscosity
	STOCKES("St", true, 1e-4, 0),

	// time
	SECOND("s", true, 1, 0),
	MIN("min", false, 60, 0),
	HOUR("h", false, 3600, 0),

	// rotational speed
	REVOLUTIONS_PER_MINUTE("rpm", true, 2.0*Math.PI/60, 0);

	public String abbreviation;


	public boolean isPrefixable;


	/**
	 * multiply by this factor to convert back to SI Unit
	 */
	public double factor;


	/**
	 * add this offset <b>after</b> multiplying by factor to convert back to SI Unit 
	 */
	public double offset;




	private SI_BaseUnit(String abbreviation, boolean isPrefixable, double scaling, double offset) {
		this.abbreviation = abbreviation;
		this.isPrefixable = isPrefixable;
		this.factor = scaling;
		this.offset = offset;
	}


	public static SI_BaseUnit get(String abbreviation){
		return map.get(abbreviation);
	}

	public final static String PREFIXABLES_REGEX;
	public final static String UNPREFIXABLES_REGEX;

	private final static Map<String, SI_BaseUnit> map;


	static {

		// build map and regex
		map = new HashMap<>();

		boolean isPrefixablesRegexStarted = false;
		StringBuilder prefixablesRegexBuilder = new StringBuilder();
		prefixablesRegexBuilder.append("(");

		boolean isUnprefixablesRegexStarted = false;
		StringBuilder unprefixablesRegexBuilder = new StringBuilder();
		unprefixablesRegexBuilder.append("(");

		for(SI_BaseUnit base : SI_BaseUnit.values()){
			map.put(base.abbreviation, base);

			if(base.isPrefixable) {
				if(isPrefixablesRegexStarted){
					prefixablesRegexBuilder.append('|');
				}
				else{
					isPrefixablesRegexStarted = true;
				}
				prefixablesRegexBuilder.append(base.abbreviation);	
			}
			else {
				if(isUnprefixablesRegexStarted){
					unprefixablesRegexBuilder.append('|');
				}
				else{
					isUnprefixablesRegexStarted = true;
				}
				unprefixablesRegexBuilder.append(base.abbreviation);	
			}
		}

		prefixablesRegexBuilder.append(")");
		PREFIXABLES_REGEX = prefixablesRegexBuilder.toString();

		unprefixablesRegexBuilder.append(")");
		UNPREFIXABLES_REGEX = unprefixablesRegexBuilder.toString();
	}

}