
/**
 * CAD_Engine
 * 
 */
function ByteOutflow(arraybuffer){
	this.arraybuffer = arraybuffer;
	this.view = new DataView(this.arraybuffer);
	this.offset = 0;
	
	this.textEncoder = new TextEncoder("utf-8");
}


ByteOutflow.prototype = {
	
		
		putInt8 : function(value){
			this.view.setInt8(this.offset, value);
			this.offset+=1;
		},
		
		putUint8 : function(value){
			this.view.setUint8(this.offset, value);
			this.offset+=1;
		},
		
		putUint8Array : function(value){
			var length = value.length;
			for(let i=0; i<length; i++){
				this.putUint8(value[i]);
			}
		},
		
		putInt16 : function(value){
			this.view.setInt16(this.offset, value);
			this.offset+=2;
		},
		
		
		putUint16 : function(value){
			this.view.setUint16(this.offset, value);
			this.offset+=2;
		},
		
		
		putInt32 : function(value){
			this.view.setInt32(this.offset, value);
			this.offset+=4;
		},
		
		
		putUint32 : function(value){
			this.view.setUint32(this.offset, value);
			this.offset+=4;
		},
		
		
		/** float par analogie avec le type C */
		putFloat32 : function(value){
			this.view.setFloat32(this.offset, value);
			this.offset+=4;
		},
		
		
		/** double par analogie avec le type C */
		putFloat64 : function(value){
			this.view.setFloat64(this.offset, value);
			this.offset+=8;
		},
		
		
		putBoolean8 : function(value){
			if(value){
				this.view.setUint8(this.offset, 32);
			}
			else{
				this.view.setUint8(this.offset, 33);
			}
			this.offset+=1;
			
		},
		
		
		putBooleanArray8 : function(values){
			var byte = 0;
			for(var i=0; i<8; i++){
				if(values[i]){
					byte |= (1 << (7-i));
				}
			}
			this.view.setUint8(this.offset, byte);
			this.offset+=1;
		},
		
		
		putStringUTF8 : function(value){
			
			// retrieve length
			var length = value.length;
			var length = this.view.setUint8(this.offset, length);
			this.offset+=1;
			
			if(length>0){
				// read string
				let encoded = this.textEncoder.encode(value);
				for(var i=0; i<length; i++){
					this.arraybuffer[this.offset]=encoded[i];
					this.offset++;
				}
			}
		}
};
