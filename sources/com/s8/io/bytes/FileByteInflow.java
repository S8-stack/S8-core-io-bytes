package com.s8.io.bytes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

import com.s8.api.io.ByteInflow;


public class FileByteInflow implements ByteInflow {

	private FileChannel channel;

	private ByteBuffer buffer;

	private boolean isEndOfFileReached;

	public FileByteInflow(FileChannel channel,  int bufferingSize) throws IOException {
		super();
		this.channel = channel;
		buffer = ByteBuffer.allocate(bufferingSize);
	}


	private final static int N_READ_RETRY = 8;


	public void pull() throws IOException {
		int nBytesRead = channel.read(buffer);
		if(nBytesRead==-1) {
			isEndOfFileReached = true;
		}
		buffer.flip();
	}
	

	private void ensure(int nBytes) throws IOException {
		if(nBytes>buffer.remaining()) {
			buffer.compact();        	
			int retryIndex = 0;
			while(nBytes>buffer.position() && retryIndex<N_READ_RETRY && !isEndOfFileReached) {
				int nBytesRead = channel.read(buffer);
				if(nBytesRead==-1) {
					isEndOfFileReached = true;
				}
				retryIndex++;
			}
			if(nBytes>buffer.position()) {
				throw new IOException("Failed to read from channel");
			}
			buffer.flip();
		}
	}


	private final static int N_RETRIEVE_TRYOUTS = 64;


	@Override
	public byte getByte() throws IOException {
		ensure(1);
		return buffer.get();
	}


	@Override
	public byte[] getByteArray(int length) throws IOException {
		byte[] bytes = new byte[length];
		int index = 0, n;
		boolean isRetrieved = false;
		int tryIndex = 0;
		while(!isRetrieved && !isEndOfFileReached && tryIndex<N_RETRIEVE_TRYOUTS) {
			n = Math.min(buffer.remaining(), length-index);
			for(int i=0; i<n; i++) {
				bytes[index++] = buffer.get();
			}
			isRetrieved = (index==length);
			if(!isRetrieved) {
				buffer.clear();
				pull();
			}
			tryIndex++;
		}
		if(!isRetrieved) {
			throw new IOException("Failed to retrieve bytes array");
		}
		return bytes;
	}

	@Override
	public boolean isMatching(byte[] sequence) throws IOException {
		int length = sequence.length;
		byte[] bytes = getByteArray(length);
		for(int i=0; i<length; i++) {
			if(bytes[i]!=sequence[i]) {
				return false;
			}
		}
		return true;
	}


	public boolean[] getFlags8() throws IOException {
		ensure(1);
		boolean[] flags = new boolean[8];
		byte b = buffer.get();
		flags[0] = (b & 0x80) == 0x80;
		flags[1] = (b & 0x40) == 0x40;
		flags[2] = (b & 0x20) == 0x20;
		flags[3] = (b & 0x10) == 0x10;
		flags[4] = (b & 0x08) == 0x08;
		flags[5] = (b & 0x04) == 0x04;
		flags[6] = (b & 0x02) == 0x02;
		flags[7] = (b & 0x01) == 0x01;
		return flags;
	}


	@Override
	public int getUInt8() throws IOException {
		ensure(1);
		return buffer.get() & 0xff;
	}


	@Override
	public short getInt16() throws IOException {
		ensure(2);
		return buffer.getShort();
	}


	@Override
	public int getUInt16() throws IOException {
		ensure(2);
		byte b0 = buffer.get();
		byte b1 = buffer.get();
		return ((b0 & 0xff) << 8 ) | (b1 & 0xff);
	}


	@Override
	public int getUInt31() throws IOException {
		ensure(4);
		byte b0 = buffer.get();
		byte b1 = buffer.get();
		byte b2 = buffer.get();
		byte b3 = buffer.get();
		return (int) (
				(b0 & 0x7f) << 24 | 
				(b1 & 0xff) << 16 | 
				(b2 & 0xff) << 8 | 
				(b3 & 0xff));
	}

	public int getUInt32() throws IOException {
		ensure(4);
		byte[] bytes = getByteArray(4);
		return  (
				(bytes[0] & 0xff) << 24 | 
				(bytes[1] & 0xff) << 16 | 
				(bytes[2] & 0xff) << 8 | 
				(bytes[3] & 0xff));
	}
	
	
	@Override
	public int getUInt() throws IOException {
		ensure(1);
		byte b = buffer.get(); // first byte
		if((b & 0x80) == 0x80) {
			int value = b & 0x7f;
			ensure(1);
			b = buffer.get(); // second byte
			if((b & 0x80) == 0x80) {
				value = (value << 7) | (b & 0x7f);
				ensure(1);
				b = buffer.get(); // third byte
				if((b & 0x80) == 0x80) {
					value = (value << 7) | (b & 0x7f);
					ensure(1);
					b = buffer.get(); // fourth byte
					if((b & 0x80) == 0x80) {
						value = (value << 7) | (b & 0x7f);
						ensure(1);
						b = buffer.get(); // fifth byte (final one)
						return (value << 7) | (b & 0x7f);
					}
					else { // fourth byte is matching 0x7f mask
						return (value << 7) | b;
					}
				}
				else { // third byte is matching 0x7f mask
					return (value << 7) | b;
				}
			}
			else { // second byte is matching 0x7f mask
				return (value << 7) | b;
			}
		}
		else { // first byte is matching 0x7f mask
			return b;
		}
	}

	/*
	public void setUInt31(int index, int value) {
		bytes[index+0] = (byte) (0x7f & (value >> 24)); // high byte
		bytes[index+1] = (byte) (0xff & (value >> 16));
		bytes[index+2] = (byte) (0xff & (value >> 8));
		bytes[index+3] = (byte) (0xff & value); // low byte
	}
	 */

	@Override
	public int getInt32() throws IOException {
		ensure(4);
		return buffer.getInt();
	}

	
	@Override
	public int[] getInt32Array() throws IOException {
		// retrieve length
		int length = getUInt32();

		ensure(4*length);
		int[] array = new int[length];
		for(int i=0; i<length; i++) {
			array[i] = buffer.getInt();
		}
		return array;
	}


	@Override
	public long getInt64() throws IOException {
		ensure(8);
		return buffer.getLong();
	}


	@Override
	public long[] getInt64Array() throws IOException {
		// retrieve length
		int length = getUInt32();

		ensure(8*length);
		long[] array = new long[length];
		for(int i=0; i<length; i++) {
			array[i] = buffer.getLong();
		}
		return array;
	}
	
	@Override
	public float getFloat32() throws IOException {
		ensure(4);
		return buffer.getFloat();
	}


	@Override
	public float[] getFloat32Array() throws IOException {
		int length = getUInt32();

		ensure(4*length);
		float[] array = new float[length];
		for(int i=0; i<length; i++) {
			array[i] = buffer.getFloat();
		}
		return array;
	}
	
	
	@Override
	public double getFloat64() throws IOException {
		ensure(8);
		return buffer.getDouble();
	}

	
	@Override
	public double[] getFloat64Array() throws IOException {
		int length = getUInt32();

		ensure(8*length);
		double[] array = new double[length];
		for(int i=0; i<length; i++) {
			array[i] = buffer.getDouble();
		}
		return array;
	}


	/**
	 * 
	 * @return String
	 * @throws IOException 
	 */
	@Override
	public String getString() throws IOException {

		// read unsigned int
		int bytecount = getUInt();

		if(bytecount>=0) {
			// retrieve all bytes
			byte[] bytes = getByteArray(bytecount);	
			return new String(bytes, StandardCharsets.UTF_8);
		}
		else {
			return null;
		}
	}


	public boolean check(byte[] sequence) throws IOException {
		int length = sequence.length;
		byte[] bytes = getByteArray(length);
		for(int i=0; i<length; i++) {
			if(bytes[i]!=sequence[i]) {
				return false;
			}
		}
		return true;
	}

}
