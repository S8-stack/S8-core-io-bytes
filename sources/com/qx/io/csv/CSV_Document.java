package com.qx.io.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class CSV_Document {


	private boolean isDefined;

	private int nbRows, nbColumns;

	private CSV_ColumnHeaders columnHeaders;

	private CSV_Row[] rows;


	public CSV_Document(){
		isDefined=false;
	}


	/**
	 * 
	 * @param nbRows
	 * @param nbColumns
	 * @param hasHeaders
	 */
	public void define(int nbRows, int nbColumns, boolean hasHeaders){
		this.nbRows = nbRows;
		this.nbColumns = nbColumns;
		rows = new CSV_Row[nbRows];
		for(int i=0; i<nbRows; i++){
			rows[i] = new CSV_Row(nbColumns);
		}

		if(hasHeaders){
			columnHeaders = new CSV_ColumnHeaders(nbColumns);
		}
		isDefined = true;
	}

	
	public void load(String pathname, boolean hasHeaders) throws FileNotFoundException{
		load(new FileInputStream(new File(pathname)), hasHeaders);
	}


	public void load(InputStream inputStream, boolean hasHeaders){

		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


			// consume headers
			if(hasHeaders){
				columnHeaders = new CSV_ColumnHeaders(reader.readLine());
			}

			List<CSV_Row> rowList = new ArrayList<CSV_Row>();
			String line;
			while((line = reader.readLine())!=null){
				rowList.add(new CSV_Row(line, 0));
			}

			nbRows = rowList.size();
			if(hasHeaders){
				nbColumns = columnHeaders.getNumberOfCells();
			}
			else{
				nbColumns = rowList.get(0).getNumberOfCells();
			}

			rows = new CSV_Row[nbRows];
			for(int i=0; i<nbRows; i++){
				this.rows[i] = rowList.get(i);
			}
			reader.close();

			isDefined = true;

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	/**
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void write(String pathname) {

		try {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File(pathname))));

			// headers
			if(columnHeaders!=null){
				columnHeaders.write(writer);
			}

			// cells
			for(int i=0; i<nbRows; i++){
				rows[i].write(writer);
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
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	public String getCell(int rowIndex, int columnIndex){
		if(!isDefined){
			throw new RuntimeException("CSV Document is not defined");
		}
		if(rowIndex>nbRows-1 && rowIndex<0 && columnIndex<0 && columnIndex>nbColumns-1){
			throw new RuntimeException("Out of index");
		}
		return rows[rowIndex].get(columnIndex);
	}



	/**
	 * 
	 * @param value
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	public void setCell(String value, int rowIndex, int columnIndex){
		if(!isDefined){
			throw new RuntimeException("CSV Document is not defined");
		}
		if(rowIndex>nbRows-1 && rowIndex<0 && columnIndex<0 && columnIndex>nbColumns-1){
			throw new RuntimeException("Out of index");
		}
		rows[rowIndex].set(columnIndex, value);
	}
	
	
	/**
	 * 
	 * @param value
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	public CSV_Row getRow(int rowIndex){
		if(!isDefined){
			throw new RuntimeException("CSV Document is not defined");
		}
		if(rowIndex>nbRows-1 && rowIndex<0){
			throw new RuntimeException("Out of index");
		}
		return rows[rowIndex];
	}
	
	/**
	 * 
	 * @param value
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	public void setRow(int rowIndex, CSV_Row row){
		if(!isDefined){
			throw new RuntimeException("CSV Document is not defined");
		}
		if(rowIndex>nbRows-1 && rowIndex<0){
			throw new RuntimeException("Out of index");
		}
		rows[rowIndex] = row;
	}


	/**
	 * @param value
	 * @param columnIndex
	 */
	public void setHeader(String value, int columnIndex){
		if(columnHeaders==null){
			throw new RuntimeException("Document has no headers");
		}
		if(!isDefined){
			throw new RuntimeException("Out of index");
		}
		columnHeaders.set(columnIndex, value);
	}


	/**
	 * 
	 * @param columnIndex
	 * @return
	 */
	public String getHeader(int columnIndex){
		if(columnHeaders==null){
			throw new RuntimeException("Document has no headers");
		}
		if(!isDefined){
			throw new RuntimeException("Out of index");
		}
		return columnHeaders.get(columnIndex);
	}


	public int getNumberOfColumns(){
		return nbColumns;
	}

	public int getNumberOfRows(){
		return nbRows;
	}

}
