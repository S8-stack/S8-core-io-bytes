package com.s8.io.bytes.utilities.others;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


/**
 * 
 *
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 */
public class ByteUtilities {


	/**
	 * 
	 * @param bytes
	 * @param start
	 * @param stop
	 * @return
	 */
	public static byte[] crop(byte[] bytes, int start, int stop){
		int length = stop-start;
		byte[] croppedBytes = new byte[length];
		for(int i=0; i<length; i++){
			croppedBytes[i] = bytes[start+i];
		}
		return croppedBytes;
	}


	/**
	 * 
	 * @param bytes: the array we write in
	 * @param value: the value to be written
	 * @param offset: the offset position for writing operation in bytes
	 */
	public static void write(byte[] bytes, int offset, byte[] value){
		for(int i=0; i<value.length; i++){
			bytes[i+offset] = value[i];
		}
	}


	/**
	 * write on two bytes
	 * @param bytes
	 * @param offset
	 * @param value
	 */
	public static void writeInteger16(byte[] bytes, int offset, short value){

		// high byte
		bytes[offset+0] = (byte) (0xff & (value >> 8));

		// low byte
		bytes[offset+1] = (byte) (0xff & value);

	}
	
	
	/**
	 * write on two bytes
	 * @param bytes
	 * @param offset
	 * @param value
	 */
	public static void writeInteger16(byte[] bytes, int offset, int value){

		// high byte
		bytes[offset+0] = (byte) (0xff & (value >> 8));

		// low byte
		bytes[offset+1] = (byte) (0xff & value);

	}



	public static void writeInteger32(byte[] bytes, int offset, int value) {

		// high byte
		bytes[offset+0] = (byte) (0xff & (value >> 24));
		bytes[offset+1] = (byte) (0xff & (value >> 16));
		bytes[offset+2] = (byte) (0xff & (value >> 8));
		bytes[offset+3] = (byte) (0xff & value); // low byte
	}


	public static void writeInteger64(byte[] bytes, int offset, long value) {

		// high byte
		bytes[offset+0] = (byte) (0xff & (value >> 56));
		bytes[offset+1] = (byte) (0xff & (value >> 48));
		bytes[offset+2] = (byte) (0xff & (value >> 40));
		bytes[offset+3] = (byte) (0xff & (value >> 32));
		bytes[offset+4] = (byte) (0xff & (value >> 24));
		bytes[offset+5] = (byte) (0xff & (value >> 16));
		bytes[offset+6] = (byte) (0xff & (value >> 8));
		bytes[offset+7] = (byte) (0xff & value);
	}





	/**
	 * Converts the register (16-bit value) at the given index
	 * into a <tt>short</tt>.
	 * The value returned is:
	 * <p/>
	 * <pre><code>
	 * (short)((a &lt;&lt; 8) | (b &amp; 0xff))
	 * </code></pre>
	 * <p/>
	 * This conversion has been taken from the documentation of
	 * the <tt>DataInput</tt> interface.
	 *
	 * @param bytes a <tt>byte[]</tt> containing a short value.
	 * @param offset an offset into the given byte[].
	 * @return the signed short as <tt>short</tt>.
	 */
	public static short readInteger16(byte[] bytes, int offset) {
		return (short) ((bytes[offset] << 8) | (bytes[offset + 1] & 0xff));
	}

	/**
	 * Converts a byte[4] binary int value to a primitive int.<br>
	 * The value returned is:
	 * <p><pre>
	 * <code>
	 * (((a &amp; 0xff) &lt;&lt; 24) | ((b &amp; 0xff) &lt;&lt; 16) |
	 * &#32;((c &amp; 0xff) &lt;&lt; 8) | (d &amp; 0xff))
	 * </code></pre>
	 *
	 * @param bytes registers as <tt>byte[4]</tt>.
	 * @return the integer contained in the given register bytes.
	 */
	public static int readInteger32(byte[] bytes, int offset) {
		return (
				((bytes[offset+0] & 0xff) << 24) |
				((bytes[offset+1] & 0xff) << 16) |
				((bytes[offset+2] & 0xff) << 8) |
				(bytes[offset+3] & 0xff)
				);
	}

	/**
	 * Converts a byte[8] binary long value into a long
	 * primitive.
	 *
	 * @param bytes a byte[8] containing a long value.
	 * @return a long value.
	 */
	public static long readInteger64(byte[] bytes, int offset) {
		/*
		return (
				((bytes[offset+0] & 0xff) << 56) |
				((bytes[offset+1] & 0xff) << 48) |
				((bytes[offset+2] & 0xff) << 40) |
				((bytes[offset+3] & 0xff) << 32) |
				((bytes[offset+4] & 0xff) << 24) |
				((bytes[offset+5] & 0xff) << 16) |
				((bytes[offset+6] & 0xff) << 8) |
				((bytes[offset+7] & 0xff))
				);
				*/
		return ByteBuffer.wrap(bytes, offset, 8).getLong();
	
	}//registersToLong



	
	/**
	 * 
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public static float readFloat32_BigEndian(byte[] bytes, int offset){
		return ByteBuffer.wrap(bytes, offset, 4).order(ByteOrder.BIG_ENDIAN).getFloat();
	}
	
	
	public static double readFloat64_BigEndian(byte[] bytes, int offset){
		return ByteBuffer.wrap(bytes, offset, 8).order(ByteOrder.BIG_ENDIAN).getDouble();
	}



	public static int BCDToInt(int value){
		return ((value>>4)&0x0F)*10+(value&0x0F);
	}

	/**
	 * bytes coding decimal. 6 bytes = 3 registers
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public static double readFloat_SOMESCA_BCD3S(byte[] bytes, int offset){
		int t1 = ByteUtilities.readInteger16(bytes, offset);
		int t2 = ByteUtilities.readInteger16(bytes, offset+2);
		int t3 = ByteUtilities.readInteger16(bytes, offset+4);

		double value;
		value=BCDToInt(t1)/(double)100+BCDToInt(t2)+(t3&0x01)*100+(t3&0x02)*200;
		if ((t3&0x10)==1){
			value=-value;
		}
		return value;
	}


	/**
	 * 6 bytes = 3 registers
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public static double readFloat_SOMESCA_BCD3U(byte[] bytes, int offset) {

		int v1 = ByteUtilities.readInteger16(bytes, offset);
		int v2 = ByteUtilities.readInteger16(bytes, offset+2);
		int v3 = ByteUtilities.readInteger16(bytes, offset+4);
		return (double) BCDToInt(v1)/(double)1000+BCDToInt(v2)/(double)10+BCDToInt(v3)*10;

	}


	/**
	 * 8 bytes = 4 registers
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public static double readFloat_SOMESCA_BCD4(byte[] bytes, int offset) {
		throw new RuntimeException("To be implemented");
	}


	/**
	 * 10 bytes = 5 registers
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public static double readFloat_SOMESCA_BCD5(byte[] bytes, int offset) {

		int v1 = ByteUtilities.readInteger16(bytes, offset);
		int v2 = ByteUtilities.readInteger16(bytes, offset+2);
		int v3 = ByteUtilities.readInteger16(bytes, offset+4);
		int v4 = ByteUtilities.readInteger16(bytes, offset+6);
		int v5 = ByteUtilities.readInteger16(bytes, offset+8);
		return
				(double) 
				BCDToInt(v1)/1000.0+
				BCDToInt(v2)/10.0+
				BCDToInt(v3)*10.0+
				BCDToInt(v4)*1000.0+
				BCDToInt(v5)*100000.0;
	}



	/**
	 * 
	 * @param bytes
	 * @param offset
	 * @param value
	 */
	public static void writeFloat32_BigEndian(byte[] bytes, int offset, double value){
		ByteBuffer.wrap(bytes, offset, 4).order(ByteOrder.BIG_ENDIAN).putFloat((float) value);
	}


	/**
	 * 
	 * @param bytes
	 * @param offset
	 * @param value
	 */
	public static void writeFloat64_BigEndian(byte[] bytes, int offset, double value){
		ByteBuffer.wrap(bytes, offset, 8).order(ByteOrder.BIG_ENDIAN).putDouble(value);
	}


	/**
	 * 
	 * @param bytes
	 * @param offset
	 * @param value
	 */
	public static void writeBooleanRegister(byte[] bytes, int offset, boolean value){
		// high byte
		if(value){	
			bytes[offset+0] = (byte) 0xff;
		}
		else{
			bytes[offset+0] = (byte) 0x00;
		}

		// low byte
		bytes[offset+1] = (byte) 0x00;	
	}

	/**
	 * 
	 * @param bytes
	 * @param offset
	 * @param value
	 */
	public static void writeBooleanDWORD(byte[] bytes, int offset, boolean value){
		// high byte
		if(value){	
			bytes[offset+0] = (byte) 0xff;
		}
		else{
			bytes[offset+0] = (byte) 0x00;
		}

		// low bytes
		bytes[offset+1] = (byte) 0x00;	
		bytes[offset+2] = (byte) 0x00;	
		bytes[offset+3] = (byte) 0x00;	
	}



	/**
	 * 
	 * @param bytes
	 * @param offset
	 * @param value
	 */
	public static void writeBooleanInByte(byte[] bytes, int offset, boolean value){
		// high byte
		if(value){	
			bytes[offset+0] = (byte) 0xff;
		}
		else{
			bytes[offset+0] = (byte) 0x00;		
		}
	}




	/**
	 * write on two bytes
	 * @param bytes
	 * @param offset
	 * @param value
	 */
	public static void writeInteger8(byte[] bytes, int offset, int value){
		bytes[offset] = (byte) (0xff & value);	
	}

	/**
	 * 
	 * @param offset
	 * @return
	 */
	public static int readUnsignedInt8(byte[] bytes, int offset) {
		byte b = bytes[offset+0];
		return Byte.toUnsignedInt(b);
	}


	/**
	 * Status is indicated as 1=ON and 0=OFF
	 * @param index: the coil adress
	 * @return
	 */
	public static boolean readCoil(byte[] bytes, int index){

		// euclidian division result
		int byteIndex = index/8;

		// euclidian division remainder
		int bitIndex = index%8;

		byte b = bytes[byteIndex];
		int value = (b>>>bitIndex) & 1;
		return value==1;
	}


	/**
	 * The status of outputs 27–20 is shown as the byte value CD hex, or binary 1100 1101.
	 * Output 27 is the MSB of this byte, and output 20 is the LSB.
	 * By convention, bits within a byte are shown with the MSB to the left, and the LSB to the right.
	 * Thus the outputs in the first byte are ‘27 through 20’, from left to right.
	 * The next byte has outputs ‘35 through 28’, left to right.
	 * As the bits are transmitted serially, they flow from LSB to MSB: 20 . . . 27, 28 . . . 35, and so on.
	 * In the last data byte, the status of outputs 38-36 is shown as the byte value 05 hex, or binary 0000 0101.
	 * Output 38 is in the sixth bit position from the left, and output 36 is the LSB of this byte.
	 * The five remaining high order bits are zero filled.
	 * Status is indicated as 1=ON and 0=OFF
	 * @param index: the coil adress
	 * @return
	 */
	public static boolean readCoil(byte[] bytes, int byteIndex, int bitIndex){

		// euclidian division result
		byteIndex += bitIndex/8;

		// euclidian division remainder
		bitIndex = bitIndex%8;

		byte b = bytes[byteIndex];
		int value = (b>>>bitIndex) & 1;
		return value==1;
	}



	/**
	 * 
	 * @param bytes
	 * @param offset
	 * @param bitIndex
	 * @param blockSize: byte count of the block
	 * @return
	 */
	public static boolean readCoilInBlock(byte[] bytes, int offset, int bitIndex, int blockSize){

		// euclidian division result
		int byteShift = bitIndex/8;

		// euclidian division remainder
		bitIndex = bitIndex%8;

		// big endian style
		byte b = bytes[offset+blockSize-byteShift];

		int value = (b>>bitIndex) & 1;
		return value==1;
	}

	/**
	 * DWORD : 32 bit
	 * @param bytes
	 * @param offset
	 * @param bitIndex
	 * @return
	 */
	public static boolean readDWORDCoil(byte[] bytes, int offset, int bitIndex){

		// euclidian division result
		int byteShift = bitIndex/8;

		// euclidian division remainder
		bitIndex = bitIndex%8;

		// big endian style
		byte b = bytes[offset+3-byteShift];

		int value = (b>>bitIndex) & 1;
		return value==1;
	}


	/**
	 * 
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public static boolean readDWORDBoolean(byte[] bytes, int offset){
		int value = ByteUtilities.readInteger32(bytes, offset);
		return value==1;	
	}


	/**
	 * Status is indicated as 1=ON and 0=OFF
	 * @param index: the coil adress
	 * @return
	 */
	public static void writeCoil(byte[] bytes, int index, boolean value){

		// euclidian division result
		int byteIndex = index/8;

		// euclidian division remainder
		int bitIndex = index%8;

		// write coil
		if(value){
			bytes[byteIndex] |= (1 << bitIndex); // set a bit to 1
		}
		else{
			bytes[byteIndex] &= ~(1 << bitIndex); // set a bit to 0
		}
	}


	/**
	 * Status is indicated as 1=ON and 0=OFF
	 * @param index: the coil adress
	 * @return
	 */
	public static void writeCoil(byte[] bytes, int byteIndex, int bitIndex, boolean value){

		// euclidian division result
		byteIndex += bitIndex/8;

		// euclidian division remainder
		bitIndex = bitIndex%8;

		// write coil
		if(value){
			bytes[byteIndex] |= (1 << bitIndex); // set a bit to 1
		}
		else{
			bytes[byteIndex] &= ~(1 << bitIndex); // set a bit to 0
		}
	}


	/**
	 * 
	 * @param b
	 * @return
	 */
	public static String getByteBinaryString(byte b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 7; i >= 0; --i) {
			sb.append(b >>> i & 1);
		}
		return sb.toString();
	}

	
	
	public static boolean equals(byte[] bytes, int offset, byte[] sequence) {
		int n = sequence.length;
		if(offset+n>bytes.length){
			return false;
		}
		for(int i=0; i<n; i++){
			if(bytes[offset+i]!=sequence[i]){
				return false;
			}
		}
		return true;
	}
	



}
