package com.s8.io.bytes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import com.s8.alpha.bytes.ByteOutflow;

public abstract class AutoByteOutflow implements ByteOutflow {


	public final static int N_RETRIES = 8;
	
	/**
	 * to be initialized by sub classes
	 */
	protected ByteBuffer buffer;

	public AutoByteOutflow() {
		super();
	}


	/**
	 * <p>
	 * Try to dump as much as possible from the buffer to the consumer (file,
	 * network, whatever).
	 * </p>
	 * <p>
	 * This method give no guarantee that bytes would actually been pushed, so ckeck
	 * and retry as many times as necessary.
	 * </p>
	 * @return the number of bytes actually pushed
	 * @throws IOException 
	 */
	protected abstract boolean push() throws IOException;
	
	
	private void prepare(int nBytes) throws IOException{
		if(buffer.remaining()<nBytes) {
			int iTry = 0;
			boolean isPushSuccessful = false;
			while(buffer.remaining()<nBytes){
				isPushSuccessful = push();
				iTry = isPushSuccessful?0:iTry+1;
				if(iTry>N_RETRIES) {
					throw new IOException("Max number of retries without push success exceed");
				}
			}	
		}
	}
	
	/**
	 * feed a new buffer and dump this one
	 * @throws IOException 
	 */
	/*
	private void push(){
		dump();
		feed();
	}
	*/

	public void sendBoolean(boolean b) throws IOException {
		prepare(1);
		if(b){
			buffer.put((byte) 32);	
		}
		else{
			buffer.put((byte) 33);	
		}
	}

	@Override
	public void putFlags8(boolean[] flags) throws IOException {
		prepare(1);
		byte b = 0;
		for(int i=0; i<7; i++){
			if(flags[i]){
				b = (byte) ((b | 1)<<1);
			}
			else{
				b = (byte) (b<<1);
			}
		}
		if(flags[7]){
			b = (byte) (b | 1);
		}
		buffer.put((byte) b);
	}
	

	@Override
	public void putByte(byte b) throws IOException {
		prepare(1);
		buffer.put(b);
	}

	@Override
	public void putUInt8(int value) throws IOException {
		prepare(1);
		buffer.put((byte) (value & 0xff));
	}

	@Override
	public void putUInt16(int value) throws IOException{
		prepare(2);
		buffer.putShort((short) (value & 0xffff));
	}


	@Override
	public void putInt16(short value) throws IOException {
		prepare(2);
		buffer.putShort(value);
	}


	@Override
	public void putUInt32(int value) throws IOException{
		prepare(4);
		buffer.putInt((int) (value & 0x7fffffff));
	}


	@Override
	public void putInt32(int value) throws IOException {
		prepare(4);
		buffer.putInt((int) (value & 0x7fffffff));	
	}


	@Override
	public void putInt64(long value) throws IOException {
		prepare(8);
		buffer.putLong(value);
	}
	
	@Override
	public void putUInt53(long value) throws IOException {
		buffer.put((byte) ((value>>48) & 0x1f)); // only 5 last bits
		buffer.put((byte) ((value>>40) & 0xff));
		buffer.put((byte) ((value>>32) & 0xff));
		buffer.put((byte) ((value>>24) & 0xff));
		buffer.put((byte) ((value>>16) & 0xff));
		buffer.put((byte) ((value>>8) & 0xff));
		buffer.put((byte) (value & 0xff));
	}

	@Override
	public void putUInt(int value) throws IOException {
		if(value<0x80) { // single byte encoding
			prepare(1);
			buffer.put((byte) (value & 0x7f));
		}
		else if(value<0x4000) { // two bytes encoding
			prepare(2);
			buffer.put((byte) (((value>>7) & 0x7f) | 0x80));
			buffer.put((byte) (value & 0x7f));
		}
		else if(value<0x200000) { // three bytes encoding
			prepare(3);
			buffer.put((byte) (((value>>14) & 0x7f) | 0x80));
			buffer.put((byte) (((value>>7) & 0x7f) | 0x80));
			buffer.put((byte) (value & 0x7f));
		}
		else if(value<0x10000000) { // four bytes encoding
			prepare(4);
			buffer.put((byte) (((value>>21) & 0x7f) | 0x80));
			buffer.put((byte) (((value>>14) & 0x7f) | 0x80));
			buffer.put((byte) (((value>>7) & 0x7f) | 0x80));
			buffer.put((byte) (value & 0x7f));
		}
		else { // five bytes encoding
			prepare(5);
			buffer.put((byte) (((value>>28) & 0x07) | 0x80));
			buffer.put((byte) (((value>>21) & 0x7f) | 0x80));
			buffer.put((byte) (((value>>14) & 0x7f) | 0x80));
			buffer.put((byte) (((value>>7) & 0x7f) | 0x80));
			buffer.put((byte) (value & 0x7f));
		}
	}
	


	public void putFloat32(double value) throws IOException {
		putFloat32((float) value); 
	}

	@Override
	public void putFloat32(float value) throws IOException {
		prepare(4);
		buffer.putFloat(value);
	}

	@Override
	public void putFloat64(double value) throws IOException {
		prepare(8);
		buffer.putDouble(value);
	}


	/**
	 * Default is UTF-8 (most compact solution).
	 * @param value
	 * @throws IOException
	 */
	@Override
	public void putL32StringUTF8(String value) throws IOException {
		if(value!=null){
			byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
			
			// we skip the first two bytes, but add to pass our own length
			int bytecount = bytes.length;
			/*
			if(length>2147483647){
				throw new IOException("String arg size is exceeding 2^31-1 (length is encoded in 4 bytes).");
			}
			*/
			putUInt(bytecount);

			putByteArray(bytes);
		}
		else{ // null
			putUInt32(0); // empty string
		}
	}

	

	@Override
	public void putL8StringASCII(String value) throws IOException{
		if(value!=null){
			byte[] bytes = value.getBytes(StandardCharsets.US_ASCII);

			// we skip the first two bytes, but add to pass our own length
			int bytecount = bytes.length;
			if(bytecount>256){
				throw new IOException("String arg size is exceeding 2^8 "
						+ "(length is encoded in 1 byte max).");
			}
			putUInt8(bytecount);

			putByteArray(bytes);
		}
		else{ // null
			putUInt8(0); // empty string
		}
	}
	


	@Override
	public void putByteArray(byte[] bytes) throws IOException {
		// /!\ No block allocation
		int offset = 0, length = bytes.length, remaining;
		while(length>0) {
			remaining = buffer.remaining();

			// not enough space
			if(remaining<length) {
				buffer.put(bytes, offset, remaining);
				length-=remaining;
				offset+=remaining;
				push();
			}
			// enough space to write remaining bytes
			else {
				buffer.put(bytes, offset, length);
				length=0;
			}
		}
	}
	
	
	@Override
	public void putByteArray(byte[] bytes, int offset, int length) throws IOException {
		// /!\ No block allocation
		int remaining;
		while(length>0) {
			remaining = buffer.remaining();

			// not enough space
			if(remaining<length) {
				buffer.put(bytes, offset, remaining);
				length-=remaining;
				offset+=remaining;
				push();
			}
			// enough space to write remaining bytes
			else {
				buffer.put(bytes, offset, length);
				length=0;
			}
		}
	}
	
	
	@Override
	public void putS8Index(long key) throws IOException {
		buffer.put((byte) ((key>>56) & 0x7f)); // only 5 last bits
		buffer.put((byte) ((key>>48) & 0xff)); // only 5 last bits
		buffer.put((byte) ((key>>40) & 0xff));
		buffer.put((byte) ((key>>32) & 0xff));
		buffer.put((byte) ((key>>24) & 0xff));
		buffer.put((byte) ((key>>16) & 0xff));
		buffer.put((byte) ((key>>8) & 0xff));
		buffer.put((byte) (key & 0xff));
	}


}
