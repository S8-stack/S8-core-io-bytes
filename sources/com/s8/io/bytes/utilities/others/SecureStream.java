package com.s8.io.bytes.utilities.others;

import java.io.IOException;
import java.io.InputStream;

/**
 * Secure version of InputStream reader
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class SecureStream {


	public final static int MAX_COUNT = 2048;

	public static boolean IS_VERBOSE = false;

	/**
	 * 
	 * 	The contracts of the read methods for InputStream and Reader classes and their subclasses are complicated
	 *  with regard to filling byte or character arrays. According to the Java API [API 2014] for the class InputStream,
	 *  the read(byte[] b) method and the read(byte[] b, int off, int len) method provide the following behavior:
	 *  <blockquote>The number of bytes actually read is returned as an integer. This method blocks
	 *  until input data is available, end of file is detected, or an exception is thrown. </blockquote>
	 *	According to the Java API for the read(byte[] b, int off, int len) method:
	 *	<blockquote>
	 *	An attempt is made to read as many as len bytes, but a smaller number may be read, possibly zero.
	 * 	</blockquote>
	 *	Both read methods return as soon as they find available input data.
	 *	As a result, these methods can stop reading data before the array is filled because the available 
	 *	data may be insufficient to fill the array.
	 *	
	 * <p>The documentation for the analogous read methods in Reader return the number of characters read, 
	 *	which implies that they also need not fill the char array provided as an argument.
	 *	Ignoring the result returned by the read() methods is a violation of EXP00-J. 
	 *	Do not ignore values returned by methods. Security issues can arise even when return values are considered
	 * because the default behavior of the read() methods lacks any guarantee that the entire buffer array is filled.
	 * </p>
	 * <p>
	 * Consequently, when using read() to fill an array, the program must check the return value of read() 
	 * and must handle the case where the array is only partially filled. In such cases,
	 * the program may try to fill the rest of the array, or work only with the subset of
	 * the array that was filled, or throw an exception.
	 * </p>
	 * 
	 * @param inputStream
	 * @param bytes
	 * @return the number of bytes read
	 * @throws IOException
	 */
	public static int read(InputStream inputStream, byte[] bytes) throws IOException {
		int offset = 0;
		int bytesRead = 0;
		int length = bytes.length;
		boolean isRead = false;
		int count=0;
		while (!isRead && count<MAX_COUNT) {
			bytesRead = inputStream.read(bytes, offset, length - offset);
			if(bytesRead>=0){

				offset += bytesRead;
				if (offset == length) {
					isRead = true;
				}
				count++;
				if(count==MAX_COUNT){
					throw new IOException("Max number of read attempts has been reached");
				}
				else if(IS_VERBOSE && count>1){
					System.out.println("Read again"); 
				}
			}
			else{
				isRead = true;
			}
		}
		return offset;
	}
}
