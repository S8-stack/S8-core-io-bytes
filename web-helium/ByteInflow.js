

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
	getInt16() {
		let value = this.view.getInt16(this.offset);
		this.offset += 2;
		return value;
	}

	getUInt32() {
		let value = this.view.getUint32(this.offset);
		this.offset += 4;
		return value;
	}

	getInt32() {
		let value = this.view.getInt32(this.offset);
		this.offset += 4;
		return value;
	}

	getUInt64() {
		let value = this.view.getUInt64(this.offset);
		this.offset += 8;
		return value;
	}

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

	getL8StringASCII() {
		let length = this.getUInt8();
		let stringView = new Uint8Array(this.arraybuffer, this.offset, length);
		let value = this.textDecoder_ASCII.decode(stringView);
		this.offset += length;
		return value;
	}

	getL32StringUTF8() {
		let length = this.getUInt32();
		let stringView = new Uint8Array(this.arraybuffer, this.offset, length);
		let value = this.textDecoder_UTF8.decode(stringView);
		this.offset += length;
		return value;
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