package com.qx.web.mime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * </p>
 * 
 * </p>
 * @author pc
 *
 */
public class MIME_Type {
	
	public final static Pattern PATTERN = Pattern.compile("([\\w-]+)/([\\w-\\.\\+]+)");
	
	private String template;
	
	private MIME_TopType topType;
	
	
	public MIME_Type(String template) throws Exception{
		Matcher matcher = PATTERN.matcher(template);
		if(!matcher.matches()) {
			throw new Exception("Template passed as argument is not matching MIME_Type pattern: "+template);
		}
		topType = MIME_TopType.get(matcher.group(1));
		this.template = template;
	}
	
	
	public String getTemplate() {
		return template;
	}
	
	public MIME_TopType getTopType() {
		return topType;
	}
	
	@Override
	public String toString() {
		return template;
	}
	
	
	private static boolean isInitialized = false;

	private static Map<String, MIME_Type> MAP;
	
	private static Map<String, MIME_Type> EXTENSIONS;

	
	private static void initialize() {
		if(!isInitialized) {
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(MIME_Type.class.getResourceAsStream("types")))){
				MAP = new HashMap<>();
				String template;
				while((template=reader.readLine())!=null) {
					MIME_Type type = new MIME_Type(template);
					MAP.put(type.getTemplate(), type);
				}
				
			} 
			catch (IOException e) {
				System.err.println("Failed to read MIME types due to: "+e.getMessage());
				e.printStackTrace();
			} 
			catch (Exception e) {
				System.err.println("Failed to read MIME types due to: "+e.getMessage());
				e.printStackTrace();
			}
			
			
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(MIME_Type.class.getResourceAsStream("extensions.csv")))){
				EXTENSIONS = new HashMap<>();
				String extension;
				
				// skip first line (headers)
				String line = reader.readLine();
				
				while((line=reader.readLine())!=null) {
					String[] cells = line.split(" *, *");
					extension = cells[0];
					MIME_Type type = MAP.get(cells[1]);
					EXTENSIONS.put(extension, type);
				}
				
			} 
			catch (IOException e) {
				System.err.println("Failed to read Extensions due to: "+e.getMessage());
				e.printStackTrace();
			} 
			
			isInitialized = true;
		}
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static MIME_Type get(String template){
		initialize();
		return MAP.get(template);
	}
	
	
	public final static Pattern FILENAME_PATTERN = Pattern.compile("[\\w\\./-]*(\\.\\w+)");

	
	public static MIME_Type find(String filname) {
		Matcher matcher = FILENAME_PATTERN.matcher(filname);
		if(!matcher.matches()) {
			return null;
		}
		String extension = matcher.group(1);
		return EXTENSIONS.get(extension);
	}
}
