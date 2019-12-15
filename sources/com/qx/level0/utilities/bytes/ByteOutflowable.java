package com.qx.level0.utilities.bytes;

import java.io.IOException;

/**
 * Can be put on a <code>ByteOutflow</code>
 * 
 * @author pc
 *
 */
public interface ByteOutflowable {

	public void compose(ByteOutflow outflow) throws IOException;
}
