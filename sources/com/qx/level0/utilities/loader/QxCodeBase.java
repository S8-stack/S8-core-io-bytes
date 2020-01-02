package com.qx.level0.utilities.loader;

/**
 * 
 * @author pc
 *
 */
public class QxCodeBase {

	/**
	 * 
	 */
	private QxModuleLoader[] loaders;
	
	
	/**
	 * 
	 * @param loaders
	 */
	public QxCodeBase(QxModuleLoader[] loaders) {
		super();
		this.loaders = loaders;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public QxModuleLoader[] getLoaders() {
		return loaders;
	}
}
