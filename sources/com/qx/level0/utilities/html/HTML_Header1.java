package com.qx.level0.utilities.html;

import java.io.IOException;
import java.io.Writer;

public class HTML_Header1 extends HTML_Block {

	private String headerText;
	
	public HTML_Header1(String text) {
		this.headerText = text;
	}
	
	@Override
	public void print(Writer writer) throws IOException {
		writer.append("<h1>"+headerText+"</h1>\n");
	}

}
