package com.qx.level0.utilities.chain;

import java.io.IOException;


/**
 * 
 * @author pc
 *
 */
public class QxChain<T extends QxChainable<T>> {


	private class Link implements QxChainLinkHandle<T> {

		private T object;

		/**
		 * next block
		 */
		private Link next;


		/**
		 * previous block
		 */
		private Link previous;

		public Link(T object) {
			super();
			this.object = object;
			object.setChainLinkHandle(this);
		}


		@Override
		public T getObject() {
			return object;
		}

		/**
		 * Chain method for efficient storage
		 */
		@Override
		public void detach(){
			
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


		/**
		 * From this<->next to this<->node<->next
		 * @param node
		 */
		
		private void insertAfter(Link node){
			if(next!=null){
				next.previous = node;
			}
			node.next = next;
			next = node;
			node.previous = this;
		}
		

		/**
		 * From previous<->this to previous<->node<->this
		 * @param node
		 */
		public void insertBefore(Link node){

			if(previous!=null){
				previous.next = node;
			}
			node.previous = previous;
			this.previous = node;
			node.next = this;
		}

		
		/**
		 * Install blockHandler passed as argument as the new head of time chain
		 * 
		 * @param link
		 */
		@Override
		public void moveFirst() {
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
		
		@Override
		public void moveLast() {
			// move to head
			if(this!=tail){
				detach();
				pushLast();
			}
		}
		
		
		/**
		 * 
		 * @param bucket
		 */
		private void pushFirst(){
			if(head!=null){
				head.insertBefore(this);
				head = this;
			}
			else{
				head = this;
				tail = this;
			}
		}
		
		
		private void pushLast(){
			if(tail!=null){
				tail.insertAfter(this);
				tail = this;
			}
			else{
				head = this;
				tail = this;
			}
		}


		@Override
		public T next() {
			if(next!=null) {
				return next.object;
			}
			else {
				return null;
			}
		}


		@Override
		public T previous() {
			if(previous!=null) {
				return previous.object;	
			}
			else {
				return null;
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
	public void pushFirst(T element) {
		new Link(element).pushFirst();
		nLinks++;
	}
	
	public void pushLast(T element) {
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


	public static interface LinkConsumer<T> {

		/**
		 * 
		 * @param object
		 * @return a flag indicating if traversing is aborted
		 */
		public boolean consume(T object);
	}

	public boolean traverse(LinkConsumer<T> consumer) {
		Link link = head;
		boolean isAborted = false;
		while(link!=null && !isAborted) {
			isAborted = consumer.consume(link.object);
			link = link.next;
		}
		return isAborted;
	}
}
