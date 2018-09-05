package com.qx.io.csv.mapped;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CSV_Mapping {

	public String tag();
}
