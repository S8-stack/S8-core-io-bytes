package com.s8.io.bytes.api;

/**
 * 
 * @author pc
 *
 */
public enum ByteFormat {


	/* <special : 0x01XX > */



	/* </special : 0x01XX > */



	/* </JAVA_primitives : 0x02XX > */

	/**
	 * 8-bits boolean value
	 * true = 0x08, false = 0x26;
	 */
	BYTE(0x0200),


	BOOL8(0x0201),


	/**
	 * Special Uint
	 * (Java and network is big-endian)
	 */
	UINT(0x0202),


	/**
	 * Big endian 8 bits unsigned integer
	 * (Java and network is big-endian)
	 */
	UINT8(0x0202),


	/** 
	 * Big endian 8 bits signed integer
	 * (Java and network is big-endian)
	 */
	INT8(0x0203),


	/** 
	 * Big endian 16 bits unsigned integer
	 * (Java and network is big-endian)
	 */
	UINT16(0x0204),


	/** 
	 * Big endian 16 bits signed integer
	 * (Java and network is big-endian)
	 */
	INT16(0x0205),


	/** 
	 * Big endian 32 bits unsigned integer
	 * (Java and network is big-endian)
	 */
	UINT32(0x0206),


	/** 
	 * Big endian 32 bits signed integer
	 * (Java and network is big-endian)
	 */
	INT32(0x0207),


	/** 
	 * Big endian 64 bits signed integer
	 * (Java and network is big-endian)
	 */
	INT64(0x0208),
	
	/** 
	 * (Theorical type, since not supported in JAVA, must be casted)
	 * Big endian 64 bits signed integer
	 * (Java and network is big-endian)
	 */
	UINT64(0x0209),


	/**
	 * The IEEE Standard for Floating-Point Arithmetic (IEEE 754) is a technical
	 * standard for floating-point arithmetic established in 1985 by the Institute
	 * of Electrical and Electronics Engineers (IEEE). The standard addressed many
	 * problems found in the diverse floating-point implementations that made them
	 * difficult to use reliably and portably. Many hardware floating-point units
	 * use the IEEE 754 standard.
	 */
	FLOAT32(0x020a),


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
	FLOAT64(0x020b),

	/* </JAVA_primitives : 0x02XX > */



	/* <JAVA_primitive_constants : 0x03XX > */


	/**
	 * zero castable to int
	 */

	INT_ZERO(0x0302),

	/**
	 * zero castable to float, double
	 */
	FLOAT_ZERO(0x0304),


	/* </JAVA_primitive_constants : 0x03XX > */


	/* <JAVA_primitives_array : 0x04XX > */

	/**
	 *bits
	 * true = 0x08, false = 0x26;
	 * <p>/!\ IMPORTANT NOTICE: MAX ARRAY SIZE and LENGTH encoding is <b>UInt16</b></p>
	 */
	BYTE_ARRAY(0x0400),


	/**
	 * 8-bits boolean value
	 * true = 0x08, false = 0x26;
	 * <p>/!\ IMPORTANT NOTICE: MAX ARRAY SIZE and LENGTH encoding is <b>UInt16</b></p>
	 */
	BOOLEAN_ARRAY(0x0401),


	/**
	 * Special UInt array
	 * (Java and network is big-endian)
	 * <p>/!\ IMPORTANT NOTICE: MAX ARRAY SIZE and LENGTH encoding is <b>UInt16</b></p>
	 */
	UINT_ARRAY(0x0402),


	/**
	 * 
	 * <p>/!\ IMPORTANT NOTICE: MAX ARRAY SIZE and LENGTH encoding is <b>UInt16</b></p>
	 */
	UINT8_ARRAY(0x0402),


	/** 
	 * Big endian 8 bits signed integer
	 * (Java and network is big-endian)
	 * <p>/!\ IMPORTANT NOTICE: MAX ARRAY SIZE and LENGTH encoding is <b>UInt16</b></p>
	 */
	INT8_ARRAY(0x0403),


	/** 
	 * Big endian 16 bits unsigned integer
	 * (Java and network is big-endian)
	 * Length encoded as UInt31
	 * <p>/!\ IMPORTANT NOTICE: MAX ARRAY SIZE and LENGTH encoding is <b>UInt16</b></p>
	 */
	UINT16_ARRAY(0x0404),


	/** 
	 * Big endian 16 bits signed integer
	 * (Java and network is big-endian)
	 * Length encoded as UInt31
	 * <p>/!\ IMPORTANT NOTICE: MAX ARRAY SIZE and LENGTH encoding is <b>UInt16</b></p>
	 */
	INT16_ARRAY(0x0405),


	/** 
	 * Big endian 32 bits unsigned integer
	 * (Java and network is big-endian)
	 * <p>/!\ IMPORTANT NOTICE: MAX ARRAY SIZE and LENGTH encoding is <b>UInt16</b></p>
	 */
	UINT32_ARRAY(0x0406),


	/** 
	 * Big endian 32 bits signed integer
	 * (Java and network is big-endian)
	 * <p>/!\ IMPORTANT NOTICE: MAX ARRAY SIZE and LENGTH encoding is <b>UInt16</b></p>
	 */
	INT32_ARRAY(0x0407),


	/** 
	 * Big endian 64 bits signed integer
	 * (Java and network is big-endian)
	 * <p>/!\ IMPORTANT NOTICE: MAX ARRAY SIZE and LENGTH encoding is <b>UInt16</b></p>
	 */
	INT64_ARRAY(0x0408),
	
	/** 
	 * Big endian 64 bits signed integer
	 * (Java and network is big-endian)
	 * <p>/!\ IMPORTANT NOTICE: MAX ARRAY SIZE and LENGTH encoding is <b>UInt16</b></p>
	 */
	UINT64_ARRAY(0x0408),


	/**
	 * The IEEE Standard for Floating-Point Arithmetic (IEEE 754) is a technical
	 * standard for floating-point arithmetic established in 1985 by the Institute
	 * of Electrical and Electronics Engineers (IEEE). The standard addressed many
	 * problems found in the diverse floating-point implementations that made them
	 * difficult to use reliably and portably. Many hardware floating-point units
	 * use the IEEE 754 standard.
	 * 
	 * 
	 * <p>/!\ IMPORTANT NOTICE: MAX ARRAY SIZE and LENGTH encoding is <b>UInt16</b></p>
	 */
	FLOAT32_ARRAY(0x040a),


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
	 * 
	 * 
	 * <p>/!\ IMPORTANT NOTICE: MAX ARRAY SIZE and LENGTH encoding is <b>UInt16</b></p>
	 */
	FLOAT64_ARRAY(0x040b),


	/* </JAVA_primitives_array : 0x04XX > */


	/* </JAVA_primitives_array2 : 0x05XX > */

	NULL_PRIMITIVE_ARRAY(0x0502),

	/* </JAVA_primitives_array2 : 0x05XX > */


	/* <String : 0x06XX > */

	/**
	 * 
	 */
	UTF8(0x0602),

	/* <String : 0x06XX /> */


	/* <String-arrays : 0x07XX > */

	/**
	 * 
	 */
	UTF8_ARRAY(0x0702),

	/* <String-arrays : 0x07XX /> */

	/* <objects : 0x08XX > */


	/**
	 * 
	 */
	S8OBJECT_ADDRESS(0x802),


	/**
	 * 
	 */
	S8VERTEX_INDEX(0x804),
	
	/**
	 * 
	 */
	ENUM_INDEX(0x808),
	
	/**
	 * 
	 */
	BOHR_INDEX(0x809),



	/* <objects : 0x08XX > */


	/* <objects_constants : 0x09XX > */

	/**
	 * 8-bits boolean value
	 * true = 0x08, false = 0x26;
	 */
	NULL(0x0902),

	/**
	 * 8-bits boolean value
	 * true = 0x08, false = 0x26;
	 */
	NULL_S8VERTEX(0x0904),

	/**
	 * 8-bits boolean value
	 * true = 0x08, false = 0x26;
	 */
	NULL_REFERENCE(0x0906),

	/**
	 * 8-bits boolean value
	 * true = 0x08, false = 0x26;
	 */
	NULL_ENUM(0x0908),
	
	/**
	 * 8-bits boolean value
	 * true = 0x08, false = 0x26;
	 */
	NULL_BOHR_INDEX(0x0909),

	/* <objects_constants : 0x09XX /> */

	/* <objects-arrays : 0x0aXX > */


	/**
	 * 
	 */
	S8OBJECT_ARRAY(0xa02),


	/**
	 * 
	 */
	S8VERTEX_ARRAY(0xa04),
	
	S8VERTEX_LIST(0xa05),

	/* <objects-arrays : 0x0aXX > */

	/* <objects-array_constants : 0x0bXX > */

	/**
	 * 
	 */
	NULL_COLLECTION(0xb02);


	/* <objects-array_constants : 0x0bXX > */


	private static ByteFormat[] FORMATS;

	/**
	 * 
	 */
	public int code;


	/**
	 * 
	 * @param code
	 */
	private ByteFormat(int code) {
		this.code = code;
	}

	public static ByteFormat getByCode(int code) {
		// lazy initialization
		if(FORMATS==null) {
			FORMATS = new ByteFormat[256];
			for(ByteFormat format : ByteFormat.values()) {
				FORMATS[format.code] = format;
			}
		}

		return FORMATS[code];
	}
}
