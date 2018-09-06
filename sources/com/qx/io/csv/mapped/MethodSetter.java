package com.qx.io.csv.mapped;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import com.qx.io.units.Unit;


public abstract class MethodSetter implements Setter {
	
	
	/**
	 * 
	 * @param field
	 * @return
	 */
	public static MethodSetter build(Method method){
		Parameter[] parameters = method.getParameters();
		if(parameters.length!=1){
			return null;
			//throw new RuntimeException("Illegal number of paramters for method:"+method.getName());
		}

		Class<?> type = parameters[0].getType();
		if(type==boolean.class){
			return new BooleanMethodSetter(method);
		}
		else if(type==short.class){
			return new ShortMethodSetter(method);
		}
		else if(type==int.class){
			return new IntegerMethodSetter(method);
		}
		else if(type==long.class){
			return new LongMethodSetter(method);
		}
		else if(type==float.class){
			return new FloatMethodSetter(method);
		}
		else if(type==double.class){
			return new DoubleMethodSetter(method);
		}
		else if(type==String.class){
			return new StringMethodSetter(method);
		}
		else {
			//throw new RuntimeException("Type is not primitive : "+type.getName());
			return null;
		}
	}
	

	protected Method method;
	
	private MethodSetter(Method method) {
		super();
		this.method = method;
	}
	
	
	public static class DoubleMethodSetter extends MethodSetter {

		public DoubleMethodSetter(Method method) {
			super(method);
		}

		@Override
		public void set(String value, Object object, Unit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			method.invoke(object, unit.toIS(Double.valueOf(value)));
		}
	}
	
	public static class FloatMethodSetter extends MethodSetter {

		public FloatMethodSetter(Method method) {
			super(method);
		}

		@Override
		public void set(String value, Object object, Unit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			method.invoke(object, (float) unit.toIS(Double.valueOf(value)));
		}
	}
	
	public static class ShortMethodSetter extends MethodSetter {

		public ShortMethodSetter(Method method) {
			super(method);
		}

		@Override
		public void set(String value, Object object, Unit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			method.invoke(object, Short.valueOf(value));
		}
	}
	
	public static class IntegerMethodSetter extends MethodSetter {

		public IntegerMethodSetter(Method method) {
			super(method);
		}

		@Override
		public void set(String value, Object object, Unit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			method.invoke(object, Integer.valueOf(value));
		}
	}
	
	public static class LongMethodSetter extends MethodSetter {

		public LongMethodSetter(Method method) {
			super(method);
		}

		@Override
		public void set(String value, Object object, Unit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			method.invoke(object, Long.valueOf(value));
		}
	}
	
	public static class BooleanMethodSetter extends MethodSetter {

		public BooleanMethodSetter(Method method) {
			super(method);
		}

		@Override
		public void set(String value, Object object, Unit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			method.invoke(object, Boolean.valueOf(value));
		}
	}
	
	public static class StringMethodSetter extends MethodSetter {

		public StringMethodSetter(Method method) {
			super(method);
		}

		@Override
		public void set(String value, Object object, Unit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			method.invoke(object, value);
		}
	}
	
	
}
