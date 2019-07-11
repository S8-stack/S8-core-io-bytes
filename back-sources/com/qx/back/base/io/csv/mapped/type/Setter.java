package com.qx.back.base.io.csv.mapped.type;

import java.lang.reflect.InvocationTargetException;

import com.qx.back.base.io.units.QxScientificUnit;


public interface Setter {
	

	public abstract void set(String value, Object object, QxScientificUnit unit)
			throws NumberFormatException,
			IllegalArgumentException,
			IllegalAccessException,
			InvocationTargetException;
	
	
}
