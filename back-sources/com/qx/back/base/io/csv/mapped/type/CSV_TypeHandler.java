package com.qx.back.base.io.csv.mapped.type;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.qx.back.base.io.csv.mapped.CSV_Mapping;

/**
 * 
 * @author pc
 *
 * @param <T>
 */
public class CSV_TypeHandler {

	

	private Map<String, Getter> getters;

	private Map<String, Setter> setters;

	/**
	 * 
	 * @param type
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchFieldException 
	 */
	public CSV_TypeHandler(Class<?> type) 
			throws 
			NoSuchMethodException, 
			SecurityException, 
			NoSuchFieldException, 
			IllegalArgumentException, 
			IllegalAccessException 
	{
		super();
		
		
		CSV_Mapping mappingAnnotation;

		getters = new HashMap<>();
		setters = new HashMap<>();

		// screen fields
		for(Field field : type.getFields()){
			mappingAnnotation = field.getAnnotation(CSV_Mapping.class);
			if(mappingAnnotation!=null){
				String column = mappingAnnotation.tag();
				FieldMapping mapping = FieldMapping.build(field); 
				getters.put(column, mapping);
				setters.put(column, mapping);
			}
		}

		// screen setters
		for(Method method : type.getMethods()){
			mappingAnnotation = method.getAnnotation(CSV_Mapping.class);
			if(mappingAnnotation!=null){
				String column = mappingAnnotation.tag();
				MethodSetter setter = MethodSetter.build(method);
				if(setter!=null){
					setters.put(column, setter);
				}
				// TODO same things for getters
			}
		}
	}

	
	public Getter getGetter(String tag) {
		return getters.get(tag);
	}
	
	public Setter getSetter(String tag) {
		return setters.get(tag);
	}

}

