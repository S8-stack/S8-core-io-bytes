package com.qx.reactive;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import com.qx.reactive.SocketQxOutflow.Node;

/**
 * <h1>QxOutflow</h1>
 * <p><code>QxOutflow</code> is the counterpart of <code>QxInflow</code>.</p>
 * <p>The general idea of QxOutflow is to push data to a "never-ending" object 
 * similar to an <code>OutputStream</code>. But this object has to be made 
 * compliant with Asynchronous way of doing things, namely split everyting 
 * in <code>ByteBuffer</code>s. That's why <code>QxOutlfow</code> builds under the hood
 * a synchronized linked queue of <code>ByteBuffer</code>.
 * </p>
 * <p>
 * Two different behaviours can be implemented regarding the becoming of these buffers:
 * <ul>
 * <li><b>Store them</b>: <code>LinkedQxOutflow</code> build a chain of node containing 
 * the <code>ByteBuffer</code> but do not consume them.</li>
 * <li><b>Push them directly so a channel</b>:  <code>SocketQxOutflow</code> is implementing
 * this.</li>
 * </ul>
 * 
 * @author pc
 *
 */
public interface QxOutflow {
	
	public class Node {

		public ByteBuffer buffer;

		public AtomicBoolean isOutflowable;

		public Node next;

		public Node() {
			super();
			buffer = ByteBuffer.allocate(capacity);
			isOutflowable = new AtomicBoolean(false);
		}
	}
	
	private int capacity;
	
	public QxOutflow(int capacity) {
		super();
		this.capacity = capacity;
	}
	

	public void push(QxOutflow value);
	
	public void push(byte[] value);
	
	public void push(short value);
	
	public void push(int value);
	
	public void push(long value);
	
	public void push(float value);
	
	public void push(double value);
	

}
