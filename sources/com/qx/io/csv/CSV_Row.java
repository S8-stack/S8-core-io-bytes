package com.qx.io.csv;

import java.io.BufferedWriter;
import java.io.IOException;

public class CSV_Row {
	
	
	private int index;

	// cells of the line
	private String[] cells;

	
	public CSV_Row(String line, int index) {
		cells = line.split(CSV_Format.DELIMITERS);
		this.index= index;
	}
	
	
	
	public int getLineIndex(){
		return index;
	}
	
	public CSV_Row(int nbColumns){
		cells = new String[nbColumns];
	}
	
	
	
	public void write(BufferedWriter writer) throws IOException{
		int nbColumns = cells.length;
		for(int i=0; i<nbColumns; i++){
			writer.append(cells[i]+((i<nbColumns-1)? CSV_Format.delimiter : CSV_Format.endOfLine));
		}
	}
	
	public int getNumberOfCells(){
		return cells.length;
	}
	
	public String get(int columnIndex){
		return cells[columnIndex];
	}

	public void set(int columnIndex, String value) {
		cells[columnIndex] = value;
	}
	
	public void set(int columnIndex, float value) {
		cells[columnIndex] = String.valueOf(value);
	}
	
	public void set(int columnIndex, double value) {
		cells[columnIndex] = String.valueOf(value);
	}
	
	public void set(int columnIndex, int value) {
		cells[columnIndex] = String.valueOf(value);
	}
	
	public void set(int columnIndex, boolean value) {
		cells[columnIndex] = String.valueOf(value);
	}
	
	public void set(int columnIndex, long value) {
		cells[columnIndex] = String.valueOf(value);
	}
	
	public void check(int nbColumns){
		if(cells.length!=nbColumns){
			throw new RuntimeException("CVS reader found "+cells.length+""+nbColumns+" values per rows are expected.");
		}
	}

	
}
