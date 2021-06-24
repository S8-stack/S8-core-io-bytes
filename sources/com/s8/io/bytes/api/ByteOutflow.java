package com.s8.io.bytes.api;

import java.io.IOException;

import com.s8.alpha.objects.S8Address;



/**
 * 
 * <p>
 * /!\ All array or String lengths are encoded as UInt32 to reflect their
 * maximum possible length in JAVA.
 * </p>
 * 
 * @author pc
 *
 */
public interface ByteOutflow {


	/**
	 * Write bytes array. Auto-feed underlying ByteBuffer as necessary.
	 * @param bytes
	 * @throws IOException
	 */
	public void putByteArray(byte[] bytes) throws IOException;

	
	/**
	 * 
	 * @param array
	 * @param offset
	 * @param length
	 * @throws IOException 
	 */
	public void putByteArray(byte[] array, int offset, int length) throws IOException;

	
	/**
	 * 
	 * @param head
	 * @throws IOException
	 */
	/*
	public default void putBytes(QxBytes head) throws IOException{
		while(head!=null) {
			putByteArray(head.bytes, head.offset, head.length);
			head = head.next;
		}
	}
	*/
	

	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * @param flags
	 * @throws IOException
	 */
	public void putFlags8(boolean[] flags) throws IOException;


	/**
	 * Hack for js part
	 * 
	 * @param value
	 * @throws IOException
	 */
	public default void putBoolean(boolean value) throws IOException {
		putUInt8(value?((byte) 32):((byte) 33));
	}
	

	/**
	 * Directly put byte
	 * @param b
	 * @throws IOException
	 */
	void putByte(byte b) throws IOException;
	


	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * @param value
	 * @throws IOException
	 */
	public void putUInt8(int value) throws IOException;


	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * @param value
	 * @throws IOException
	 */
	public void putUInt16(int value) throws IOException;
	


	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * @param value
	 * @throws IOException
	 */
	public void putInt16(short value) throws IOException;



	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * @param value
	 * @throws IOException
	 */
	public void putUInt32(int value) throws IOException;
	
	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * This format is adequate to send JAVA long to JS (since JS MAX_INTEGER is 2^53).
	 * @param value
	 * @throws IOException
	 */
	public void putUInt53(long value) throws IOException;

	
	/**
	 * put Uint32 using extensible mechanism to save bytes.
	 * Mechanism is compliant with natural JAVA and network Big-Endianness.
	 * @param value
	 * @throws IOException
	 */
	public void putUInt(int value) throws IOException;
	
	
	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * @param value
	 * @throws IOException
	 */
	public void putInt32(int value) throws IOException;


	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * @param value
	 * @throws IOException
	 */
	public void putInt64(long value) throws IOException;



	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * @param value
	 * @throws IOException
	 */
	public void putFloat32(float value) throws IOException;




	/**
	 * Auto-feed underlying ByteBuffer as necessary.
	 * @param value
	 * @throws IOException
	 */
	public void putFloat64(double value) throws IOException;



	/**
	 * String is encoded in UTF8.
	 * 
	 * @return String
	 * @throws IOException 
	 */
	public void putL32StringUTF8(String str) throws IOException;

	
	/**
	 * 
	 * @param str
	 * @throws IOException
	 */
	public void putL8StringASCII(String str) throws IOException;

	
	
	/**
	 * 
	 * @param strs
	 * @throws IOException
	 */
	public default void putS8Address(S8Address address) throws IOException {
		putByteArray(address.bytes);
	}
	
	/**
	 * 
	 * @param strs
	 * @throws IOException
	 */
	public void putS8Index(long key) throws IOException;
}
