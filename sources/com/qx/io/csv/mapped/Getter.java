package com.qx.io.csv.mapped;

import com.qx.io.units.Unit;

public interface Getter {


	public abstract String get(Object object, Unit unit) throws IllegalArgumentException, IllegalAccessException;
}
