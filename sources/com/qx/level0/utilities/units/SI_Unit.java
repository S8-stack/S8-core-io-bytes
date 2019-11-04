package com.qx.level0.utilities.units;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * <p>
 * The International System of Units (SI, abbreviated from the French Système
 * international (d'unités)) is the modern form of the metric system and is the
 * most widely used system of measurement. It comprises a coherent system of
 * units of measurement built on seven base units, which are:
 * </p>
 * <ul>
 * <li><b>s</b>: second</li>
 * <li><b>m</b>: metre</li>
 * <li><b>kg</b>: kilogram</li>
 * <li><b>A</b>: ampere</li>
 * <li><b>K</b>: kelvin</li>
 * <li><b>mol</b>: mole</li>
 * <li><b>cd</b>: candela</li>
 * <li>and a set of twenty prefixes to the
 * unit names and unit symbols that may be used when specifying multiples and
 * fractions of the units</li>
 * <li>The system also specifies names for 22 derived units,
 * such as lumen and watt, for other common physical quantities</li>
 * </ul>
 * 
 * @author pc
 *
 */
public class SI_Unit {
	
	public class WrongUnitFormat extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8480686215239790433L;
		
	
		public WrongUnitFormat(String abbreviation, String group) {
			super("Failed to parse the following group: "+group+", withing the following unit:"+abbreviation);
		}
	}
	
	
	public final static Pattern DOUBLE_PATTERN = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");

	public final static Pattern PATTERN;
	
	static{
		String REGEX = 
				"(?:"+SI_BaseUnit.UNPREFIXABLES_REGEX+"(-?[1-9])?)"
				+"|(?:"+SI_UnitPrefix.REGEX+'?'+SI_BaseUnit.PREFIXABLES_REGEX+"(-?[1-9])?(\\.)?)";
		PATTERN = Pattern.compile(REGEX);
	}
	
	private String abbreviation;
	
	private double factor;
	
	private double offset;
	
	public SI_Unit(String abbreviation) throws WrongUnitFormat {
		super();
		this.abbreviation = abbreviation;
		Matcher matcher = PATTERN.matcher(abbreviation);
		
		factor = 1.0;
		offset = 0.0;
		
		
		SI_UnitPrefix prefix;
		SI_BaseUnit base;
		String exponantEncoding;
		int exponant=1;
		
		double groupFactor=1.0, groupOffset=0.0;
		int position=0;
		String unprefixable;
		
		boolean isContinuing = matcher.find();
		while(isContinuing){
			
			//System.out.print("Expression: "+matcher.group(0)+", ");
			//System.out.print("prefix: "+matcher.group(1)+", ");
			
			if((unprefixable=matcher.group(1))!=null) {
				base = SI_BaseUnit.get(unprefixable);
				if(base==null) {
					throw new WrongUnitFormat(abbreviation, matcher.group(0));
				}
				groupFactor = base.factor;
				groupOffset = base.offset;
				
				exponant = 1;
				exponantEncoding = matcher.group(2);
				if(exponantEncoding!=null){
					exponant = Integer.parseInt(exponantEncoding);
					groupFactor = Math.pow(groupFactor, exponant);
				}
			}
			else { // prefixable
				groupFactor = 1.0;
				groupOffset = 0.0;
				
				prefix = SI_UnitPrefix.get(matcher.group(3));
				if(prefix!=null){
					groupFactor*=prefix.scaling;
				}
				//System.out.print("unit: "+matcher.group(2)+", ");
				base = SI_BaseUnit.get(matcher.group(4));
				if(base!=null){
					groupFactor*=base.factor;
					groupOffset = base.offset;
				}
				//System.out.print("exponent: "+matcher.group(3)+"\n");
				exponant = 1;
				exponantEncoding = matcher.group(5);
				if(exponantEncoding!=null){
					exponant = Integer.parseInt(exponantEncoding);
					groupFactor = Math.pow(groupFactor, exponant);
				}
			}
			
			// aggregate
			factor*=groupFactor;
			
			// handle offset
			if(position==0 && exponant==1) {
				offset=groupOffset;	
			}
			position++;
			
			isContinuing = matcher.find();
		}
	}
	
	
	public String getAbbreviation(){
		return abbreviation;
	}
	
	
	public double convertBack(double value){
		return value*factor+offset;
	}
	
	public double convert(double value){
		return (value-offset)/factor;
	}
	
	
	public static double read(String value) {
		Matcher m = DOUBLE_PATTERN.matcher(value);
		boolean isFound = m.find();
		if(!isFound) {
			throw new RuntimeException("Failed to read number");
		}
		double number = Double.valueOf(m.group());
		
		try {
			SI_Unit unit = new SI_Unit(value.substring(m.end()).trim());
			return unit.convertBack(number);
		} 
		catch (WrongUnitFormat e) {
			e.printStackTrace();
		}
		return 0.0;
		
	}

}
