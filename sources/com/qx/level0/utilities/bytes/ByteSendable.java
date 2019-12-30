package com.qx.level0.utilities.bytes;

import java.io.IOException;

/**
 * Can be put on a <code>ByteOutflow</code>
 * 
 * @author pc
 *
 */
public interface ByteSendable {

	
	/**
	 * Compose and cast all double to float, all long to int
	 * @param outflow
	 * @throws IOException
	 */
	public void send(ByteOutflow outflow) throws IOException;
}
