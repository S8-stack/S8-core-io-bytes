package com.qx.back.base.bytes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 * 
 * @author pc
 *
 */
public class FileByteOutflow implements ByteOutflow {

	private FileChannel channel;

	private ByteBuffer buffer;

	public FileByteOutflow(FileChannel channel, int bufferingSize) {
		super();
		this.channel = channel;
		buffer = ByteBuffer.allocate(bufferingSize);
	}


	public final static int N_RETRIES = 8;

	/**
	 * 
	 * @param nBytes: MUST not exceed buffer capacity (otherwise impossible to satisfy)
	 * @throws IOException
	 */
	private void ensure(int nBytes) throws IOException {
		if(nBytes>buffer.remaining()) {
			push();
		}
	}

	
	public void push() throws IOException {
		buffer.flip();
		
		int nRetries = 0;
		while(buffer.hasRemaining() && nRetries<8) {
			channel.write(buffer);	
			nRetries++;
		}
		if(buffer.hasRemaining()) {
			throw new IOException("Failed to write buffer");
		}
		buffer.clear();
	}
	
	
	@Override
	public void putByteArray(byte[] bytes) throws IOException {
		int offset = 0, length=bytes.length;
		boolean isWritten = false;
		while(!isWritten) {
			int nWritables = Math.min(buffer.remaining(), length-offset);
			buffer.put(bytes, offset, nWritables);
			offset+=nWritables;
			if(offset==length) {
				isWritten = true;
			}
			else {
				push();
			}
		}
		
	}


	@Override
	public void putFlags(boolean[] flags) throws IOException {
		ensure(1);
		byte b = 0x00;
		if(flags[0]) { b|= 0x01; }
		if(flags[1]) { b|= 0x02; }
		if(flags[2]) { b|= 0x04; }
		if(flags[3]) { b|= 0x08; }
		if(flags[4]) { b|= 0x10; }
		if(flags[5]) { b|= 0x20; }
		if(flags[6]) { b|= 0x40; }
		if(flags[7]) { b|= 0x80; }
		buffer.put(b);
	}
	

	@Override
	public void putUInt8(int value) throws IOException {
		ensure(1);
		buffer.put((byte) (0xff & value));
	}

	@Override
	public void putInt16(short value) throws IOException {
		ensure(2);
		buffer.putShort(value);
	}


	@Override
	public void putUInt16(int value) throws IOException {
		ensure(2);
		buffer.put((byte) (0xff & (value >> 8)));
		buffer.put((byte) (0xff & value));
	}
	
	@Override
	public void putUInt32(int value) throws IOException {
		ensure(4);
		buffer.put((byte) (0x7f & (value >> 24)));
		buffer.put((byte) (0xff & (value >> 16)));
		buffer.put((byte) (0xff & (value >> 8)));
		buffer.put((byte) (0xff & value));
	}
	

	@Override
	public void putUInt32Array(int[] array) throws IOException {
		int length = array.length;
		putUInt32(length);

		for(int i=0; i<length; i++) {
			putUInt32(array[i]);
		}
	}


	@Override
	public void putInt32(int value) throws IOException {
		ensure(4);
		buffer.putInt(value);
	}


	@Override
	public void putInt32Array(int[] array) throws IOException {
		int length = array.length;
		putUInt32(length);

		for(int i=0; i<length; i++) {
			putInt32(array[i]);
		}
	}


	@Override
	public void putInt64(long value) throws IOException {
		ensure(8);
		buffer.putLong(value);
	}

	
	@Override
	public void putInt64Array(long[] array) throws IOException {
		int length = array.length;
		putUInt32(length);

		for(int i=0; i<length; i++) {
			putInt64(array[i]);
		}
	}


	@Override
	public void putFloat32(float value) throws IOException {
		ensure(4);
		buffer.putFloat(value);
	}

	@Override
	public void putFloat32Array(float[] array) throws IOException {
		// write byte
		int length = array.length;
		putUInt32(length);

		for(int i=0; i<length; i++) {
			putFloat32(array[i]);
		}
	}
	
	@Override
	public void putFloat64(double value) throws IOException {
		ensure(8);
		buffer.putDouble(value);
	}

	@Override
	public void putFloat64Array(double[] array) throws IOException {
		// write byte
		int length = array.length;
		putUInt32(length);

		for(int i=0; i<length; i++) {
			putFloat64(array[i]);
		}
	}


	/**
	 * Max <code>String</code> length is 65536
	 * @return String
	 * @throws IOException 
	 */
	@Override
	public void putStringUTF8(String str) throws IOException {

		// write byte
		int length = str.length();
		putUInt32(length);

		// put all bytes
		ensure(length);
		byte[] bytes = str.getBytes();
		buffer.put(bytes);
	}


}
