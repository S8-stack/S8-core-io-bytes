package com.qx.level0.utilities.bytes;

import java.io.IOException;

/**
 * Can parse from a <code>ByteInflow</code>
 * @author pc
 *
 */
public interface ByteSerializable {

	/**
	 * parse from inflow
	 * 
	 * @param inflow
	 * @throws IOException
	 */
	public void deserialize(ByteInflow inflow) throws IOException;
	
	
	
	/**
	 * 
	 * @param outflow
	 * @throws IOException
	 */
	public void serialize(ByteOutflow outflow) throws IOException;
	
}
