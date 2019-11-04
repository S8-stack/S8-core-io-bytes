package com.qx.level0.utilities.chain;

import java.io.IOException;


/**
 * 
 * @author pc
 *
 */
public class QxSynchronizedChain {


	private class Link {

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
			object.setChainLinkHandle(new QxChainLinkHandle() {

				@Override
				public Object previous() {
					return previous;
				}

				@Override
				public Object next() {
					return next;
				}

				@Override
				public void detach() {
					Link.this.detach();
				}

				@Override
				public void moveFirst() {
					Link.this.moveFirst();
				}

				@Override
				public void moveLast() {
					Link.this.moveLast();
				}
			});
		}


		/**
		 * Chain method for efficient storage
		 */
		public void detach(){
			synchronized (lock) {
				if(this==head){ // update head
					head = next;
				}
				
				if(this==tail){ // update tail
					tail = previous;
				}
				
				if(next!=null){ // update next
					next.previous = previous;
				}
				
				if(previous!=null){ // update previous
					previous.next = next;
				}
				
				object.onChainLinkDetached();	
			}
		}


		/**
		 * 
		 * @param node
		 */
		
		private void insertAfter(Link node){
			synchronized (lock) {
				if(next!=null){
					next.previous = node;
				}
				node.next = next;
				next = node;
				node.previous = this;	
			}
		}
		

		/**
		 * 
		 * @param node
		 */
		public void insertBefore(Link node){
			//From previous<->this to previous<->node<->this

			synchronized (lock) {
				if(previous!=null){
					previous.next = node;
				}
				node.previous = previous;
				this.previous = node;
				node.next = this;	
			}
			
		}

		
		/**
		 * Install blockHandler passed as argument as the new head of time chain
		 * 
		 * @param link
		 */
		private void moveFirst() {
			synchronized (lock) {
				// move to head
				if(this!=head){
					if(this==tail) {
						Link newTail = previous;
						detach();
						tail = newTail;
					}
					else {
						detach();	
					}

					pushFirst();
				}	
			}
			
		}
		
		private void moveLast() {
			synchronized (lock) {
				// move to head
				if(this!=tail){
					detach();
					pushLast();
				}	
			}
		}
		
		
		/**
		 * 
		 * @param bucket
		 */
		private void pushFirst(){
			synchronized (lock) {
				if(head!=null){
					head.insertBefore(this);
					head = this;
				}
				else{
					head = this;
					tail = this;
				}	
			}
		}
		
		
		private void pushLast(){
			synchronized (lock) {
				if(tail!=null){
					tail.insertAfter(this);
					tail = this;
				}
				else{
					head = this;
					tail = this;
				}	
			}
		}
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

	
	private Object lock = new Object();
	
	public QxSynchronizedChain() {
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
	public void pushFirst(QxChainable element) {
		new Link(element).pushFirst();
		nLinks++;
	}
	
	public void pushLast(QxChainable element) {
		new Link(element).pushLast();
		nLinks++;
	}


	public void trim(int nLinks) {
		while(this.nLinks>nLinks) {
			removeLast();
		}
	}

	/**
	 * Drop oldest nodes
	 * @throws IOException 
	 * @throws BkException 
	 * @throws Exception 
	 * 
	 */
	public void removeLast() {
		tail.detach();
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
