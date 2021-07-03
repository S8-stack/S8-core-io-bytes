





// exporte une constante
export const machin = Math.sqrt(2);

export const UINT8_ARRAY_MAX_SIZE = 255;

export const UINT16_ARRAY_MAX_SIZE = 65535;

/**
 * true limit is 4294967295, but cannot be encoded in JAVA directly, so choose: 2^31-1
 */
export const UINT32_ARRAY_MAX_SIZE = 2147483647;



/* <bytes : 0x0X > */

export const BYTE = 0x02;

export const NULL_BYTES = 0x03;

export const EMPTY_BYTES = 0x04;

export const BYTES = 0x06;

/* </bytes : 0x0X > */



/* </JAVA_booleans : 0x01XX > */



/**
 * for pattern
 */
export const BOOL8 = 0x14;

/**
 * sepcial value for true
 */
export const TRUE_BOOL8 = 0x15;


/**
 * special value for false
 */
export const FALSE_BOOL8 = 0x17;

/* </JAVA_booleans : 0x01XX > */


/* </JAVA_ints : 0x02XX > */


/**
 * (length encoding for null collections)
 */
export const VOID_INT = 0x20;

/**
 * Big endian 8 bits unsigned integer
 * (Java and network is big-endian)
 */
export const ZERO_INT = 0x21;

/**
 * Big endian 8 bits unsigned integer
 * (Java and network is big-endian)
 */
export const INT_MINUS_ONE = 0x22;

/**
 * Big endian 8 bits unsigned integer
 * (Java and network is big-endian)
 */
export const UINT8 = 0x23;


/** 
 * Big endian 8 bits signed integer
 * (Java and network is big-endian)
 */
export const INT8 = 0x024;


/** 
 * Big endian 16 bits unsigned integer
 * (Java and network is big-endian)
 */
export const UINT16 = 0x25;


/** 
 * Big endian 16 bits signed integer
 * (Java and network is big-endian)
 */
export const INT16 = 0x26;


/** 
 * Big endian 32 bits unsigned integer
 * (Java and network is big-endian)
 */
export const UINT32 = 0x27;


/** 
 * Big endian 32 bits signed integer
 * (Java and network is big-endian)
 */
export const INT32 = 0x28;


/** 
 * Big endian 64 bits signed integer
 * (Java and network is big-endian)
 */
export const INT64 = 0x29;

/** 
 * (Theorical type, since not supported in JAVA, must be casted)
 * Big endian 64 bits signed integer
 * (Java and network is big-endian)
 */
export const UINT64 = 0x2a;


/**
 * Special Uint
 * (Java and network is big-endian)
 */
export const UINT = 0x2b;

/* </JAVA_ints : 0x02XX > */

/* </JAVA_floats : 0x03XX > */



/**
 * zero castable to float, double
 */
export const ZERO_FLOAT = 0x30;


/**
 * The IEEE Standard for Floating-Point Arithmetic (IEEE 754) is a technical
 * standard for floating-point arithmetic established in 1985 by the Institute
 * of Electrical and Electronics Engineers (IEEE). The standard addressed many
 * problems found in the diverse floating-point implementations that made them
 * difficult to use reliably and portably. Many hardware floating-point units
 * use the IEEE 754 standard.
 */
export const FLOAT32 = 0x32;


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
export const FLOAT64 = 0x34;

/* </JAVA_floats : 0x03XX > */



/* <String : 0x04XX > */


/**
 * 
 */
export const NULL_STRING = 0x42;

/**
 * 
 */
export const EMPTY_STRING = 0x43;

/**
 * <p>String with the following characteristics</p>
 * <ul>
 * <li>Length is encoded on an UInt8 (this implies that length cannot be more than 256</li>
 * <li>Chars are encoded with UTF8 format</li>
 *</ul>
 */
export const L8_STRING_UTF8 = 0x44;

/**
 * <p>
 * String with the following characteristics
 * </p>
 * <ul>
 * <li>Length is encoded on an UInt32 (this implies that length cannot be more
 * than 2^31, yes 32 is slightly bullshit since JAVA int naturale encoding is on
 * 31 bits and 1 bit for sign)</li>
 * <li>Chars are encoded with UTF8 format</li>
 * </ul>
 */
export const L32_STRING_UTF8 = 0x45;


/**
 * Full ASCII String with length encoded on 8 bits (maximum compacity, use for universal identifiers)
 */
export const L8_STRING_ASCII = 0x46;


/* <String : 0x04XX /> */


/* <objects : 0x06XX > */


/**
 * 8-bits boolean value
 * true = 0x08, false = 0x26;
 */
export const NULL_OBJECT = 0x60;

/**
 * 8-bits boolean value
 * true = 0x08, false = 0x26;
 */
export const NULL_S8ADDRESS = 0x61;

/**
 * 
 */
export const S8ADDRESS = 0x62;

/**
 * 
 */
export const NULL_S8INDEX = 0x63;

/**
 * 
 */
export const S8INDEX = 0x64;

/**
 * 
 */
export const ENUM_INDEX = 0x66;

/**
 * 
 */
export const FLOWABLE_INDEX = 0x67;

/**
 * 
 */
export const BOHR_OBJECT = 0x68;


/* </objects : 0x06XX > */


/* <S8Object : 0x07XX > */


export const S8_OBJECT = 0x7a;



/* </S8Object : 0x07XX > */


/* <extensible types : 0x08XX > */

export const MIME_IMAGE = 0x82;

export const MIME_IMAGE_PNG = 0x22;
export const MIME_IMAGE_JPG = 0x23;


export const MIME_AUDIO = 0x83;

export const MIME_VIDEO = 0x84;

export const MIME_APPLICATION = 0x85;


/* </extensible types : 0x08XX > */


/* <arrays : 0x0bXX > */


/**
 * empty array
 */
export const NULL_ARRAY = 0xb0;

/**
 * empty array
 */
export const EMPTY_ARRAY = 0xb1;

/**
 * array structure with length encoded as UINT8
 * 
 * <p>/!\ IMPORTANT NOTICE: MAX ARRAY SIZE and LENGTH encoding is <b>UInt8</b></p>
 * <p>Next byte indicate the type of data</p>
 */
export const ARRAY_UINT8 = 0xb2;

/**
 * true = 0x08, false = 0x26;
 * <p>/!\ IMPORTANT NOTICE: MAX ARRAY SIZE and LENGTH encoding is <b>UInt16</b></p>
 */
export const ARRAY_UINT16 = 0xb4;

/**
 */
export const ARRAY_UINT32 = 0xb6;

/**
 */
export const ARRAY_UINT64 = 0xb8;


/*
 * <collections>
 */

export const LIST = 0xc2;

/*
 * </collections>
 */
