package com.qx.level0.utilities.utils;

import java.io.IOException;

import com.qx.level0.lang.xml.annotation.XML_GetAttribute;
import com.qx.level0.lang.xml.annotation.XML_SetAttribute;
import com.qx.level0.lang.xml.annotation.XML_Type;



/**
 * All purposes identifier generator.
 * Generates compact human readable hash keys.
 * @author Pierre Convert
 *
 */
public class IdentifierGenerator {

	private final static char[] CHAR_LIST = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
		'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

	private final static int NUMBER_OF_CHAR = CHAR_LIST.length;


	private String prefix;

	private int length;



	/**
	 * buffer
	 */
	private char[] buffer;

	/**
	 * coordinates
	 */
	private int[] coordinates;


	/**
	 * index
	 */
	private long index;


	/**
	 * Default constructor
	 */
	public IdentifierGenerator(){
		this(12, "");
	}


	/**
	 * Standard constructor
	 * @param lenght
	 * @param prefix
	 */
	public IdentifierGenerator(int lenght, String prefix){
		setup(lenght, prefix);
	}


	/**
	 * 
	 * @param script
	 * @throws IOException 
	 */
	public IdentifierGenerator(Script script) throws IOException {
		setup(script.length, script.prefix);
		initialize(script.index);
	}



	public Script serialize(){
		Script script = new Script();
		script.length = length;
		script.prefix = prefix;
		script.index = index;
		return script;
	}


	/**
	 * setup
	 * @param length
	 * @param prefix
	 */
	private void setup(int length, String prefix){
		this.length = length;
		this.prefix = prefix;

		coordinates = new int[length];
		buffer = new char[length];

		clear();
	}


	public void clear(){
		for(int i=0; i<length; i++){
			coordinates[i] = 0;
			buffer[i] = CHAR_LIST[coordinates[i]];
		}
		index=0;
	}


	/**
	 * 
	 * @param index
	 * @throws IOException 
	 */
	public void initialize(long index){

		this.index = index;

		long[] multipliers = new long[length];
		int multiplier = 1;
		for(int i=0; i<length; i++){
			multipliers[length-1-i] = multiplier;
			multiplier*=NUMBER_OF_CHAR;
		}

		long b;
		for(int i=0; i<length; i++){
			b = index%multipliers[i];
			coordinates[i] = (int) ((index-b)/multipliers[i]);
			index = b;
			buffer[i] = CHAR_LIST[coordinates[i]];
		}

	}



	public synchronized String create(){
		String identifier = prefix+new String(buffer);

		int i=length-1;
		while(i>=0 && coordinates[i]==NUMBER_OF_CHAR-1){
			coordinates[i]=0;
			buffer[i] = CHAR_LIST[coordinates[i]];
			i--;
		}

		if(i>=0){
			coordinates[i]++;
			buffer[i] = CHAR_LIST[coordinates[i]];	
		}
		index++;

		return identifier;
	}





	@XML_Type(name="IdentifierGenerator")
	public static class Script {
		
		public long index;

		public String prefix;
		
		public int length;
		
		public String path;

		public IdentifierGenerator deserialize() throws IOException{
			return new IdentifierGenerator(this);
		}

		@XML_SetAttribute(tag="index")
		public void setIndex(long index) { this.index = index; }

		@XML_SetAttribute(tag="prefix")
		public void setPrefix(String prefix) { this.prefix = prefix; }

		@XML_SetAttribute(tag="length")
		public void setLength(int length) { this.length = length; }

		@XML_GetAttribute(tag="path")
		public String getPath() { return path; }
		
		@XML_GetAttribute(tag="index")
		public long getIndex() { return index; }

		@XML_GetAttribute(tag="prefix")
		public String getPrefix() { return prefix; }

		@XML_SetAttribute(tag="length")
		public int setLength() { return length; }

		@XML_SetAttribute(tag="path")
		public String setPath() { return path; }
		
	}



	public static void main(String[] args) throws IOException{
		IdentifierGenerator gen = new IdentifierGenerator(2, "f_");
		gen.initialize(12);
		for(int i=0; i<10; i++){
			System.out.println(gen.create());
		}
	}
}
