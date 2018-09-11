package com.qx.io.html;

import java.io.IOException;
import java.io.Writer;

public abstract class HTML_Block {

	/**
	 * write the block to the writer
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public abstract void print(Writer writer) throws IOException;
}
