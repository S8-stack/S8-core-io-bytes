package com.qx.base.units;

import java.util.HashMap;
import java.util.Map;


public enum QxScientificUnitPrefix {

	PICO("p", 1e-12),
	NANO("n", 1e-9),
	MICRO("mu", 1e-6),
	MILLI("m", 1e-3),
	CENTI("c", 1e-2),
	DECI("d", 1e-1),
	DECA("da", 1e1),
	HECTO("h", 1e2),
	KILO("k", 1e3),
	MEGA("M", 1e6),
	GIGA("G", 1e9),
	TERA("T", 1e12),
	PETA("P", 1e15);

	public String abbreviation;

	public double scaling;

	private QxScientificUnitPrefix(String abbreviation, double scaling) {
		this.abbreviation = abbreviation;
		this.scaling = scaling;
	}


	public static QxScientificUnitPrefix get(String abbreviation){
		return map.get(abbreviation);
	}

	public final static String REGEX;

	private final static Map<String, QxScientificUnitPrefix> map;

	static {

		// build map and regex
		map = new HashMap<>();

		boolean isStarted = false;
		StringBuilder builder = new StringBuilder();
		builder.append("(");

		for(QxScientificUnitPrefix prefix : QxScientificUnitPrefix.values()){
			map.put(prefix.abbreviation, prefix);

			if(isStarted){
				builder.append('|');
			}
			else{
				isStarted = true;
			}
			builder.append(prefix.abbreviation);
		}

		builder.append(")?");
		REGEX = builder.toString();
	}

}