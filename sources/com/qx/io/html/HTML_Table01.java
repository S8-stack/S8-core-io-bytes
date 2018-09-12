package com.qx.io.html;

import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.qx.io.units.Unit;

public class HTML_Table01 extends HTML_Block {

	private String name;
	private String[] header;
	private List<String[]> body;
	
	public static final DecimalFormat
	F01 = new DecimalFormat("0.00"),
	F02 = new DecimalFormat("0.00E0");
	
	public HTML_Table01(String name){
		super();
		this.name = name;
		this.header = new String[]{"Name", "Symbol", "Value", "Unit"};
		body = new ArrayList<>();
	}
	
	public void setHeader(int columnIndex, String value){
		header[columnIndex] = value;
	}
	
	public void pushRow(String... row){
		body.add(row);
	}
	
	public void pushF01(String name, String symbol, double value, Unit unit){
		body.add(new String[]{
				name,
				symbol,
				F01.format(unit.fromIS(value)),
				unit.abbreviation
		});
	}
	
	public void pushF02(String name, String symbol, double value, Unit unit){
		body.add(new String[]{
				name,
				symbol,
				F02.format(unit.fromIS(value)),
				unit.abbreviation
		});
	}
	
	public void push(String name, String symbol, int value){
		body.add(new String[]{
				name,
				symbol,
				Integer.toString(value),
				"-"
		});
	}
	
	public void push(String name, String value){
		body.add(new String[]{
				name,
				"-",
				value,
				"-"
		});
	}
	
	@Override
	public void print(Writer writer) throws IOException{
		
		writer.write("<table class=\"table01\">");
		
		// header
		writer.write("<thead>");
		writer.write("<tr><td colspan:\"4\">"+name+"</td><td></td><td></td><td></td></tr>");
		writer.write("</tr>");
		for(String cell : header){
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
