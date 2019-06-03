package com.qx.back.base.resources;

import java.util.HashMap;
import java.util.Map;


public enum MIME_Type {

	/**
	 * Represents any document that contains text and is theoretically human readable
	 * text/plain, text/html, text/css, text/javascript
	 */
	TEXT("text", null),
	TEXT_PLAIN("text/plain", ".txt"),
	TEXT_HTML("text/html", ".html"),
	TEXT_CSS("text/css", ".css"),
	TEXT_CSV("text/csv", ".csv"),
	TEXT_Javascript("text/javascript", ".js"),
	

	/**
	 * image
	 * Represents any kind of images.
	 * Videos are not included, though animated images (like animated gif) are describes with an image type.
	 * image/gif, image/png, image/jpeg, image/bmp, image/webp
	 */
	IMAGE("image", null),
	IMAGE_GIF("image/gif", ".gif"),
	IMAGE_PNG("image/png", ".png"),
	IMAGE_JPEG("image/jpeg", ".jpg"),
	IMAGE_BMP("image/bmp", ".bmp"),
	IMAGE_WEBP("image/webp", ".webp"),
	IMAGE_SVG("image/svg+xml", ".svg"),


	/**
	 * audio
	 * Represents any kind of audio files: audio/midi, audio/mpeg, audio/webm, audio/ogg, audio/wav
	 */
	AUDIO("audio", null),
	AUDIO_MIDI("audio/midi", ".midi"),
	AUDIO_MPEG("audio/mpeg", ".mpeg"),
	AUDIO_WEBM("audio/webm", ".webm"),
	AUDIO_OGG("audio/ogg", ".ogg"),
	AUDIO_WAV("audio/wav", ".wav"),


	/**
	 * application
	 * Represents any kind of binary data.
	 * application/octet-stream, application/pkcs12,
	 *  application/vnd.mspowerpoint, application/xhtml+xml,
	 * application/xml,  application/pdf
	 **/
	APPLICATION("application", null),
	
	/**
	 * use this one for standard HTTP requests;, 
	 * @see <a href="https://stackoverflow.com/questions/4007969/
	 * application-x-www-form-urlencoded-or-multipart-form-data">Discussion</a>
	 */
	APPLICATION_XHR("application/x-www-form-urlencoded", null),
	APPLICATION_OCTET_STREAM("application/octet-stream", null),
	APPLICATION_JS("application/javascript", null),
	APPLICATION_ARRAY_BUFFER("application/arraybuffer", null),


	/**
	 * Multipart types indicates a category of document that are broken in distinct parts, 
	 * often with different MIME types.
	 * It is a way to represent composite document. 
	 */
	MULTIPART_FORM_DATA("multipart/form-data", null);
	
	
	public String token;
	public String extension;

	/**
	 * 
	 */
	private MIME_Type(String keyword, String extension) {
		this.token = keyword;
		this.extension = extension;
	}


	
	private static Map<String, MIME_Type> mapByToken;
	
	static {
		mapByToken = new HashMap<String, MIME_Type>(MIME_Type.values().length);
		for(MIME_Type type : MIME_Type.values()){
			mapByToken.put(type.token, type);
		}
	}

	private static Map<String, MIME_Type> mapByExtensions;

	static {
		mapByExtensions = new HashMap<String, MIME_Type>(MIME_Type.values().length);
		for(MIME_Type type : MIME_Type.values()){
			if(type.extension!=null){
				mapByExtensions.put(type.extension, type);	
			}
		}
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static MIME_Type getByToken(String token){
		return mapByToken.get(token);
	}
	
	public static MIME_Type getByExtenion(String extension){
		return mapByExtensions.get(extension);
	}
	
	
	/**
	 * 
	 * @param pathname
	 * @return the MIME_type that fits the pathname extension, null if no type match the extension
	 */
	public static MIME_Type match(String pathname){
		boolean isMatching = false;
		MIME_Type type = null;
		MIME_Type[] types = MIME_Type.values();
		int n = types.length, i=0, pathnameLength=pathname.length();
		
		while(!isMatching && i<n){
			type = types[i];
			if(type.extension!=null && pathnameLength>type.extension.length()){
				if(pathname.substring(pathnameLength-type.extension.length()).equals(type.extension)){
					isMatching = true;
				}	
			}
			i++;
		}
		
		if(isMatching){
			return type;
		}
		else{
			return null;
		}
	}
}
