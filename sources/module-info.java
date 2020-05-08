/**
 * 
 */
/**
 * @author pc
 *
 */
module com.qx.level0.io.bytes {
	
	exports com.qx.level0.io.index;
	exports com.qx.level0.io.bytes;
	exports com.qx.level0.io.chain;
	exports com.qx.level0.io.reactive;
	exports com.qx.level0.io.log;
	exports com.qx.level0.io.boot;
	exports com.qx.level0.io.units;
	exports com.qx.level0.io.utils;
	
	
	/*
	 * dependencies: XML
	 */
	requires transitive com.qx.level0.lang.xml;
	requires transitive com.s8.api;

}