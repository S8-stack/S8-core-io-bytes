package com.qx.io.csv.mapped;

import java.lang.reflect.InvocationTargetException;

import com.qx.io.units.Unit;


public interface Setter {
	

	public abstract void set(String value, Object object, Unit unit)
			throws NumberFormatException, IllegalArgumentException, IllegalAccessException, InvocationTargetException;
	
	
}
