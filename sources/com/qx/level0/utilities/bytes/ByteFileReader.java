package com.qx.level0.utilities.bytes;

import java.io.IOException;

public interface ByteFileReader {
	
	/**
	 * 
	 * @param inflow
	 * @return true if inflow has been successfully consumed
	 * @throws IOException 
	 */
	public abstract void consume(ByteInflow inflow) throws IOException;


	public abstract void onFileDoesNotExist();
	
	public abstract void onIOException(IOException exception);
	
}
