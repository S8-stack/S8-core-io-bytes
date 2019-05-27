package com.qx.index;

import java.io.IOException;

import com.qx.io.bytes.FileByteOutflow;

/**
 * 
 * General purpose index. Fully adaptable size.
 * <p>
 * Index is based on a bytes array, with the following structure:
 * </p>
 * <ul>
 * <li><b>Most significant bytes comes first</b>. This is especially important
 * for testing zero or equal, since least significant bytes are incremented
 * first but also to be tested first.</li>
 * <li><b>Least significant bytes comes last</b></li>
 * </ul>
 * 
 * @author pc
 *
 */
public class QxIndex {

	public byte[] bytes;


	/**
	 * ZERO constructor
	 */
	public QxIndex(int length) {
		super();
		bytes = new byte[length];
	}

	public QxIndex(byte[] bytes) {
		super();
		this.bytes = bytes;
	}


	public QxIndex copy() {
		int nBytes=bytes.length;
		byte[] nextBytes = new byte[nBytes];
		for(int i=0; i<nBytes; i++) {
			nextBytes[i] = bytes[i];
		}
		return new QxIndex(nextBytes);
	}


	/**
	 * 
	 * @param value
	 */
	public void parse(String value, int offset, int length){
		bytes = new byte[length];
		for(int i=0; i<length; i++){
			bytes[i] = (byte) Short.parseShort(value.substring(offset, offset+2), 16);
			offset+=2;
		}
	}


	public void compose(StringBuilder builder){
		int length = bytes.length;
		for(int i=0; i<length; i++){
			builder.append(String.format("%02x", bytes[i]));
		}
	}

	public void compose(FileByteOutflow outflow) throws IOException {
		outflow.putByteArray(bytes);
	}


	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		compose(builder);
		return builder.toString();
	}



	public void increment() {
		int index=bytes.length-1;
		while(index>=0 && bytes[index]++ == (byte) 0xff){
			bytes[index] = 0;
			index--;
		}
	}


	public boolean isZero() {
		for(int i=bytes.length-1; i>=0; i--) {
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
		int length = bytes.length;
		for(int i=0; i<length; i++) {
			if(bytes[i]!=right.bytes[i]) {
				return false;
			}
		}
		return true;
	}
	
	
	public boolean isGreaterThan(QxIndex right) {
		int length = bytes.length;
		for(int i=0; i<length; i++) {
			if(bytes[i] < right.bytes[i]) {
				return false;
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

}
