package com.qx.back.base.io.html;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class HTML_Table02 extends HTML_Block {

	private int n;
	private String title;
	private String[] columnNames;
	private String[] columnComments;
	
	private List<String[]> body;
	
	
	public HTML_Table02(String title, String[] columnNames, String[] columnComments){
		super();
		this.title = title;
		this.columnNames = columnNames;
		this.columnComments = columnComments;
		n = columnNames.length;
		body = new ArrayList<>();
	}
	
	
	public void push(String[] row){
		if(row.length!=n){
			throw new RuntimeException("Wrong number of cells while inserting new row in Table02");
		}
		body.add(row);
	}
	
	@Override
	public void print(Writer writer) throws IOException{
		
		writer.write("<table class=\"table02\">");
		
		// header
		writer.write("<thead>");
		
		// title
		writer.write("<tr>");
		writer.write("<td colspan:\""+n+"\">"+title+"</td>");
		for(int i=0; i<n-1; i++){
			writer.write("<td></td>");
		}
		writer.write("</tr>");
		
		// names
		writer.write("</tr>");
		for(String cell : columnNames){
			writer.write("<td>");
			writer.write(cell);
			writer.write("</td>");
		}
		writer.write("</tr>");
		
		// comments
		writer.write("</tr>");
		for(String cell : columnComments){
			writer.write("<td>");
			writer.write(cell);
			writer.write("</td>");
		}
		writer.write("</tr>");
		writer.write("</thead>");
		
		
		// body
		writer.write("<tbody>");
		for(String[] row : body){
			writer.write("<tr>");
			for(String cell : row){
				writer.write("<td>");
				writer.write(cell);
				writer.write("</td>");
			}
			writer.write("</tr>");
		}
		writer.write("</tbody>");
		writer.write("</table>");
	}
	
}
