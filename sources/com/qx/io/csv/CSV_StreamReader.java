package com.qx.io.csv;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CSV_StreamReader implements Iterable<CSV_Row>{

	/**
	 * reader
	 */
	private BufferedReader reader;
	
	private int nbColumns;
	
	private Map<String, Integer> indices;



	/**
	 * 
	 * @param nbColumns
	 * @param nbRows
	 * @throws Exception 
	 */
	public CSV_StreamReader(String pathname) throws Exception{
		super();
		initialize(new FileInputStream(new File(pathname)));
	}
	
	/**
	 * 
	 * @param nbColumns
	 * @param nbRows
	 * @throws Exception 
	 */
	public CSV_StreamReader(InputStream inputStream) throws Exception{
		super();
		initialize(inputStream);
	}
	
	private void initialize(InputStream inputStream) throws IOException{
		reader = new BufferedReader(new InputStreamReader(inputStream));

		String[] headers = reader.readLine().split(CSV_Format.DELIMITERS);
		nbColumns = headers.length;
		
		indices = new HashMap<String, Integer>();
		for(int i=0; i<nbColumns; i++){
			indices.put(headers[i], i);
		}
	}

	/**
	 * 
	 * @param header
	 * @return
	 */
	public int getColumnIndex(String header){
		return indices.get(header);
	}


	@Override
	public Iterator<CSV_Row> iterator() {
		return new LineIterator();
	}





	public class LineIterator implements Iterator<CSV_Row> {

		private int lineCount = -1;

		private String lineData;

		private CSV_Row nextLine;

		public LineIterator(){
			nextLine = consumeStream();
		}


		private CSV_Row consumeStream() {

			lineCount++;
			CSV_Row row;
			if(reader!=null){
				// consume first line
				try {
					lineData = reader.readLine();
					if(lineData!=null){
						try{
							row = new CSV_Row(lineData, lineCount);
							row.check(nbColumns);
							return row;
						}
						catch (Exception exception){
							System.out.println(exception.getMessage()+" at line "+lineCount);
						}
					}
					else{
						reader.close();
						return null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		public boolean hasNext() {
			return nextLine!=null;
		}

		@Override
		public CSV_Row next() {
			CSV_Row currentLine = nextLine;
			nextLine = consumeStream();
			return currentLine;
		}

		@Override
		public void remove() {
			throw new RuntimeException("Not implemented");
		}
	}


}
