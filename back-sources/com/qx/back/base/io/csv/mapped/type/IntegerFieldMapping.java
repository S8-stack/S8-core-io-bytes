package com.qx.back.base.io.csv.mapped.type;

import java.lang.reflect.Field;

import com.qx.back.base.io.units.QxScientificUnit;


public class IntegerFieldMapping extends FieldMapping {

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