package com.qx.io.html;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HTML_Page {
	
	private List<HTML_Block> blocks;

	public HTML_Page() {
		super();
		blocks = new ArrayList<>();
	}
	
	public void push(HTML_Block block){
		if(block!=null){
			blocks.add(block);	
		}
	}

	
	public void print(String pathname) throws IOException{
		Writer writer = new OutputStreamWriter(new FileOutputStream(new File(pathname)));
		writer.append("<!DOCTYPE html>\n");
		writer.append("<html>\n");
		writer.append(getHeader());
		writer.append("<body>");
		for(HTML_Block block : blocks){
			block.print(writer);
		}
		writer.append("</body>");
		writer.append("</html>");
		writer.close();
	}
	
	
	
	private String getHeader() throws IOException{
		InputStream inputStream = HTML_Page.class.getResourceAsStream("head.html");
		StringBuilder builder = new StringBuilder();
		Reader reader = new BufferedReader(
				new InputStreamReader(inputStream, Charset.forName(StandardCharsets.UTF_8.name())));

		int c = 0;
		while ((c = reader.read()) != -1) {
			builder.append((char) c);
		}
		return builder.toString();
	}
}
