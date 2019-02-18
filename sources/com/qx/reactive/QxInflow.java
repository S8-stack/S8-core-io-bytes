package com.qx.reactive;

import java.nio.ByteBuffer;

/**
 * <h1>QxInflow</h1>
 * <p>
 * This interface defines by itself an extremely efficient programming pattern. It builds on 
 * reactive programming and SAX parsing.
 * </p>
 * <p>
 * The general pattern is the following: let's say we have a source of IO (basically File or Socket). 
 * Lots of works have already been done to demonstrate that:
 * <ul>
 * <li><b>asynchronous</b> programming is the solution for overcoming blocking IO, bringing unmatched
 * scalability with moderate amounts of resources</li>
 * <li><b>SAX</b> parsing is the best approach since it is directly compatible with single pass, 
 * <bold>streamlined</b> reading. SAX stateful parsing is probably the fastest, memory saving, easy 
 * to code and to review way of dealing with streams of data.
 * </li>
 * </ul>
 * Good luck is: they share the same general 
 * concept of being driven by the source of data, rather than willing to request things from the 
 * data source. In other words, they are both passive to the data, rather than requesting 
 * pro-actively the data.
 * </p>
 * <p>
 * On top of that, java NIO has been built around the <code>ByteBuffer</code> object which proves 
 * to be well-designed for the purposes of dealing with asynchronous resources. 
 * That's why, quite naturally, we will build on it. This
 * ensures good compliance AcyncChannel like, for instance, <code>AsynchronousSocketChannel</code>.
 * </p>
 * <p>
 * The general idea of a <code>QxIOReactive</code> is to be a node in a IO handling structure. 
 * It can be directly a parser, or equally a state of this parser. 
 * In fact, it is a good pattern to have a cascading construction based on <code>QxIOReactive</code> objects,
 * each of them responsible for a given scope of stream analysis. And these different scopes might very well
 * be embedded the ones within the others, leading to cascading.
 * </p>
 * <p>
 * It is natural for <code>QxIOReactive</code> implementation to delegate the return of the 
 * termination flag to other event-listening methods. See below for a simple scenario:
 * </p>
 * <pre>
 *	public class QxIOReactive {
 * 	
 * 		private SomeContextClass context;
 * 	
 * 		public boolean onBytes(ByteBuffer buffer){
 * 
 * 			if(specific event occurred){
 * 				// cannot decide within this class if aborted
 *				// but context can 
 * 				return context.onThisSpecificEvent(..params..);
 * 			}
 * 
 * 			return false; // default OK
 * 		}
 * }
 * </pre>
 * @author pc
 *
 */
public interface QxInflow {
	
	
	public interface Context {
		
		public void switchState(QxInflow inflow);
		
	}
	
	/**
	 * <p>
	 * Note that the returned flag IS NOT an error throwing pattern.
	 * Errors must be thrown through the on<Type>Error() on the underlying
	 * parser/composer.
	 * </p>
	 * @param inflow: the input buffer submitted to this reactive.
	 * @return a flag indicating if the reading/writing has been aborted and 
	 * cannot be further resumed with the same parsing/composing state as <code>
	 * this</code> one.
	 * 
	 */
	public boolean on(ByteBuffer buffer);
	
	
	/**
	 * 
	 * @author pc
	 *
	 */
	public abstract class PullInteger implements QxInflow {
		
		public PullInteger(int value, QxInflow callback) {
			super();
		}
		
		private InputByteArray bytes = new InputByteArray(4);

		@Override
		public boolean on(ByteBuffer buffer) {
			
			if(bytes.push(buffer)) {
				onRead(ByteBuffer.wrap(bytes.getArray()).getInt());
			}
			return false;
		}
		
		
		/**
		 * Do state switching here.
		 * @param value
		 */
		public abstract void onRead(int value);
	}
	
}
