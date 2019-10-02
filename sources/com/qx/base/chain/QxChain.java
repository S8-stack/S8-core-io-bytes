package com.qx.base.chain;

import java.io.IOException;


/**
 * 
 * @author pc
 *
 */
public class QxChain {

	/**
	 * 
	 * @author pc
	 *
	 */
	public class Link {

		private QxChainable object;

		/**
		 * next block
		 */
		private Link next;


		/**
		 * previous block
		 */
		private Link previous;
		
		

		public Link(QxChainable object) {
			super();
			this.object = object;
			object.setLink(this);
		}

	
		
		public void setLatest() {
			QxChain.this.setLatest(this);
		}

		/**
		 * Chain method for efficient storage
		 */
		private void pop(){
			if(next!=null){
				next.previous = previous;
			}
			if(previous!=null){
				previous.next = next;
			}
			previous = null;
			next = null;
		}


		/**
		 * 
		 * @param node
		 */
		/*
		private void insertAfter(Link node){
			if(next!=null){
				next.previous = node;
			}
			node.next = next;
			next = node;
			node.previous = this;
		}
		*/


		/**
		 * 
		 * @param node
		 */
		private void insertBefore(Link node){
			//From previous<->this to previous<->node<->this

			if(previous!=null){
				previous.next = node;
			}
			node.previous = previous;
			this.previous = node;
			node.next = this;
		}

	}


	/**
	 * <p><code>Chainable</code> is keeping a reference to a <code>Link</code></p>
	 * @author pc
	 *
	 */
	public interface QxChainable {

		/**
		 * 
		 * @param link
		 */
		public void setLink(Link link);

		/**
		 * 
		 * @return
		 */
		public Link getLink();
		
		/**
		 * To be called when dropped. Typical use case:
		 * <ul>
		 * <li><code>save();</code></li>
		 * <li>remove from mapping: <code>buckets.remove(bucket.getPath());</code></li>
		 * </ul>
		 */
		public void dispose();
		
	}

	/**
	 * most recently used
	 */
	private Link head = null;

	/**
	 * least recently used
	 */
	private Link tail = null;

	/**
	 * number of elements
	 */
	private int nLinks = 0;
	
	public QxChain() {
		super();
	}

	public int size() {
		return nLinks;
	}

	/**
	 * Insert in chain set as new head.
	 * Modify size of the chain.
	 * @param element
	 */
	public void push(QxChainable element) {
		push(new Link(element));
		nLinks++;
	}

	/**
	 * 
	 * @param bucket
	 */
	private void push(Link link){
		if(head!=null){
			head.insertBefore(link);
			head = link;
		}
		else{
			head = link;
			tail = link;
		}
	}




	/**
	 * Install blockHandler passed as argument as the new head of time chain
	 * 
	 * @param link
	 */
	private void setLatest(Link link) {
		// move to head
		if(link!=head){
			if(link==tail) {
				Link newTail = link.previous;
				link.pop();
				tail = newTail;
			}
			else {
				link.pop();	
			}

			push(link);
			//displayMappedBuckets("swap");
		}
	}
	
	
	
	public void trim(int nLinks) {
		while(this.nLinks>nLinks) {
			dropTail();
		}
	}
	
	/**
	 * Drop oldest nodes
	 * @throws IOException 
	 * @throws BkException 
	 * @throws Exception 
	 * 
	 */
	public void dropTail() {
		Link link = tail;

		link.object.dispose();

		// adjust list
		Link newTail = tail.previous;
		tail.pop();
		tail = newTail;
		if(tail==null){
			head = null;
		}
		nLinks--;
	}

	
	public static interface LinkConsumer {
		
		/**
		 * 
		 * @param object
		 * @return a flag indicating if traversing is aborted
		 */
		public boolean consume(QxChainable object);
	}
	
	public boolean traverse(LinkConsumer consumer) {
		Link link = head;
		boolean isAborted = false;
		while(link!=null && !isAborted) {
			isAborted = consumer.consume(link.object);
			link = link.next;
		}
		return isAborted;
	}
}
