package com.qx.level0.utilities.bytes;

import java.io.IOException;

/**
 * Can parse from a <code>ByteInflow</code>
 * @author pc
 *
 */
public interface ByteInflowable {

	/**
	 * parse from inflow
	 * 
	 * @param inflow
	 * @throws IOException
	 */
	public void parse(ByteInflow inflow) throws IOException;
	
}
