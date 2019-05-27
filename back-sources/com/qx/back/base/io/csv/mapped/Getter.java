package com.qx.back.base.io.csv.mapped;

import com.qx.back.base.io.units.QxScientificUnit;

public interface Getter {


	public abstract String get(Object object, QxScientificUnit unit) throws IllegalArgumentException, IllegalAccessException;
}
