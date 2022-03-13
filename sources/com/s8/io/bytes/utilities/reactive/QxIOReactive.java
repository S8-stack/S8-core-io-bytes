package com.s8.io.bytes.utilities.reactive;

import java.nio.ByteBuffer;

/**
 * <h1>IOReactive</h1>
 * <p>
 * Based on the "don't call us, we'll call you" principle. 
 * Namely, use this class by overriding this method and supply 
 * bytes when required.
 * </p>
 * <p>
 * Note that is the responsability of the application to flip/clear/compact buffer
 * </p>
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public interface QxIOReactive {


	/**
	 * 
	 * @param buffer
	 * 
	 */
	public void on(ByteBuffer buffer);
	
	

}
