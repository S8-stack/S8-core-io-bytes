/**
 * 
 */
/**
 * @author pc
 *
 */
module com.s8.blocks.helium {
	

	exports com.s8.blocks.helium;

	exports com.s8.blocks.bytes.demos.d0;
	
	exports com.s8.blocks.helium.linked;
	
	
	// others
	exports com.s8.blocks.helium.utilities.index;
	exports com.s8.blocks.helium.utilities.reactive;
	exports com.s8.blocks.helium.utilities.log;
	exports com.s8.blocks.helium.utilities.others;
	
	
	/*
	 * dependencies: XML
	 */
	requires transitive com.s8.alpha;
	requires transitive com.s8.blocks.xml;

}