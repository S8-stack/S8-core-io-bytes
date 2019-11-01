
/**
 * CAD_Engine
 * 
 */


class ByteOutflow {

	constructor(arrayBuffer){
		this.arrayBuffer = arrayBuffer;
		this.view = new DataView(this.arrayBuffer);
		this.offset = 0;
		this.textEncoder = new TextEncoder("utf-8");	
	}


	putInt8(value) {
		this.view.setInt8(this.offset, value);
		this.offset+=1;
	}

	putUInt8(value) {
		this.view.setUint8(this.offset, value);
		this.offset+=1;
	}

	
	/**
	 * push an Int8 array to the buffer
	 */
	putUInt8Array(value) {
		var length = value.length;
		for(let i=0; i<length; i++){
			this.putUInt8(value[i]);
		}
	}

	putInt16(value) {
		this.view.setInt16(this.offset, value);
		this.offset+=2;
	}


	putUInt16(value) {
		this.view.setUint16(this.offset, value);
		this.offset+=2;
	}


	putInt32(value) {
		this.view.setInt32(this.offset, value);
		this.offset+=4;
	}


	putUInt32(value) {
		this.view.setUint32(this.offset, value);
		this.offset+=4;
	}


	putUInt(value) {
		if(value<0x80) { // single byte encoding
			this.view.setUint8(this.offset++, value & 0x7f);
		}
		else if(value<0x4000) { // two bytes encoding
			this.view.setUint8(this.offset++, ((value>>7) & 0x7f) | 0x80);
			this.view.setUint8(this.offset++, value & 0x7f);
		}
		else if(value<0x200000) { // three bytes encoding
			this.view.setUint8(this.offset++, ((value>>14) & 0x7f) | 0x80);
			this.view.setUint8(this.offset++, ((value>>7) & 0x7f) | 0x80);
			this.view.setUint8(this.offset++, value & 0x7f);
		}
		else if(value<0x10000000) { // four bytes encoding
			this.view.setUint8(this.offset++, ((value>>21) & 0x7f) | 0x80);
			this.view.setUint8(this.offset++, ((value>>14) & 0x7f) | 0x80);
			this.view.setUint8(this.offset++, ((value>>7) & 0x7f) | 0x80);
			this.view.setUint8(this.offset++, value & 0x7f);
		}
		else { // five bytes encoding
			this.view.setUint8(this.offset++, ((value>>28) & 0x07) | 0x80);
			this.view.setUint8(this.offset++, ((value>>21) & 0x7f) | 0x80);
			this.view.setUint8(this.offset++, ((value>>14) & 0x7f) | 0x80);
			this.view.setUint8(this.offset++, ((value>>7) & 0x7f) | 0x80);
			this.view.setUint8(this.offset++, value & 0x7f);
		}
	}
	
	
	/** float par analogie avec le type C */
	putFloat32(value){
		this.view.setFloat32(this.offset, value);
		this.offset+=4;
	}


	/** double par analogie avec le type C */
	putFloat64(value){
		this.view.setFloat64(this.offset, value);
		this.offset+=8;
	}


	putBoolean8(value){
		if(value){
			this.view.setUint8(this.offset, 32);
		}
		else{
			this.view.setUint8(this.offset, 33);
		}
		this.offset+=1;
	}


	putBooleanArray8(values){
		var byte = 0;
		for(var i=0; i<8; i++){
			if(values[i]){
				byte |= (1 << (7-i));
			}
		}
		this.view.setUint8(this.offset, byte);
		this.offset+=1;
	}



	putString(value){

		/*
		 * First part, read from UCS2/UTF16
		 */
		var codePoints = [];
		
		var index = 0;
		var length = value.length;
		let codeUnit, lowSurrogateCodeUnit;
		let bytecount = 0;
		
		let append = function(codePoint){
			// 7 bits -> 1-byte sequence
			if (codePoint < 0x80) { 
				bytecount+=1;
			}
			// 11 bits -> 2-byte sequence
			else if (codePoint < 0x800) {
				bytecount+=2;
			}
			// 16 bits -> 3-byte sequence
			else if (codePoint < 0x10000) {
				bytecount+=3;
			}
			// 21 bits -> 4-byte sequence
			else if (codePoint < 0x110000) { 
				bytecount+=4;
			}
			codePoints.push(codePoint);
		};
		
		while(index < length) {
			codeUnit = value.charCodeAt(index++);
			if (codeUnit & 0xFC00==0xD800) { // this is an high surrogate

				if(index==length){
					throw "Surrogate problem in UCS encoding";
				}
				// this is a low surrogate
				lowSurrogateCodeUnit = value.charCodeAt(index++);
				if ((extra & 0xFC00) != 0xDC00) { 
					throw "Low surrogate prefix not matching";
				}
				append(((codeUnit & 0x3FF) << 10) + (lowSurrogateCodeUnit & 0x3FF) + 0x10000);

			} 
			else if (codeUnit & 0xFC00==0xDC00) { // this is an low surrogate
				throw "Lone low surrogate";
			}
			else {
				append(codeUnit);
			}
		}

		/* 
		 * push code points
		 */

		// set bytecount
		this.putUInt(bytecount);

		for(let codePoint of codePoints){

			// 7 bits -> 1-byte sequence
			if (codePoint < 0x80) { 
				this.view.setUint8(this.offset++, codePoint);
			}
			// 11 bits -> 2-byte sequence
			else if (codePoint < 0x800) {
				this.view.setUint8(this.offset++, 0xC0 | ((codePoint >> 6) & 0x1F));
				this.view.setUint8(this.offset++, 0x80 | (codePoint & 0x3F));
			}
			// 16 bits -> 3-byte sequence
			else if (codePoint < 0x10000) {

				// check scalar value
				if (codePoint >= 0xD800 && codePoint <= 0xDFFF) {
					throw Error('Lone surrogate U+' + 
							codePoint.toString(16).toUpperCase() +' is not a scalar value');
				}
				this.view.setUint8(this.offset++, 0xE0 | ((codePoint >> 12) & 0x0F));
				this.view.setUint8(this.offset++, 0x80 | ((codePoint >> 6) & 0x3F));
				this.view.setUint8(this.offset++, 0x80 | (codePoint & 0x3F));
			}
			// 21 bits -> 4-byte sequence
			else if (codePoint < 0x110000) { 
				this.view.setUint8(this.offset++, 0xF0 | ((codePoint >> 18) & 0x07));
				this.view.setUint8(this.offset++, 0xE0 | ((codePoint >> 12) & 0x0F));
				this.view.setUint8(this.offset++, 0x80 | ((codePoint >> 6) & 0x3F));
				this.view.setUint8(this.offset++, 0x80 | (codePoint & 0x3F));
			}
		}
	}
	
	
	/* DEPRECATED -> use puttArrayBuffer instead */
	putBkAddress(address){
		for(let i=0; i<4; i++){
			this.view.setUint32(this.offset+=4, address[i]);
			this.offset+=4;
		}
	}
	
	putArrayBuffer(arrayBuffer){
		let arrayBufferView = new Uint8Array(arrayBuffer);
		let length = arrayBufferView.length;
		for(let i=0; i<length; i++){
			this.view.setUint8(this.offset, arrayBufferView[i]);
			this.offset++;
		}
	}

}