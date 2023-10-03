package com.s8.io.bytes.utilities.index;

import java.io.IOException;

import com.s8.api.bytes.ByteInflow;
import com.s8.api.bytes.ByteOutflow;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class QxIndex {

	
	/**
	 * <p>
	 * Index is based on a bytes array, with the following structure:
	 * </p>
	 * <ul>
	 * <li><b>Least significant bytes comes first</b>. This is especially important
	 * for testing zero or equal, since least significant bytes are incremented
	 * first but also to be tested first.</li>
	 * <li><b>Most significant bytes comes last</b></li>
	 * </ul>
	 */
	public byte[] bytes;


	public QxIndex(byte[] bytes) {
		super();
		this.bytes = bytes;
	}



	
	/**
	 * 
	 * @param right
	 * @return
	 */
	public QxIndex append(QxIndex right) {
		
		int nLeft = bytes.length , nRight = right.bytes.length;
		int nBytes = nLeft+nRight;
		byte[] bytes = new byte[nBytes];
		
		// left
		for(int i=0; i<nLeft; i++) {
			bytes[i] = this.bytes[i];
		}
		
		// right
		for(int i=0; i<nRight; i++) {
			bytes[nLeft+i] = right.bytes[i];
		}
		return new QxIndex(bytes);
	}

	
	/**
	 * 
	 * @return
	 */
	public QxIndex copy() {
		int nBytes=bytes.length;
		byte[] nextBytes = new byte[nBytes];
		for(int i=0; i<nBytes; i++) {
			nextBytes[i] = bytes[i];
		}
		return new QxIndex(nextBytes);
	}


	public static QxIndex fromHexadecimal(String value) {
		int length = value.length()/2;
		byte[] bytes = new byte[length];
		int offset=0, index=length-1;
		for(int i=0; i<length; i++) {
			bytes[index] = (byte) Integer.parseUnsignedInt(value.substring(offset, offset+2), 16);
			index--;
			offset+=2;
		}
		return new QxIndex(bytes);
	}
	
	
	public static QxIndex fromHexadecimal(String value, int trimLength) {
		byte[] bytes = new byte[trimLength];
		int offset = value.length()-2*trimLength, index=trimLength-1;
		for(int i=0; i<trimLength; i++) {
			bytes[index] = (byte) Integer.parseUnsignedInt(value.substring(offset, offset+2), 16);
			index--;
			offset+=2;
		}
		return new QxIndex(bytes);
	}
	
	
	public static QxIndex fromLong(int length, long value) {
		byte[] bytes = new byte[length];
		
		// least significant bytes first
		for(int i=0; i<length; i++) {
			bytes[i] = (byte) (0xff & value);
			value >>= 8;
		}
		return new QxIndex(bytes);
	}

	public static QxIndex fromInt(int length, int value) {
		byte[] bytes = new byte[length];
		
		// least significant bytes first
		for(int i=0; i<length; i++) {
			bytes[i] = (byte) (0xff & value);
			value >>= 8;
		}
		return new QxIndex(bytes);
	}
	
	
	
	public static QxIndex zero(int length) {
		return new QxIndex(new byte[length]);
	}

	/**
	 * 
	 * @param value
	 */
	/*
	public static QxIndex fromHexadecimal(String value, int offset, int length){
		byte[] bytes = new byte[length];
		for(int i=0; i<length; i++){
			bytes[i] = (byte) Short.parseShort(value.substring(offset, offset+2), 16);
			offset+=2;
		}
	}
	*/


	public void compose(StringBuilder builder){
		int length = bytes.length;
		int index=length-1;
		for(int i=0; i<length; i++){
			builder.append(String.format("%02x", bytes[index]));
			index--;
		}
	}

	public void compose(ByteOutflow outflow) throws IOException {
		outflow.putByteArray(bytes);
	}


	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		compose(builder);
		return builder.toString();
	}


	public String toHexadecimal() {
		StringBuilder builder = new StringBuilder();
		int length = bytes.length, index = length-1;
		for(int i=0; i<length; i++){
			builder.append(String.format("%02x", bytes[index]));
			index--;
		}
		return builder.toString();
	}



	public void increment() {
		int index=0, length=bytes.length;
		while(index<length && bytes[index]++ == (byte) 0xff){
			bytes[index] = 0;
			index++;
		}
	}
	


	public boolean isZero() {
		int length=bytes.length;
		for(int i=0; i<length; i++) {
			if(bytes[i]!=0) {
				return false;
			};
		}
		return true;
	}




	/**
	 * 
	 * @return a copy of underlying bytes array
	 */
	public byte[] getBytes() {
		int nBytes=bytes.length;
		byte[] bytesCopy = new byte[nBytes];
		for(int i=0; i<nBytes; i++) {
			bytesCopy[i] = bytes[i];
		}
		return bytesCopy;
	}
	
	

	/**
	 * The multiplier of this function is 31 and it has its own rationale: Joshua
	 * Bloch in Effective Java explained this solution: "The value 31 was chosen
	 * because it is an odd prime. If it were even and the multiplication
	 * overflowed, information would be lost, as multiplication by 2 is equivalent
	 * to shifting. The advantage of using a prime is less clear, but it is
	 * traditional. A nice property of 31 is that the multiplication can be replaced
	 * by a shift and a subtraction for better performance: 31 * i == (i << 5) - i.
	 * Modern VMs do this sort of optimization automatically.
	 */
	@Override
	public int hashCode() {
		int length = bytes.length, h = 0;
		for(int i=0; i<length; i++) {
			h = 31 * h + bytes[i];
		}
		return h;
	}

	@Override
	public boolean equals(Object object) {
		if(object instanceof QxIndex) {
			QxIndex right = (QxIndex) object;
			for(int i=bytes.length-1; i>=0; i--) {
				if(bytes[i]!=right.bytes[i]) {
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}


	/**
	 * Assume that right key has the same length.
	 * 
	 * @param right
	 * @return
	 */
	public boolean equals(QxIndex right) {
		if(right!=null) {
			int length = bytes.length;
			for(int i=0; i<length; i++) {
				// start with least significant bytes first
				if(bytes[i]!=right.bytes[i]) {
					return false;
				}
			}
			return true;	
		}
		else {
			return false;
		}
	}


	public boolean isGreaterThan(QxIndex right) {
		int length = bytes.length, index=length-1;
		int bLeft, bRight;
		for(int i=0; i<length; i++) {
			bLeft = bytes[index] & 0xff; // suppress sign
			bRight = right.bytes[index] & 0xff; // suppress sign
			if(bLeft < bRight) {
				return false;
			}
			else if(bLeft > bRight) {
				return false;
			}
			else {
				index--;
			}
		}
		return true;
	}




	/**
	 * Assume (not tested) that <code>bytes.length</code> is 1.
	 * @return
	 */
	public int toUInt8() {
		return bytes[0] & 0xff;
	}



	public short toInt16() {
		return (short) ((bytes[1] << 8) | (bytes[0] & 0xff));
	}


	/**
	 * 
	 * @return the 4 least significant bytes turned into an int32.
	 */
	public int toInt32() {
		int n0 = bytes.length-1, n1=n0-4>0?n0-4:0;
		int shift = 0, result=0;
		for(int index=n0; index>n1; index--) {
			result |= (bytes[index] & 0xff) << shift;
			shift+=8;
		}
		return result;
	}
	
	/*
	public QxIndex max(QxIndex right) {
		int nLeft = bytes.length, nRight = right.bytes.length, n=Math.max(nLeft, nRight);
		for(int i=0; i<n; i++) {
			int bLeft = i<nLeft?bytes[nLeft-i]:0x00;
			int bRight = i<nRight?bytes[nRight-i]:0x00;
			
		}
	}
	*/
	


	/**
	 * Shuffle all bytes at random
	 */
	public void randomize(){
		int n = bytes.length;
		for(int i=0; i<n; i++) {
			bytes[i] = (byte) (((int) (Math.random()*256)) & 0xff);
		}
	}


	public void read(ByteInflow inflow) throws IOException {
		bytes = inflow.getByteArray(bytes.length);
	}

	public void write(ByteOutflow outflow) throws IOException {
		outflow.putByteArray(bytes);
	}

}
