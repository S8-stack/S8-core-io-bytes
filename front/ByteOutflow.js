

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
	 * @param {ArrayBuffer} buffer 
	 */
	constructor(buffer){
		this.buffer = buffer;
		this.view = new DataView(buffer);
		this.textEncoder_ASCII = new TextEncoder('ascii');
		this.textEncoder_UTF8 = new TextEncoder('utf8');
		this.offset = 0;
	}

	
	putByte(value){
		this.view.setUint8(this.offset, value);
		this.offset+=1;
	}


	putUInt8(value){
		this.view.setUint8(this.offset, value);
		this.offset+=1;
	}

	putUInt16(value){
		this.view.setUint16(this.offset, value);
		this.offset+=2;
	}

	getInt16(value){
		this.view.setInt16(this.offset, value);
		this.offset+=2;
	}

	putUInt32(value){
		this.view.setUInt32(this.offset, value);
		this.offset+=4;
	}

	putInt32(value){
		this.view.setInt32(this.offset, value);
		this.offset+=4;
	}

	putUInt64(value){
		this.view.setUInt64(this.offset, value);
		this.offset+=8;
	}
	
	putInt64(value){
		this.view.setInt64(this.offset, value);
		this.offset+=8;
	}

	putFloat32(value){
		this.view.setFloat32(this.offset, value);
		this.offset+=4;
	}

	putFloat64(value){
		this.view.setFloat64(this.offset, value);
		this.offset+=8;
	}

	putL8StringASCII(value){
		let stringBuffer = this.textEncoder_ASCII.encode(value);
		let length = stringBuffer.length;
		this.view.setUint8(this.offset, length);
		this.offset+=1;
		new Uint8Array(this.buffer).set(stringBuffer, this.offset);
		this.offset+=length;
	}

	putL32StringUTF8(value){
		let stringBuffer = this.textEncoder_UTF8.encode(value);
		let length = stringBuffer.length;
		this.view.setUint32(this.offset, length);
		this.offset+=4;
		new Uint8Array(this.buffer).set(stringBuffer, this.offset);
		this.offset+=length;
	}

	/**
	 * 
	 * @param {string} value 
	 * @returns 
	 */
	putBohrHashCode(value){
		for(let i=0; i<8; i++){
			this.view.setUint8(this.offset, parseInt(value.substr(2*i,2),16));
			this.offset++;
		}
	}
}