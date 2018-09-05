package com.qx.io.csv;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

@Deprecated
public class CSV_StreamReaderOld implements Iterable<CSV_Row>{


	public interface Source {
		public InputStream get() throws FileNotFoundException;
	}
	
	private Source source;

	/**
	 * Number of columns
	 */
	private int nbColumns;

	/**
	 * array of column headers
	 */
	public String[] columnHeaders;


	/**
	 * 
	 * @param nbColumns
	 * @param nbRows
	 * @throws IOException 
	 */
	public CSV_StreamReaderOld(String path) throws IOException{
		// get reader on file
		source = new Source(){
			public @Override InputStream get() throws FileNotFoundException { return new FileInputStream(new File(path)); }
		};
		initialize();
	}
	
	public CSV_StreamReaderOld(Source source) throws IOException{
		this.source = source;
		initialize();
	}
	
	
	
	private void initialize() throws IOException{

		// words
		String[] words;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(source.get()));

		// consume headers
		words = reader.readLine().split(CSV_Format.DELIMITERS);
		nbColumns = words.length;
		columnHeaders = new String[nbColumns];
		for(int i=0; i<nbColumns; i++){
			columnHeaders[i] = words[i];
		}
		reader.close();

	}


	/**
	 * 
	 * @param header
	 * @return
	 */
	public int getColumnIndex(String header){
		for(int i=0; i<nbColumns; i++){
			if(columnHeaders[i].equals(header)){
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @return number of data lines (without header)
	 */
	public int getNumberOfLines(){
		int lines = 0;
		for(@SuppressWarnings("unused") CSV_Row row : this){
			lines++;
		}
		return lines;
	}


	@Override
	public Iterator<CSV_Row> iterator() {
		return new CSVLineIterator();
	}


	private class CSVLineIterator implements Iterator<CSV_Row> {

		private BufferedReader reader;
		
		private int lineCount = -1;
		
		private String lineData;

		private CSV_Row nextLine;

		public CSVLineIterator(){
			
			// get reader on file
			try{
				reader = new BufferedReader(new InputStreamReader(source.get()));	
			}
			catch(Exception exception){
				exception.printStackTrace();
			}
			
			// consume first line (headers)
			consumeStream();
			
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
