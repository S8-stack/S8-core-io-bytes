package com.qx.io.csv;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class CSV_DoubleArray {



	/**
	 * Number of columns
	 */
	protected int nbColumns;


	/**
	 * Number of rows (values only, header line not accounted)
	 */
	protected int nbRows;


	/**
	 * matrix of values
	 */
	protected double[] values;


	/**
	 * 
	 * @param nbColumns
	 * @param nbRows
	 */
	public CSV_DoubleArray(int nbColumns, int nbRows){
		this.nbColumns = nbColumns;
		this.nbRows = nbRows;
		values = new double[nbColumns*nbRows];
	}




	public double getValue(int columnIndex, int rowIndex){
		return values[rowIndex*nbColumns+columnIndex];
	}


	public void setValue(double value, int columnIndex, int rowIndex){
		values[rowIndex*nbColumns+columnIndex] = value;
	}

	/**
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void write(String path) {

		try {
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(new File(path)));


			/*
			 * Values 
			 */
			for(int i=0; i<nbRows; i++){
				for(int j=0; j<nbColumns; j++){
					writer.append(String.valueOf(getValue(j, i))
							+(j<nbColumns-1?CSV_Format.delimiter:CSV_Format.endOfLine));
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
	public void load(String path) {

		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));

			String[] words;
			for(int i=0; i<nbRows; i++){
				words = reader.readLine().split(CSV_Format.DELIMITERS);

				if(words.length!=nbColumns){
					reader.close();
					throw new RuntimeException("CVS reader found "+words.length+" values at line "+i+" of file "+path+".\n"+
							nbColumns+" values per rows are expected.");
				}
				for(int j=0; j<nbColumns; j++){
					values[i*nbColumns+j]=Double.valueOf(words[j]);
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
