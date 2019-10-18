package com.qx.base.chain;

public interface QxChainLinkHandle {

	/**
	 * detach from the chain
	 */
	public void detach();
	
	
	/**
	 * move this element to start of the chain (become the new head)
	 */
	public void moveFirst();
	
	
	/**
	 * move this element to the end of the chain (become the new tail)
	 */
	public void moveLast();
	
	
	/**
	 * move next along the chain
	 * @return
	 */
	public Object next();
	
	
	/**
	 * move to previous one along the chain
	 * @return
	 */
	public Object previous();
}
