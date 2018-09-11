package com.qx.io.html;

import java.io.IOException;
import java.io.Writer;

public class HTML_Table extends HTML_Block {

	private int nbColumns;
	private int nbRows;
	
	private String[] header;
	private String[][] body;
	
	public HTML_Table(int nbColumns, int nbRows){
		super();
		this.nbColumns = nbColumns;
		this.nbRows = nbRows;
		
		header = new String[nbColumns];
		body = new String[nbRows][nbColumns];
	}
	
	public void setHeader(int columnIndex, String value){
		header[columnIndex] = value;
	}
	
	public void setBody(int columnIndex, int rowIndex, String value){
		body[rowIndex][columnIndex] = value;
	}
	
	@Override
	public void print(Writer writer) throws IOException{
		writer.write("<table>");
		
		// header
		writer.write("<thead><tr>");
		for(int i=0; i<nbColumns; i++){
			writer.write("<td>");
			writer.write(header[i]);
			writer.write("</td>");
		}
		writer.write("</tr></thead>");
		
		
		// body
		String[] row;
		writer.write("<tbody>");
		for(int rowIndex=0; rowIndex<nbRows; rowIndex++){
			row = body[rowIndex];
			writer.write("<tr>");
			for(int columnIndex=0; columnIndex<nbColumns; columnIndex++){
				writer.write("<td>");
				writer.write(row[columnIndex]);
				writer.write("</td>");
			}
			writer.write("</tr>");
		}
		writer.write("</tbody>");
		writer.write("</table>");
	}
	
}
