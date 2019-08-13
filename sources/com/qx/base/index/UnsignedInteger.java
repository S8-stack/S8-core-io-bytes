package com.qx.base.index;

import java.io.IOException;

import com.qx.base.bytes.ByteInput;
import com.qx.base.bytes.ByteOutput;

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
public class UnsignedInteger {

	public byte[] bytes;


	/**
	 * ZERO constructor
	 */
	public UnsignedInteger(int length) {
		super();
		bytes = new byte[length];
	}

	public UnsignedInteger(byte[] bytes) {
		super();
		this.bytes = bytes;
	}
	
	public UnsignedInteger(String hexadecimalEncoding) {
		super();
		int length = hexadecimalEncoding.length()/2;
		bytes = new byte[length];
		int offset=0;
		for(int i=0; i<length; i++) {
			bytes[i] = (byte) Integer.parseUnsignedInt(hexadecimalEncoding.substring(offset, offset+2), 16);
			offset+=2;
		}
	}
	
	public UnsignedInteger(int length, String hexadecimalEncoding) {
		super();
		bytes = new byte[length];
		int l = Math.min(hexadecimalEncoding.length()/2, length);
		int offset=0;
		for(int i=length-l; i<length; i++) {
			bytes[i] = (byte) Integer.parseUnsignedInt(hexadecimalEncoding.substring(offset, offset+2), 16);
			offset+=2;
		}
	}

	
	public UnsignedInteger(int length, long value) {
		super();
		bytes = new byte[length];
		int shift = 0;
		for(int i=0; i<length; i++) {
			bytes[length-1-i] = (byte) (0xff & (value >> shift));
			shift+=8;
		}
	}
	
	public UnsignedInteger(int length, int value) {
		super();
		bytes = new byte[length];
		int shift = 0;
		for(int i=0; i<length; i++) {
			bytes[length-1-i] = (byte) (0xff & (value >> shift));
			shift+=8;
		}
	}

	public UnsignedInteger(UnsignedInteger ui0, UnsignedInteger ui1) {
		super();
		int n0 = ui0.bytes.length , n1 = ui1.bytes.length;
		int nBytes = n0+n1;
		bytes = new byte[nBytes];
		for(int i=0; i<n0; i++) {
			bytes[i] = ui0.bytes[i];
		}
		for(int i=0; i<n1; i++) {
			bytes[n0+i] = ui1.bytes[i];
		}
	}

	public UnsignedInteger copy() {
		int nBytes=bytes.length;
		byte[] nextBytes = new byte[nBytes];
		for(int i=0; i<nBytes; i++) {
			nextBytes[i] = bytes[i];
		}
		return new UnsignedInteger(nextBytes);
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

	public void compose(ByteOutput outflow) throws IOException {
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
		if(object instanceof UnsignedInteger) {
			UnsignedInteger right = (UnsignedInteger) object;
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
	public boolean equals(UnsignedInteger right) {
		int length = bytes.length;
		for(int i=0; i<length; i++) {
			if(bytes[i]!=right.bytes[i]) {
				return false;
			}
		}
		return true;
	}
	
	
	public boolean isGreaterThan(UnsignedInteger right) {
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
	
	
	/**
	 * Shuffle all bytes at random
	 */
	public void randomize(){
		int n = bytes.length;
		for(int i=0; i<n; i++) {
			bytes[i] = (byte) (((int) (Math.random()*256)) & 0xff);
		}
	}
	
	
	public void read(ByteInput inflow) throws IOException {
		bytes = inflow.getByteArray(bytes.length);
	}
	
	public void write(ByteOutput outflow) throws IOException {
		outflow.putByteArray(bytes);
	}

}
