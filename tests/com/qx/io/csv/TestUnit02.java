package com.qx.io.csv;


import java.io.File;
import java.io.FileInputStream;

import com.qx.io.csv.CSV_Row;
import com.qx.io.csv.CSV_StreamReader;

public class TestUnit02 {

	public static void main(String[] args) throws Exception {
		CSV_StreamReader reader = new CSV_StreamReader(new FileInputStream(new File("data/test_data.csv")));
		int index = reader.getColumnIndex("Viscosity [muPa.s]");
		for(CSV_Row row : reader){
			System.out.println(row.get(index));
		}
	}

}
