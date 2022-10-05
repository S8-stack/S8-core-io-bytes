

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
export class ByteOutflow {

	/**
	 * 
	 * @param {ArrayBuffer} arraybuffer 
	 */
	constructor(arraybuffer) {
		this.arraybuffer = arraybuffer;
		this.view = new DataView(arraybuffer);
		this.textEncoder_ASCII = new TextEncoder('ascii');
		this.textEncoder_UTF8 = new TextEncoder('utf8');
		this.offset = 0;
	}

	allocate(bytecount){
		// TODO
	}


	putByte(value) {
		this.allocate(1);
		this.view.setUint8(this.offset, value);
		this.offset += 1;
	}


	putBool8(value){
		this.allocate(1);
		this.putUInt8(value ? ByteInflow.BOOL8_TRUE : ByteInflow.BOOL8_FALSE);
	}


	putUInt7x(value) {
		/*
		 * 0x40: 01000000
		 * 0x80: 10000000
		 * 0xc0: 11000000
		 * 0x3f = 0b111111
		 */

		if(value < 0) {
			value = - value; // take the opposite (so -1 is not 2^63+1 as in JAVA encoding)
			if((value & 0x3f) == value) {
				this.putUInt8((value & 0x3f) | 0x40);
			}
			else {
				throw "This code is not defined";
			}
		}
		else {
			let b = value & 0x3f;
			value = (value >> 6);

			while(value != 0x00) {

				// push previous byte
				this.putUInt8(b | 0x80);

				// compute next byte
				b = value & 0x7f;
				value = (value >> 7);
			}	
			// push final byte
			this.putUInt8(b & 0xff);
		}
	}


	putUInt8(value) {
		this.allocate(1);
		this.view.setUint8(this.offset, value);
		this.offset += 1;
	}

	putUInt16(value) {
		this.allocate(2);
		this.view.setUint16(this.offset, value);
		this.offset += 2;
	}


	putUInt32(value) {
		this.allocate(4);
		this.view.setUInt32(this.offset, value);
		this.offset += 4;
	}

	/**
	 * 
	 * @param {*} value 
	 */
	putUInt64(value) {
		this.allocate(8);
		this.view.setUInt64(this.offset, value);
		this.offset += 8;
	}



	/**
	 * 
	 * @param {*} value 
	 */
	putInt8(value) {
		this.allocate(1);
		this.view.setInt8(this.offset, value);
		this.offset += 1;
	}


	/**
	 * 
	 * @param {*} value 
	 */
	putInt16(value) {
		this.allocate(2);
		this.view.setInt16(this.offset, value);
		this.offset += 2;
	}


	/**
	 * 
	 * @param {*} value 
	 */
	putInt32(value) {
		this.allocate(4);
		this.view.setInt32(this.offset, value);
		this.offset += 4;
	}


	/**
	 * 
	 * @param {*} value 
	 */
	putInt64(value) {
		this.allocate(8);
		this.view.setInt64(this.offset, value);
		this.offset += 8;
	}


	/**
	 * 
	 * @param {*} value 
	 */
	putFloat32(value) {
		this.allocate(4);
		this.view.setFloat32(this.offset, value);
		this.offset += 4;
	}


	/**
	 * 
	 * @param {*} value 
	 */
	putFloat64(value) {
		this.allocate(8);
		this.view.setFloat64(this.offset, value);
		this.offset += 8;
	}


	/**
	 * 
	 * @param {*} value 
	 */
	putL8StringASCII(value) {
		let stringBuffer = this.textEncoder_ASCII.encode(value);
		let length = stringBuffer.length;
		this.view.setUint8(this.offset, length);
		this.offset += 1;
		new Uint8Array(this.arraybuffer).set(stringBuffer, this.offset);
		this.offset += length;
	}


	/**
	 * 
	 * @param {*} value 
	 */
	putStringUTF8(value) {
		let stringBuffer = this.textEncoder_UTF8.encode(value);
		let length = stringBuffer.length;
		this.putUInt7x(length);
		this.allocate(length);
		new Uint8Array(this.arraybuffer).set(stringBuffer, this.offset);
		this.offset += length;
	}

	compact() {
		// prepare compact array buffer
		let n = this.offset;
		let compactedArrayBuffer = new ArrayBuffer(n);

		// copy
		let compactView = new DataView(compactedArrayBuffer);
		for (let i = 0; i < n; i++) {
			compactView.setUint8(i, this.view.getUint8(i));
		}

		return compactedArrayBuffer;
	}

}