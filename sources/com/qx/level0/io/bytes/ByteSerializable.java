package com.qx.level0.io.bytes;

import java.io.IOException;

/**
 * Can parse from a <code>ByteInflow</code> and compose to a <code>ByteOutflow</code>.
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
