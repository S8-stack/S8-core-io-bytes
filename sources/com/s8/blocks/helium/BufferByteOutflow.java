package com.s8.blocks.helium;

import java.io.IOException;
import java.nio.ByteBuffer;

public class BufferByteOutflow extends BaseByteOutflow {



	public BufferByteOutflow(ByteBuffer buffer) {
		super();
		this.buffer = buffer;
	}


	@Override
	public void putFlags8(boolean[] flags) throws IOException {
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
	public void putByte(byte b) throws IOException{
		buffer.put(b);
	}

	@Override
	public void putUInt8(int value) throws IOException{
		buffer.put((byte) (value & 0xff));
	}

	@Override
	public void putUInt16(int value){
		buffer.putShort((short) (value & 0xffff));
	}


	@Override
	public void putUInt24(int value) throws IOException{
		buffer.put((byte) ((value>>16) & 0xff));
		buffer.put((byte) ((value>>8) & 0xff));
		buffer.put((byte) (value & 0xff));
	}

	
	@Override
	public void putUInt31(int value){
		buffer.put((byte) ((value>>24) & 0x7f));
		buffer.put((byte) ((value>>16) & 0xff));
		buffer.put((byte) ((value>>8) & 0xff));
		buffer.put((byte) (value & 0xff));
	}
	

	@Override
	public void putUInt32(long value){
		buffer.put((byte) ((value>>24) & 0xff));
		buffer.put((byte) ((value>>16) & 0xff));
		buffer.put((byte) ((value>>8) & 0xff));
		buffer.put((byte) (value & 0xff));
	}
	

	

	@Override
	public void putUInt40(long value) throws IOException {
		buffer.put((byte) ((value >> 32) & 0xffL));
		buffer.put((byte) ((value >> 24) & 0xffL));
		buffer.put((byte) ((value >> 16) & 0xffL));
		buffer.put((byte) ((value >> 8) & 0xffL));
		buffer.put((byte) (value & 0xff));
	}
	
	
	@Override
	public void putUInt48(long value) throws IOException {
		buffer.put((byte) ((value >> 40) & 0xffL));
		buffer.put((byte) ((value >> 32) & 0xffL));
		buffer.put((byte) ((value >> 24) & 0xffL));
		buffer.put((byte) ((value >> 16) & 0xffL));
		buffer.put((byte) ((value >> 8) & 0xffL));
		buffer.put((byte) (value & 0xff));
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
	public void putUInt56(long value) throws IOException {
		buffer.put((byte) ((value >> 48) & 0xffL));
		buffer.put((byte) ((value >> 40) & 0xffL));
		buffer.put((byte) ((value >> 32) & 0xffL));
		buffer.put((byte) ((value >> 24) & 0xffL));
		buffer.put((byte) ((value >> 16) & 0xffL));
		buffer.put((byte) ((value >> 8) & 0xffL));
		buffer.put((byte) (value & 0xff));
	}
	
	
	@Override
	public void putUInt64(long value) throws IOException {
		buffer.put((byte) ((value >> 56) & 0x7fL));
		buffer.put((byte) ((value >> 48) & 0xffL));
		buffer.put((byte) ((value >> 40) & 0xffL));
		buffer.put((byte) ((value >> 32) & 0xffL));
		buffer.put((byte) ((value >> 24) & 0xffL));
		buffer.put((byte) ((value >> 16) & 0xffL));
		buffer.put((byte) ((value >> 8) & 0xffL));
		buffer.put((byte) (value & 0xff));
	}




	@Override
	public void putInt16(short value) throws IOException {
		buffer.putShort(value);
	}


	@Override
	public void putInt32(int value) throws IOException {
		buffer.putInt((int) (value & 0x7fffffff));	
	}



	
	
	@Override
	public void putInt64(long value){
		buffer.putLong(value);
	}


	public void putFloat32(double value){
		putFloat32((float) value); 
	}

	@Override
	public void putFloat32(float value){
		buffer.putFloat(value);
	}

	@Override
	public void putFloat64(double value){
		buffer.putDouble(value);
	}




	@Override
	public void putByteArray(byte[] bytes) throws IOException {
		buffer.put(bytes);
	}

	@Override
	public void putByteArray(byte[] bytes, int offset, int length) throws IOException {
		buffer.put(bytes, offset, length);
	}

	@Override
	protected void prepare(int bytecount) throws IOException {
		// do nothing...
	}


	
}
