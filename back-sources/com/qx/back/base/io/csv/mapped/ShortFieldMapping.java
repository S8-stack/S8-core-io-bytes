package com.qx.back.base.io.csv.mapped;

import java.lang.reflect.Field;

import com.qx.back.base.io.units.QxScientificUnit;

public class ShortFieldMapping extends FieldMapping {

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