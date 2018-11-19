package com.qx.io.csv.mapped;

import com.qx.io.units.QxScientificUnit;

public interface Getter {


	public abstract String get(Object object, QxScientificUnit unit) throws IllegalArgumentException, IllegalAccessException;
}
