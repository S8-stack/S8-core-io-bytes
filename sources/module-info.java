/**
 * 
 */
/**
 * @author pc
 *
 */
module com.s8.io.bytes {
	

	exports com.s8.io.bytes;

	exports com.s8.blocks.bytes.demos.d0;
	
	exports com.s8.io.bytes.linked;
	
	
	// others
	exports com.s8.io.bytes.utilities.index;
	exports com.s8.io.bytes.utilities.reactive;
	exports com.s8.io.bytes.utilities.log;
	exports com.s8.io.bytes.utilities.others;
	
	
	/*
	 * dependencies: XML
	 */
	requires transitive com.s8.alpha;
	requires transitive com.s8.blocks.xml;

}