package com.qx.back.base.io.csv.mapped;

import java.lang.reflect.Field;

import com.qx.back.base.enumerables.QxEnumerable;


public abstract class FieldMapping implements Getter, Setter {


	/**
	 * 
	 * @param field
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	public static FieldMapping build(Field field) 
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
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
		else if(type.isEnum()){
			return new EnumFieldMapping(field);
		}
		else if(QxEnumerable.class.isAssignableFrom(type)) {
			return new QxEnumerableFieldMapping(field);
		}
		else {
			throw new RuntimeException("Type is not primitive : "+type.getName());
		}
	}


	protected Field field;

	public FieldMapping(Field field) {
		super();
		this.field = field;
	}
	
	
}
