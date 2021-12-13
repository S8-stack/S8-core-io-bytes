package com.s8.blocks.helium;

import java.io.IOException;

import com.s8.alpha.utilities.bytes.ByteInflow;

public abstract class BaseByteInflow implements ByteInflow {
	



	@Override
	public int getUInt24() throws IOException {
		// byte array is automatically handling the buffer feeding
		byte[] bytes = getByteArray(3); 
		return (bytes[1] & 0xff) << 16 | 
				(bytes[2] & 0xff) << 8 | 
				(bytes[3] & 0xff);
	}
	

	@Override
	public int getUInt31() throws IOException {
		// byte array is automatically handling the buffer feeding
		byte[] bytes = getByteArray(4);
		return (int) (
				(bytes[0] & 0x7f) << 24 | 
				(bytes[1] & 0xff) << 16 | 
				(bytes[2] & 0xff) << 8 | 
				(bytes[3] & 0xff));
	}

	
	@Override
	public long getUInt32() throws IOException {
		// byte array is automatically handling the buffer feeding
		byte[] bytes = getByteArray(4);
		return (long) (
				(bytes[0] & 0xffL) << 24 | 
				(bytes[1] & 0xffL) << 16 | 
				(bytes[2] & 0xffL) << 8 | 
				(bytes[3] & 0xffL));
	}
	


	@Override
	public long getUInt40() throws IOException {
		// byte array is automatically handling the buffer feeding
		byte[] bytes = getByteArray(5);
		return (bytes[0] & 0xffL) << 32 | 
				(bytes[1] & 0xffL) << 24 | 
				(bytes[2] & 0xffL) << 16 | 
				(bytes[3] & 0xffL) << 8 | 
				(bytes[4] & 0xffL);
	}


	@Override
	public long getUInt48() throws IOException {
		// byte array is automatically handling the buffer feeding
		byte[] bytes = getByteArray(6);
		return (bytes[0] & 0xffL) << 40 | 
				(bytes[1] & 0xffL) << 32 | 
				(bytes[2] & 0xffL) << 24 | 
				(bytes[3] & 0xffL) << 16 | 
				(bytes[4] & 0xffL) << 8 | 
				(bytes[5] & 0xffL);
	}


	@Override
	public long getUInt56() throws IOException {
		// byte array is automatically handling the buffer feeding
		byte[] bytes = getByteArray(7);
		return (bytes[0] & 0xffL) << 48 | 
				(bytes[1] & 0xffL) << 40 | 
				(bytes[2] & 0xffL) << 32 | 
				(bytes[3] & 0xffL) << 24 | 
				(bytes[4] & 0xffL) << 16 | 
				(bytes[5] & 0xffL) << 8 | 
				(bytes[6] & 0xffL);
	}


	@Override
	public long getUInt63() throws IOException {
		// byte array is automatically handling the buffer feeding
		byte[] bytes = getByteArray(8);
		return (bytes[0] & 0x7fL) << 56 | 
				(bytes[1] & 0xffL) << 48 | 
				(bytes[2] & 0xffL) << 40 | 
				(bytes[3] & 0xffL) << 32 | 
				(bytes[4] & 0xffL) << 24 | 
				(bytes[5] & 0xffL) << 16 | 
				(bytes[6] & 0xffL) << 8 | 
				(bytes[7] & 0xffL);
	}

}
