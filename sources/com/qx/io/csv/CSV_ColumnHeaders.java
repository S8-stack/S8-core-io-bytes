package com.qx.io.csv;

import java.io.BufferedWriter;
import java.io.IOException;

import com.qx.io.csv.CSV_Format;

public class CSV_ColumnHeaders {

	// cells of the line
	private String[] headers;

	
	public CSV_ColumnHeaders(String line) {
		headers = line.split(CSV_Format.DELIMITERS);
	}
	
	public CSV_ColumnHeaders(int nbColumns){
		headers = new String[nbColumns];
	}
	
	
	public void write(BufferedWriter writer) throws IOException{
		int nbColumns = headers.length;
		for(int i=0; i<nbColumns; i++){
			writer.append(headers[i]+((i<nbColumns-1)? CSV_Format.delimiter : CSV_Format.endOfLine));
		}
	}
	
	public int getNumberOfCells(){
		return headers.length;
	}
	
	public String get(int columnIndex){
		return headers[columnIndex];
	}

	public void set(int columnIndex, String value) {
		headers[columnIndex] = value;
	}
	
}
