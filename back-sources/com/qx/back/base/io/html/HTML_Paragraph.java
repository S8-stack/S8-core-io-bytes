package com.qx.back.base.io.html;

import java.io.IOException;
import java.io.Writer;

public class HTML_Paragraph extends HTML_Block {

	private String text;

	public HTML_Paragraph(String text){
		super();
		this.text = text;
	
	}
	
	@Override
	public void print(Writer writer) throws IOException{
		writer.write("<p>");
		writer.write(text);
		writer.write("</p>");
	}
}
