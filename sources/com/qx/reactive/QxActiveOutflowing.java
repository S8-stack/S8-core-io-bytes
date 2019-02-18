package com.qx.reactive;

import java.nio.ByteBuffer;
import java.util.Deque;
import java.util.Queue;

/**
 */
@Deprecated
public interface QxActiveOutflowing {

	
	/**
	 * <p>
	 * General scenario is the following: the calling object is keeping track of 
	 * <code>ByteChain</code> HEAD and TAIL.
	 * <ul>
	 * <li>HEAD is used for consuming the data, through the use of <code>next()</code></li>
	 * <li>TAIL allows to append further nodes by passing this tail to <code>pull<
	 * @param outflow: the HEAD of the chain this <code>QxActiveOutflowing</code> is supposed to write to.
	 * Note that, on the other side
	 * @return
	 */
	public boolean pull(Queue<ByteBuffer> outflow, int defaultBufferCapacity);
	
	
	
}
