package com.qx.back.base.io.csv.mapped.type;

import java.lang.reflect.Field;

import com.qx.back.base.io.units.QxScientificUnit;

public class LongFieldMapping extends FieldMapping {

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
