package com.qx.back.base.io.csv.mapped;

import java.lang.reflect.Field;

import com.qx.back.base.io.units.QxScientificUnit;


public abstract class FieldMapping implements Getter, Setter {
	
	
	/**
	 * 
	 * @param field
	 * @return
	 */
	public static FieldMapping build(Field field){
		Class<?> type = field.getType();
		if(type==boolean.class){
			return new BooleanFieldMapping(field);
		}
		else if(type==short.class){
			return new ShortFieldMapping(field);
		}
		else if(type==int.class){
			return new IntegerFieldMapping(field);
		}
		else if(type==long.class){
			return new LongFieldMapping(field);
		}
		else if(type==float.class){
			return new FloatFieldMapping(field);
		}
		else if(type==double.class){
			return new DoubleFieldMapping(field);
		}
		else if(type==String.class){
			return new StringFieldMapping(field);
		}
		else {
			throw new RuntimeException("Type is not primitive : "+type.getName());
		}
	}
	

	protected Field field;
	
	private FieldMapping(Field field) {
		super();
		this.field = field;
	}
	
	
	public static class DoubleFieldMapping extends FieldMapping {

		
		public DoubleFieldMapping(Field field) {
			super(field);
		}

		@Override
		public void set(String value, Object object, QxScientificUnit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
			field.setDouble(object, unit.toIS(Double.valueOf(value)));
		}

		@Override
		public String get(Object object, QxScientificUnit unit)
				throws IllegalArgumentException, IllegalAccessException {
			return Double.toString(unit.fromIS(field.getDouble(object)));
		}
	}
	
	
	public static class FloatFieldMapping extends FieldMapping {
		
		public FloatFieldMapping(Field field) {
			super(field);
		}

		@Override
		public void set(String value, Object object, QxScientificUnit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
			field.setFloat(object, (float) unit.toIS(Float.valueOf(value)));
		}

		@Override
		public String get(Object object, QxScientificUnit unit)
				throws IllegalArgumentException, IllegalAccessException {
			return Double.toString(unit.fromIS(field.getFloat(object)));
		}
	}
	
	public static class IntegerFieldMapping extends FieldMapping {

		public IntegerFieldMapping(Field field) {
			super(field);
		}

		@Override
		public void set(String value, Object object, QxScientificUnit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
			field.setInt(object, Integer.valueOf(value));
		}

		@Override
		public String get(Object object, QxScientificUnit unit)
				throws IllegalArgumentException, IllegalAccessException {
			return Integer.toString(field.getInt(object));
		}
	}
	
	public static class LongFieldMapping extends FieldMapping {

		public LongFieldMapping(Field field) {
			super(field);
		}

		@Override
		public void set(String value, Object object, QxScientificUnit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
			field.setLong(object, Long.valueOf(value));
		}

		@Override
		public String get(Object object, QxScientificUnit unit)
				throws IllegalArgumentException, IllegalAccessException {
			return Long.toString(field.getLong(object));
		}
	}
	
	public static class ShortFieldMapping extends FieldMapping {

		public ShortFieldMapping(Field field) {
			super(field);
		}

		@Override
		public void set(String value, Object object, QxScientificUnit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
			field.setShort(object, Short.valueOf(value));
		}

		@Override
		public String get(Object object, QxScientificUnit unit)
				throws IllegalArgumentException, IllegalAccessException {
			return Short.toString(field.getShort(object));
		}
	}
	
	public static class BooleanFieldMapping extends FieldMapping {

		public BooleanFieldMapping(Field field) {
			super(field);
		}

		@Override
		public void set(String value, Object object, QxScientificUnit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
			field.setBoolean(object, Boolean.valueOf(value));
		}

		@Override
		public String get(Object object, QxScientificUnit unit)
				throws IllegalArgumentException, IllegalAccessException {
			return Boolean.toString(field.getBoolean(object));
		}
	}
	
	public static class StringFieldMapping extends FieldMapping {

		public StringFieldMapping(Field field) {
			super(field);
		}

		@Override
		public void set(String value, Object object, QxScientificUnit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
			field.set(object, value);
		}

		@Override
		public String get(Object object, QxScientificUnit unit)
				throws IllegalArgumentException, IllegalAccessException {
			return (String) field.get(object);
		}
	}
}
