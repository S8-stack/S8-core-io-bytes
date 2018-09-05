package com.qx.io.csv;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class CSV_StreamWriter {


	/**
	 * 
	 */
	private BufferedWriter writer;

	/**
	 * Number of columns
	 */
	private int nbLevels;
	
	/**
	 * Number of columns
	 */
	private int nbColumns;

	/**
	 * array of column headers
	 */
	private String[] columnHeaders;


	/**
	 * 
	 * @param nbColumns
	 * @param nbRows
	 * @throws IOException 
	 */
	public CSV_StreamWriter(String pathname, String[]... columnHeaders) throws IOException{
		initialize(pathname, columnHeaders);
	}
	
	
	private void initialize(String path, String[]... columnHeaders) throws IOException{
		this.columnHeaders = columnHeaders[0];
		nbLevels = columnHeaders.length;
		nbColumns = columnHeaders[0].length;
		
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path))));

		// write headers
		
		String[] headers;
		for(int j=0; j<nbLevels; j++){
			headers = columnHeaders[j];
			for(int i=0; i<nbColumns; i++){
				writer.append(headers[i]+((i<nbColumns-1)? CSV_Format.delimiter : CSV_Format.endOfLine));
			}	
		}
	}


	public CSV_Row createLine(){
		return new CSV_Row(nbColumns);
	}

	public void append(CSV_Row line) throws IOException{
		line.write(writer);
	}

	public void close() throws IOException{
		writer.close();
	}


	/**
	 * 
	 * @param header
	 * @return
	 */
	public int getColumnIndex(String header){
		for(int i=0; i<nbColumns; i++){
			if(columnHeaders[i].equals(header)){
				return i;
			}
		}
		throw new RuntimeException("Header not found: "+header);
	}

}
