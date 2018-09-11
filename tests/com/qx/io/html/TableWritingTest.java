package com.qx.io.html;

import java.io.IOException;

public class TableWritingTest {

	public static void main(String[] args) throws IOException {

		HTML_Page page = new HTML_Page();
		
		HTML_Table table = new HTML_Table(4, 5);
		table.setHeader(0, "name");
		table.setHeader(1, "symbol");
		table.setHeader(2, "value");
		table.setHeader(3, "unit");
		
		for(int i=0; i<5; i++){
			table.setBody(0, i, "temperature");
			table.setBody(1, i, "T_in");
			table.setBody(2, i, "123.656");
			table.setBody(3, i, "°C");
		}
		
		page.push(table);
		
		page.print("output/myReport.html");
		
	}

}
