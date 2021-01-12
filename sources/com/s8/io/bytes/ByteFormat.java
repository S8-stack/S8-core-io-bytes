package com.s8.io.bytes;

/**
 * 
 * @author pc
 *
 */
public enum ByteFormat {

	
	/**
	 * 8-bits boolean value
	 * true = 0x08, false = 0x26;
	 */
	BOOL8(new Class<?>[]{ boolean.class }),
	
	/**
	 * Big endian 8 bits unsigned integer
	 * (Java and network is big-endian)
	 */
	UINT8(new Class<?>[]{ byte.class, short.class, int.class, long.class }),
	
	/** 
	 * Big endian 8 bits signed integer
	 * (Java and network is big-endian)
	 */
	INT8(new Class<?>[]{ byte.class, short.class, int.class, long.class }),

	/** 
	 * Big endian 16 bits unsigned integer
	 * (Java and network is big-endian)
	 */
	UINT16(new Class<?>[]{ byte.class, short.class, int.class, long.class }),
	
	/** 
	 * Big endian 16 bits signed integer
	 * (Java and network is big-endian)
	 */
	INT16(new Class<?>[]{ byte.class, short.class, int.class, long.class }),
	
	/** 
	 * Big endian 32 bits unsigned integer
	 * (Java and network is big-endian)
	 */
	UINT32(new Class<?>[]{ byte.class, short.class, int.class, long.class }),
	

	/** 
	 * Big endian 32 bits signed integer
	 * (Java and network is big-endian)
	 */
	INT32(new Class<?>[]{ byte.class, short.class, int.class, long.class }),

	/** 
	 * Big endian 64 bits signed integer
	 * (Java and network is big-endian)
	 */
	INT64(new Class<?>[]{ byte.class, short.class, int.class, long.class }),
	
	/**
	 * The IEEE Standard for Floating-Point Arithmetic (IEEE 754) is a technical
	 * standard for floating-point arithmetic established in 1985 by the Institute
	 * of Electrical and Electronics Engineers (IEEE). The standard addressed many
	 * problems found in the diverse floating-point implementations that made them
	 * difficult to use reliably and portably. Many hardware floating-point units
	 * use the IEEE 754 standard.
	 */
	FLOAT32(new Class<?>[]{ double.class, float.class }),
	
	/**
	 * Floating point is used to represent fractional values, or when a wider range
	 * is needed than is provided by fixed point (of the same bit width), even if at
	 * the cost of precision. Double precision may be chosen when the range or
	 * precision of single precision would be insufficient.
	 * 
	 * In the IEEE 754-2008 standard, the 64-bit base-2 format is officially
	 * referred to as binary64; it was called double in IEEE 754-1985. IEEE 754
	 * specifies additional floating-point formats, including 32-bit base-2 single
	 * precision and, more recently, base-10 representations.
	 */
	FLOAT64(new Class<?>[]{ double.class, float.class }),
	
	/**
	 * String
	 */
	UTF8(new Class<?>[]{ String.class });
	
	
	public Class<?>[] encodables;
	
	private ByteFormat(Class<?>[] encodables) {
		this.encodables = encodables;
	}
}
