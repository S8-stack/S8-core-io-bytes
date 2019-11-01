/**
 * 
 */
/**
 * @author pc
 *
 */
module com.qx.base {
	
	exports com.qx.base;
	exports com.qx.base.index;
	exports com.qx.base.bytes;
	exports com.qx.base.chain;
	exports com.qx.base.units;
	exports com.qx.base.reactive;
	exports com.qx.base.html;
	exports com.qx.base.log;
	exports com.qx.base.boot;
	exports com.qx.base.utils;
	
	exports com.qx.web.mime;

	exports com.qx.web.sources;
	exports com.qx.web.sources.js;

	/*
	 * dependencies: none
	 */
	requires transitive com.qx.lang.xml;
}