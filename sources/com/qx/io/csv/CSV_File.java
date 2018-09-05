package com.qx.io.csv;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class CSV_File {


	
	
	protected String path;
	
	/**
	 * Number of columns
	 */
	protected int nbColumns;


	/**
	 * Number of rows (values only, header line not accounted)
	 */
	protected int nbRows;


	/**
	 * array of column headers
	 */
	protected String[] columnHeaders;


	/**
	 * matrix of values
	 */
	protected String[] values;


	/**
	 * 
	 * @param nbColumns
	 * @param nbRows
	 */
	public CSV_File(String path, int nbColumns, int nbRows){
		this.path = path;
		this.nbColumns = nbColumns;
		this.nbRows = nbRows;

		values = new String[nbColumns*nbRows];
		columnHeaders = new String[nbColumns];
	}
	
	
	
	public boolean isFileExisting(){
		return new File(path).exists();
	}


	public String getValue(int columnIndex, int rowIndex){
		return values[rowIndex*nbColumns+columnIndex];
	}

	public void setValue(String value, int columnIndex, int rowIndex){
		values[rowIndex*nbColumns+columnIndex] = value;
	}


	public String getColumnHeader(int columnIndex){
		return columnHeaders[columnIndex];
	}

	public void setColumnHeader(String columnHeader, int columnIndex){
		columnHeaders[columnIndex] = columnHeader;
	}


	/**
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void write() {

		try {
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(new File(path)));
			
			/*
			 * headers
			 */
			for(int j=0; j<nbColumns; j++){
				writer.append(columnHeaders[j]);

				if(j<nbColumns-1){
					writer.append(CSV_Format.delimiter);	
				}else{
					writer.append(CSV_Format.endOfLine);	
				}
			}


			/*
			 * Values 
			 */
			for(int i=0; i<nbRows; i++){
				for(int j=0; j<nbColumns; j++){
					writer.append(getValue(j, i));

					if(j<nbColumns-1){
						writer.append(CSV_Format.delimiter);	
					}
					else{
						writer.append(CSV_Format.endOfLine);
					}
				}
			}
			writer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}

	
	/**
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void load() {

		try {
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
			
			// consume headers
			reader.readLine();
			
			String[] words;
			for(int i=0; i<nbRows; i++){
				words = reader.readLine().split(CSV_Format.DELIMITERS);
				if(words.length!=nbColumns){
					reader.close();
					throw new RuntimeException("CVS reader found "+words.length+" values at line "+i+" of file "+path+".\n"+
							nbColumns+" values per rows are expected.");
				}
				for(int j=0; j<nbColumns; j++){
					setValue(words[j], j, i);
				}
			}
			reader.close();
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
