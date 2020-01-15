package com.qx.level0.utilities.chain;

/**
 * <p><code>Chainable</code> is keeping a reference to a <code>Link</code></p>
 * @author pc
 *
 */
public interface QxChainable<T extends QxChainable<T>> {
	

	/**
	 * 
	 * @param link
	 */
	public void setChainLink(QxChain<T>.Link handle);
	
	public QxChain<T>.Link getChainLink();

	
	/**
	 * To be called when dropped. Typical use case:
	 * <ul>
	 * <li><code>save();</code></li>
	 * <li>remove from mapping: <code>buckets.remove(bucket.getPath());</code></li>
	 * </ul>
	 */
	public void onChainLinkDetached();
	
	
	
}
