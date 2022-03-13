package com.s8.io.bytes;

import java.io.IOException;



/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class AutoByteOutflow extends BaseByteOutflow {


	public final static int N_RETRIES = 8;
	

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
	
	
	@Override
	protected void prepare(int nBytes) throws IOException {
		if(buffer.remaining()<nBytes) {
			int iTry = 0;
			boolean isPushSuccessful = false;
			while(buffer.remaining()<nBytes){
				isPushSuccessful = push();
				iTry = isPushSuccessful ? 0 : iTry+1;
				if(iTry>N_RETRIES) {
					throw new IOException("Max number of retries without push success exceed");
				}
			}
		}
	}
	
	
	/**
	 * 
	 * @param delta
	 * @throws IOException 
	 */
	public void shift(int delta) throws IOException {
		
		prepare(delta);
		
		// shift position
		buffer.position(buffer.position() + delta);
		
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
}
