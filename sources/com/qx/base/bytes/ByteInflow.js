
/**
 * CAD_Engine
 * 
 */
class ByteInflow {

	constructor(arraybuffer){
		this.arraybuffer = arraybuffer;
		this.view = new DataView(this.arraybuffer);
		this.offset = 0;

		this.textDecoder = new TextDecoder("utf-8");
	}

	getInt8() {
		var value = this.view.getInt8(this.offset);
		this.offset+=1;
		return value;
	}

	getUInt8() {
		var value = this.view.getUint8(this.offset);
		this.offset+=1;
		return value;
	}

	getUInt8Array() {
		var array = UInt8Array(length);
		for(let i=0; i<length; i++){
			array[i] = this.view.getUint8(this.offset);
			this.offset+=1;
		}
		return value;
	}

	getInt16() {
		var value = this.view.getInt16(this.offset);
		this.offset+=2;
		return value;
	}

	getUInt16() {
		var value = this.view.getUint16(this.offset);
		this.offset+=2;
		return value;
	}

	getInt32() {
		var value = this.view.getInt32(this.offset);
		this.offset+=4;
		return value;
	}

	getUInt32() {
		var value = this.view.getUint32(this.offset);
		this.offset+=4;
		return value;
	}

	getUInt() {
		let b = this.view.getUint8(this.offset++); // first byte
		if((b & 0x80) == 0x80) {
			let value = b & 0x7f;
			b = this.view.getUint8(this.offset++); // second byte
			if((b & 0x80) == 0x80) {
				value = (value << 7) | (b & 0x7f);
				b = this.view.getUint8(this.offset++); // third byte
				if((b & 0x80) == 0x80) {
					value = (value << 7) | (b & 0x7f);
					b = this.view.getUint8(this.offset++); // fourth byte
					if((b & 0x80) == 0x80) {
						value = (value << 7) | (b & 0x7f);
						b = this.view.getUint8(this.offset++); // fifth byte (final one)
						return (value << 7) | (b & 0x7f);
					}
					else { // fourth byte is matching 0x7f mask
						return (value << 7) | b;
					}
				}
				else { // third byte is matching 0x7f mask
					return (value << 7) | b;
				}
			}
			else { // second byte is matching 0x7f mask
				return (value << 7) | b;
			}
		}
		else { // first byte is matching 0x7f mask
			return b;
		}
	}


	/** float par analogie avec le type C */
	getFloat32() {
		var value = this.view.getFloat32(this.offset);
		this.offset+=4;
		return value;
	}

	/** double par analogie avec le type C */
	getFloat64() {
		var value = this.view.getFloat64(this.offset);
		this.offset+=8;
		return value;
	}

	getBoolean8() {
		var value = this.view.getUint8(this.offset);
		this.offset+=1;
		if(value==32){
			return true;
		}
		if(value==33){
			return false;
		}
	}

	getBooleanArray8() {
		var byte = this.view.getUint8(this.offset);
		this.offset+=1;
		var values = new Array(8);
		for(var i=0; i<8; i++){
			if(((byte >> (7-i)) & 1)==1){
				values[i] = true;
			}
			else{
				values[i] = false;
			}
		}
		return values;
	}



	getString(){

		/*
		 * First part, read code points from UTF8
		 */
		let position = 0, limit = this.getUInt();
		let b0, b1, b2, b3;
		let codePoints = [];

		while(position<limit){

			b0 = this.view.getUint8(this.offset++);

			//  pattern 0....... => 7 bits, 1-byte sequence 
			if((b0 & 0x80) == 0x00){ 
				codePoints.push(b0);
				position+=1;
			}
			//  pattern 110....., 10...... => 11 bits, 2-byte sequence 
			else if((b0 & 0xE0) == 0xC0){
				b1 = this.view.getUint8(this.offset++);
				if((b1 & 0xC0) != 0x80){
					throw "Inconsistent UTF8 format: wrong byte prefix";
				}
				codePoints.push(((b0 & 0x1F) << 6) | (b1 & 0x3f));
				position+=2;
			}
			//  pattern 1110...., 10......, 10...... => 16 bits, 3-byte sequence 
			else if((b0 & 0xF0) == 0xE0){
				b1 = this.view.getUint8(this.offset++);
				if((b1 & 0xC0) != 0x80){
					throw "Inconsistent UTF8 format: wrong byte prefix";
				}
				b2 = this.view.getUint8(this.offset++);
				if((b2 & 0xC0) != 0x80){
					throw "Inconsistent UTF8 format: wrong byte prefix";
				}
				codePoints.push(((b0 & 0x0F) << 12) | ((b1 & 0x3F) << 6) | (b2 & 0x3f));
				position+=3;
			}
			//  pattern 11110..., 10......, 10......, 10...... => 21 bits, 4-byte sequence 
			else if((b0 & 0xF8) == 0xF0){
				b1 = this.view.getUint8(this.offset++);
				if((b1 & 0xC0) != 0x80){
					throw "Inconsistent UTF8 format: wrong byte prefix";
				}
				b2 = this.view.getUint8(this.offset++);
				if((b2 & 0xC0) != 0x80){
					throw "Inconsistent UTF8 format: wrong byte prefix";
				}
				b3 = this.view.getUint8(this.offset++);
				if((b3 & 0xC0) != 0x80){
					throw "Inconsistent UTF8 format: wrong byte prefix";
				}
				codePoints.push(((b0 & 0x07) << 18) | ((b1 & 0x3F) << 12) | ((b2 & 0x3F) << 6) | (b3 & 0x3f));
				position+=4;
			}
		}

		// recompose code units of UCS2 / UTF-16
		let value = "";
		for(let codePoint of codePoints) {

			// can be encoded using 16 bits
			if(codePoint < 0x10000){ 
				value+=String.fromCharCode(codePoint);
			}
			// need surrogates pair
			else {
				let block0 = ((codePoint>>16) & 0x1F)-1;
				let block1 = (codePoint>>10) & 0x3F;
				let block2 = codePoint & 0x3FF;
				
				// high surrogate
				value+=String.fromCharCode(0xD800 | block0<<6 | block1); 
				// low surrogate
				value+=String.fromCharCode(0xDC00 | block2); 
			}
		}
		return value;
	}
	
	
	getBkAddress(){
		let address = new Array(4);
		for(let i=0; i<4; i++){
			address[i] = this.view.getUint32(this.offset);
			this.offset++;
		}
		return address;
	}

	/*
	getString() {

		// retrieve length
		var length = this.view.getUint32(this.offset);
		this.offset+=4;

		if(length>0){
			// read string
			var str = this.textDecoder.decode(new Uint8Array(this.arraybuffer, this.offset, length));
			this.offset+=length;
			return str;		
		}
		else{
			return null;
		}
	}
	 */

};
