package com.qx.base.units;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QxScientificUnit {

	public final static Pattern PATTERN;
	
	static{
		String REGEX = QxScientificUnitPrefix.REGEX+QxScientificUnitBase.REGEX+"(-?[1-9])?(\\.)?";
		PATTERN = Pattern.compile(REGEX);
	}
	
	private String abbreviation;
	
	private double scaling;
	
	public QxScientificUnit(String abbreviation){
		super();
		
		this.abbreviation = abbreviation;
		
		scaling = 1.0;
		
		Matcher matcher = PATTERN.matcher(abbreviation);
		boolean isContinuing = true;
		
		QxScientificUnitPrefix prefix;
		QxScientificUnitBase base;
		String exponantEncoding;
		int exponant;
		
		double groupScaling;
		
		while(isContinuing && matcher.find()){
			groupScaling = 1.0;
			//System.out.print("Expression: "+matcher.group(0)+", ");
			//System.out.print("prefix: "+matcher.group(1)+", ");
			prefix = QxScientificUnitPrefix.get(matcher.group(1));
			if(prefix!=null){
				groupScaling*=prefix.scaling;
			}
			//System.out.print("unit: "+matcher.group(2)+", ");
			base = QxScientificUnitBase.get(matcher.group(2));
			if(base!=null){
				groupScaling*=base.scaling;
			}
			//System.out.print("exponent: "+matcher.group(3)+"\n");
			exponantEncoding = matcher.group(3);
			if(exponantEncoding!=null){
				exponant = Integer.parseInt(exponantEncoding);
				groupScaling = Math.pow(groupScaling, exponant);
			}
			scaling*=groupScaling;
			isContinuing = (matcher.group(4)!=null);
		}
	};
	
	
	public String getAbbreviation(){
		return abbreviation;
	}
	
	
	public double toIS(double value){
		return value*scaling;
	}
	
	public double fromIS(double value){
		return value/scaling;
	}

}
