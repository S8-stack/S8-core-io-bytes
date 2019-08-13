package com.qx.base.units;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public enum Unit {

	// Special
	Adimensional(new IdentityConverter(), "-", new DecimalFormat("0.##")),
	Percentage(new ScalingConverter(1e-2), "%", new DecimalFormat("0.##")),
	
	// Dimensions
	Meter(new IdentityConverter(), "m", new DecimalFormat("0.##")),
	Decimeter(new ScalingConverter(1e-1), "dm", new DecimalFormat("0.##")),
	Centimeter(new ScalingConverter(1e-2), "cm", new DecimalFormat("0.##")),
	Millimeter(new ScalingConverter(1e-3), "mm", new DecimalFormat("0.##")),
	Kilometer(new ScalingConverter(1e3), "km", new DecimalFormat("0.##")),
	
	// Areas
	SquareMeters(new IdentityConverter(), "m2", new DecimalFormat("0.##E0")),
	SquareMillimeters(new ScalingConverter(1e-6), "mm2", new DecimalFormat("0.##")),
	
	// Rotational speed
	RotationsPerMinute(new ScalingConverter(2*Math.PI/60), "rpm", new DecimalFormat("0.##")),
	RadianPerSecond(new IdentityConverter(), "rad.s-1", new DecimalFormat("0.##")),
	
	// Angles
	Degree(new ScalingConverter(Math.PI/180.0), "deg", new DecimalFormat("0.##")),
	Radian(new IdentityConverter(), "rad", new DecimalFormat("0.##")),
	
	// Temperature
	Kelvin(new IdentityConverter(), "K", new DecimalFormat("0.##")),
	Celsius(new CelsiusConverter(), "degC", new DecimalFormat("0.##")),
	
	// Pressure
	Pascal(new IdentityConverter(), "Pa", new DecimalFormat("#,##0")),
	KiloPascals(new ScalingConverter(1000), "kPa", new DecimalFormat("#,##0.#")),
	Bar(new ScalingConverter(100000), "bar", new DecimalFormat("0.####")),
	MegaPascals(new ScalingConverter(1000000), "MPa", new DecimalFormat("0.###")),

	// Density
	KilogramsPerCubeMeters(new IdentityConverter(), "kg.m-3", new DecimalFormat("#,##0.##")),
	KilogramsPerCubeDecimeters(new ScalingConverter(1000), "kg.dm-3", new DecimalFormat("#,##0.##")),
	
	// Volume
	CubeMetersPerKilograms(new IdentityConverter(), "m3.kg-1", new DecimalFormat("0.##E0")),
	CubeDecimetersPerKilograms(new ScalingConverter(1e-3), "dm3.kg-1", new DecimalFormat("0.##E0")),
	
	// Enthalpy
	JoulePerKilogram(new IdentityConverter(), "J.kg-1", new DecimalFormat("#,##0")),
	KiloJoulesPerKilogram(new ScalingConverter(1000), "kJ.kg-1", new DecimalFormat("0.##")),
	
	// Entropy, Specific heat
	JoulePerKilogramPerKelvin(new IdentityConverter(), "J.kg-1.K-1", new DecimalFormat("#,##0.##")),
	KiloJoulesPerKilogramPerKelvin(new ScalingConverter(1000), "kJ.kg-1.K-1", new DecimalFormat("0.####")),
	
	// Speed of Sound
	MetersPerSecond(new IdentityConverter(), "m.s-1", new DecimalFormat("#,##0.##")),
	KilometersPerHour(new ScalingConverter(1.0/3.6), "km.h-1", new DecimalFormat("0.####")),
	
	// Thermal conductivity
	WattPerMeterPerKelvin(new IdentityConverter(), "W.m-1.K-1", new DecimalFormat("#,##0.###")),
	MilliwattPerMeterPerKelvin(new ScalingConverter(1e-3), "mW.m-1.K-1", new DecimalFormat("0.##")),
	
	
	// Heat transfer coefficient
	WattPerSquareMetersPerKelvin(new IdentityConverter(), "W.m-2.K-1", new DecimalFormat("0.##")),
	
	// Thermal resistance
	KelvinPerWatt(new IdentityConverter(), "K.W-1", new DecimalFormat("0.##E0")),
	
	// Thermal resistivity
	KelvinSquareMetersPerWatt(new IdentityConverter(), "K.m2.W-1", new DecimalFormat("0.##E0")),
	
	// Thermal diffusivity, Kinematic viscosity
	SquareMetersPerSecond(new IdentityConverter(), "m2.s-1", new DecimalFormat("0.##E0")),
	Stockes(new ScalingConverter(1e-4), "St", new DecimalFormat("0.##E0")),
	CentiStockes(new ScalingConverter(1e-6), "cSt", new DecimalFormat("0.##E0")),
	
	// Dynamic viscosity
	KilogramsPerMeterPerSecond(new IdentityConverter(), "kg.m-1.s-1", new DecimalFormat("0.##E0")),
	PascalSecond(new IdentityConverter(), "Pa.s", new DecimalFormat("0.##E0")),
	MilliPascalSecond(new ScalingConverter(1e-3), "mPa.s", new DecimalFormat("#,##0.##")),
	MicroPascalSecond(new ScalingConverter(1e-6), "uPa.s", new DecimalFormat("0.##E0")),
	
	// Mass 
	Ton(new ScalingConverter(1e3), "T", new DecimalFormat("#,##0.##")),
	Kilogram(new IdentityConverter(), "kg", new DecimalFormat("#,##0.##")),
	Gram(new ScalingConverter(1e-3), "g", new DecimalFormat("#,##0.##")),
	
	// Mole
	Mole(new IdentityConverter(), "mol", new DecimalFormat("0.##")),
	
	// Molar mass
	GramPerMole(new ScalingConverter(1e-3), "g.mol-1", new DecimalFormat("0.##")),
	KilogramPerMole(new IdentityConverter(), "kg.mol-1", new DecimalFormat("0.##")),
	
	// Mass flow
	KilogramPerSecond(new IdentityConverter(), "kg.s-1", new DecimalFormat("#,##0.##")),
	
	// Volume flow
	CubeMetersPerSecond(new IdentityConverter(), "m3.s-1", new DecimalFormat("0.##E0")),
	NormCubeMetersPerSecond(new IdentityConverter(), "Nm3.s-1", new DecimalFormat("0.##E0")),
	CubeMetersPerHour(new ScalingConverter(1./3600), "m3.h-1", new DecimalFormat("0.##E0")),
	NormCubeMetersPerHour(new ScalingConverter(1./3600), "Nm3.h-1", new DecimalFormat("0.##E0")),
	
	// Force
	Newton(new IdentityConverter(), "N", new DecimalFormat("0.##")),
	KiloNewton(new ScalingConverter(1000), "kN", new DecimalFormat("0.##")),
	
	// Power
	Watt(new IdentityConverter(), "W", new DecimalFormat("0.##")),
	KiloWatt(new ScalingConverter(1000), "kW", new DecimalFormat("#,##0.##")),
	
	// Energy
	Joule(new IdentityConverter(), "J", new DecimalFormat("0.##")),
	KiloWattHour(new ScalingConverter(36e5), "kWh", new DecimalFormat("0.##")),
	MegaWattHour(new ScalingConverter(36e8), "MWh", new DecimalFormat("0.##")),
	
	// Time
	Second(new IdentityConverter(), "s", new DecimalFormat("0.##")),
	Minute(new ScalingConverter(60), "min", new DecimalFormat("0.##")),
	Hour(new ScalingConverter(3600), "h", new DecimalFormat("0.##")),
	Day(new ScalingConverter(86400), "d", new DecimalFormat("0.##")),
	Year(new ScalingConverter(3.156e+7), "y", new DecimalFormat("0.##")),
	
	// Money
	Euro(new IdentityConverter(), "€", new DecimalFormat("#,##0.##")),
	Dollar(new ScalingConverter(0.73), "$", new DecimalFormat("#,##0.##")),
	
	EuroPerWs(new IdentityConverter(), "€.Ws-1", new DecimalFormat("0.##")),
	EuroPerMWh(new ScalingConverter(1/36e8), "€.MWh-1", new DecimalFormat("0.##")), // /1e6/3600
	
	EuroPerSecond(new IdentityConverter(), "€.s-1", new DecimalFormat("0.##")),
	EuroPerYear(new ScalingConverter(1/3.156e7), "€.y-1", new DecimalFormat("0.##"));
	
	/*
	 * fields
	 */
	
	protected UnitConverter unitConverter;
	public String abbreviation;
	public DecimalFormat preferredDisplayFormat;
	
	
	/**
	 * @param unitConverter
	 */
	private Unit(UnitConverter unitConverter, String abbreviation, DecimalFormat preferredDisplayFormat) {
		this.unitConverter = unitConverter;
		this.abbreviation = abbreviation;
		this.preferredDisplayFormat = preferredDisplayFormat;
	}

	public double fromIS(double value){
		return unitConverter.fromIS(value);
	}

	public double toIS(double value){
		return unitConverter.toIS(value);
	}
	
	public static Unit get(String abbreviation){
		return units.get(abbreviation);
	}
	
	/*
	 * Converters
	 */
	
	/**
	 * read a value and convert it in IS
	 */
	public static double read(String measure){
		Matcher m = pattern2.matcher(measure);
		if(m.find()) {
			//System.out.println(Unit.get(m.group(2)).toString());
			return Unit.get(m.group(2)).toIS(Double.valueOf(m.group(1)));
		}
		else{
			throw new RuntimeException("Measure cannot be read (invalid format).");
		}
	}
	
	/**
	 * write an IS value in the unit.
	 * @param value_IS
	 * @return
	 */
	public String write(double value_IS){
		return preferredDisplayFormat.format(fromIS(value_IS))+" "+abbreviation;
	}
	
	/**
	 * 
	 * @param value_IS
	 * @param format
	 * @return
	 */
	public String write(double value_IS, DecimalFormat format){
		return format.format(fromIS(value_IS))+" "+abbreviation;
	}

	public interface UnitConverter {


		public double fromIS(double value);
		
		public double toIS(double value);	
		
	}
	
	public static class IdentityConverter implements UnitConverter{

		@Override
		public double fromIS(double value) {
			return value;
		}

		@Override
		public double toIS(double value) {
			return value;
		}
	}
	
	public static class ScalingConverter implements UnitConverter{

		private double scalingFactor;
		
		/**
		 * @param scalingFactor
		 */
		public ScalingConverter(double scalingFactor) {
			super();
			this.scalingFactor = scalingFactor;
		}

		@Override
		public double fromIS(double value) {
			return value/scalingFactor;
		}

		@Override
		public double toIS(double value) {
			return value*scalingFactor;
		}
	}

	public static class CelsiusConverter implements UnitConverter{
		// absolute zero in Celsius degrees
		public final static double absoluteZero = 273.15;

		@Override
		public double fromIS(double value) {
			return value-absoluteZero;
		}

		@Override
		public double toIS(double value) {
			return value+absoluteZero;
		}
	}
	
	/*
	 * static variables
	 */
	
	// absolute zero in Celsius degrees
	public final static double ABSOLUTE_ZERO = 273.15;

	// Matcher for reading measures
	public final static String regex = " ?([a-zA-Z]) ?= ?([0-9\\.\\-eE]*) ?([a-zA-Z�\\-1\\.]*) ?";
	public final static Pattern pattern = Pattern.compile(regex);
	
	public final static String regex2 = " ?([0-9\\.\\-eE]*) ?([a-zA-Z�\\-1-9\\.]*) ?";
	public final static Pattern pattern2 = Pattern.compile(regex2);
	
	protected static Map<String, Unit> units;
	
	static{
		units = new HashMap<String, Unit>();
		for(Unit unit : values()){
			units.put(unit.abbreviation, unit);
		}
	}
}
