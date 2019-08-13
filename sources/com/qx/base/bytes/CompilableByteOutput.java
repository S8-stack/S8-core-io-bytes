package com.qx.base.bytes;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author pc
 *
 */
public class CompilableByteOutput extends AutoByteOutput {

	public final static int CAPACITY = 1024;
	
	private int capacity;

	private List<ByteBuffer> filled;


	public CompilableByteOutput() {
		super();
		this.capacity = CAPACITY;
		filled = new ArrayList<>();
	}

	
	public CompilableByteOutput(int capacity) {
		super();
		this.capacity = capacity;
		filled = new ArrayList<>();
	}

	
	@Override
	protected void feed() {
		buffer = ByteBuffer.allocate(capacity);
	}
	
	
	@Override
	protected void dump() {
		if(buffer!=null) {
			buffer.flip();
			filled.add(buffer);
			buffer = null;
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public byte[] compile(){

		// close
		dump();

		int size=0;
		for(ByteBuffer buffer : filled){
			size+=buffer.limit();
		}
		byte[] bytes = new byte[size];
		int offset=0, length;
		for(ByteBuffer buffer : filled){
			length = buffer.limit();
			buffer.get(bytes, offset, length);
			offset+=length;
		}
		return bytes;
	}
}
