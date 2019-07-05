package com.qx.back.base.io.csv.mapped;

import java.lang.reflect.Field;

import com.qx.back.base.io.units.QxScientificUnit;

public class FloatFieldMapping extends FieldMapping {

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
