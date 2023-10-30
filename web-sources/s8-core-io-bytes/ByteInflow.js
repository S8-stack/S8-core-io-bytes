

/**
 * <p>
 * Unified in-flow of bytes with getters for standard primitives.
 * </p>
 * 
 * <p>
 * /!\ All array or String lengths are encoded as UInt32 to reflect their
 * maximum possible length in JAVA.
 * </p>
 * 
 * @author pc
 *
 */


export const Bytes = {
	
	BOOL8_FALSE : 0x37,
	BOOL8_TRUE : 0x53,
};


export class ByteInflow {

	/**
	 * 
	 * @param {ArrayBuffer} arraybuffer 
	 */
	constructor(arraybuffer) {
		this.arraybuffer = arraybuffer;
		this.view = new DataView(arraybuffer);
		this.textDecoder_ASCII = new TextDecoder('ascii');
		this.textDecoder_UTF8 = new TextDecoder('utf8');
		this.offset = 0;
	}


	/**
	 * 
	 * @returns 
	 */
	getByte() {
		let value = this.view.getUint16(this.offset);
		this.offset += 1;
		return value;
	}

	/**
	 * @return next byte
	 * @throws IOException 
	 */
	getBool8() {
		switch(this.getUInt8()) {
		case Bytes.BOOL8_FALSE : return false;
		case Bytes.BOOL8_TRUE : return true;
		}
	}


	/**
	 * 
	 * @returns 
	 */
	getUInt7x() {
		/*
		 * 0x40: 01000000
		 * 0x80: 10000000
		 * 0xc0: 11000000
		 */
		
		// pull first byte
		let b = this.getUInt8();
		
		if((b & 0x40) == 0x40) {
			return  -(b & 0x3f); // no shift
		}
		else {
			// compute value
			
			let value = b & 0x3f; // no shift
			let shift = 6;
			
			while((b & 0x80) == 0x80) {
				
				// pull next byte
				b = this.getUInt8();
				
				// recompute value
				
				value = ((b & 0x7f) << shift) | value;
				shift += 7;
			}
			return value;
		}
	}
	

	/**
	 * 
	 * @returns 
	 */
	getUInt8() {
		let value = this.view.getUint8(this.offset);
		this.offset += 1;
		return value;
	}


	/**
	 * 
	 * @returns 
	 */
	getUInt16() {
		let value = this.view.getUint16(this.offset);
		this.offset += 2;
		return value;
	}


	/**
	 * 
	 * @returns 
	 */
	getUInt32() {
		let value = this.view.getUint32(this.offset);
		this.offset += 4;
		return value;
	}


	/**
	 * 
	 * @returns 
	 */
	getUInt64() {
		let value = this.view.getUInt64(this.offset);
		this.offset += 8;
		return value;
	}


	/**
	 * 
	 * @returns 
	 */
	getInt8() {
		let value = this.view.getInt8(this.offset);
		this.offset += 1;
		return value;
	}


	/**
	 * 
	 * @returns 
	 */
	getInt16() {
		let value = this.view.getInt16(this.offset);
		this.offset += 2;
		return value;
	}


	/**
	 * 
	 * @returns 
	 */
	getInt32() {
		let value = this.view.getInt32(this.offset);
		this.offset += 4;
		return value;
	}


	/**
	 * 
	 * @returns 
	 */
	getInt64() {
		let value = this.view.getInt64(this.offset);
		this.offset += 8;
		return value;
	}


	getFloat32() {
		let value = this.view.getFloat32(this.offset);
		this.offset += 4;
		return value;
	}

	getFloat64() {
		let value = this.view.getFloat64(this.offset);
		this.offset += 8;
		return value;
	}

	getStringUTF8() {
		let length = this.getUInt7x();
		if(length > 0){
			let stringView = new Uint8Array(this.arraybuffer, this.offset, length);
			let value = this.textDecoder_UTF8.decode(stringView);
			this.offset += length;
			return value;
		}
		else if(length == 0){
			return "";
		}
		else{
			return null;
		}
	}


	getIndex(length){
		let script = "";
		for(let i=0; i<length; i++){
			let digit = this.view.getUint8(this.offset);
			this.offset += 1;
			script += digit > 0x0f ? digit.toString(16) : '0'+digit.toString(16);
		}
		return script;
    }

}