/**
 * 
 */
/**
 * @author pc
 *
 */
module com.s8.blocks.bytes {
	

	exports com.s8.blocks.bytes;
	
	exports com.s8.blocks.bytes.linked;
	
	
	// others
	exports com.s8.blocks.bytes.utilities.index;
	exports com.s8.blocks.bytes.utilities.reactive;
	exports com.s8.blocks.bytes.utilities.log;
	exports com.s8.blocks.bytes.utilities.others;
	
	
	/*
	 * dependencies: XML
	 */
	requires transitive com.s8.alpha;
	requires transitive com.s8.blocks.xml;

}