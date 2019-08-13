package com.qx.base.units;

import java.util.HashMap;
import java.util.Map;


public enum QxScientificUnitBase {

	// length
	METER("m", 1.0),

	// angle
	RADIAN("rad", 1.0),
	DEGREE("deg", Math.PI/180.0),

	// temperature
	KELVIN("K", 1.0),
	CELCIUS_DEGREE("degC", 1.0),

	// pressure
	PASCAL("Pa", 1.0),
	BAR("bar", 1e5),

	// weight
	GRAM("g", 1e-3),
	TON("T", 1e3),
	
	// Force
	NEWTON("N", 1.0),

	// Ampere
	AMPERE("A", 1.0),

	// Ampere
	VOLT("V", 1.0),

	// Volt-Ampere
	VOLT_AMPERE("VA", 1.0),

	// power
	WATT("W", 1.0),

	// energy
	JOULE("J", 1.0),
	WATT_HOUR("Wh", 3600.0),

	// viscosity
	STOCKES("St", 1e-4),

	// molar
	MOLE("mol", 1.0),
	
	// time
	SECOND("s", 1.0),
	MIN("min", 1.0),
	HOUR("h", 1.0);

	public String abbreviation;

	public double scaling;

	private QxScientificUnitBase(String abbreviation, double scaling) {
		this.abbreviation = abbreviation;
		this.scaling = scaling;
	}


	public static QxScientificUnitBase get(String abbreviation){
		return map.get(abbreviation);
	}

	public final static String REGEX;

	private final static Map<String, QxScientificUnitBase> map;

	
	static {

		// build map and regex
		map = new HashMap<>();

		boolean isStarted = false;
		StringBuilder builder = new StringBuilder();
		builder.append("(");

		for(QxScientificUnitBase prefix : QxScientificUnitBase.values()){
			map.put(prefix.abbreviation, prefix);

			if(isStarted){
				builder.append('|');
			}
			else{
				isStarted = true;
			}
			builder.append(prefix.abbreviation);
		}

		builder.append(")");
		REGEX = builder.toString();
	}

}