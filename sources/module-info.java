/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
module com.s8.io.bytes {
	

	exports com.s8.io.bytes;
	exports com.s8.io.bytes.linked;
	
	
	// others
	exports com.s8.io.bytes.base64;
	exports com.s8.io.bytes.utilities.index;
	exports com.s8.io.bytes.utilities.reactive;
	exports com.s8.io.bytes.utilities.log;
	exports com.s8.io.bytes.utilities.others;
	
	
	/*
	 * dependencies: none
	 */
	
	exports com.s8.blocks.bytes.demos.d0;
	
	
	requires transitive com.s8.api;

}