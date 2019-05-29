
/**
 * CAD_Engine
 * 
 */
function ByteInflow(arraybuffer){
	this.arraybuffer = arraybuffer;
	this.view = new DataView(this.arraybuffer);
	this.offset = 0;
	
	this.textDecoder = new TextDecoder("utf-8");
}


ByteInflow.prototype = {
	
		getInt8 : function(){
			var value = this.view.getInt8(this.offset);
			this.offset+=1;
			return value;
		},
		
		getUint8 : function(){
			var value = this.view.getUint8(this.offset);
			this.offset+=1;
			return value;
		},
		
		getUint8Array : function(length){
			var array = Uint8Array(length);
			for(let i=0; i<length; i++){
				array[i] = this.view.getUint8(this.offset);
				this.offset+=1;
			}
			return value;
		},
		
		getInt16 : function(){
			var value = this.view.getInt16(this.offset);
			this.offset+=2;
			return value;
		},
		
		getUint16 : function(){
			var value = this.view.getUint16(this.offset);
			this.offset+=2;
			return value;
		},
		
		getInt32 : function(){
			var value = this.view.getInt32(this.offset);
			this.offset+=4;
			return value;
		},
		
		getUint32 : function(){
			var value = this.view.getUint32(this.offset);
			this.offset+=4;
			return value;
		},
		
		/** float par analogie avec le type C */
		getFloat32 : function(){
			var value = this.view.getFloat32(this.offset);
			this.offset+=4;
			return value;
		},
		
		/** double par analogie avec le type C */
		getFloat64 : function(){
			var value = this.view.getFloat64(this.offset);
			this.offset+=8;
			return value;
		},
		
		getBoolean8 : function(){
			var value = this.view.getUint8(this.offset);
			this.offset+=1;
			if(value==32){
				return true;
			}
			if(value==33){
				return false;
			}
		},
		
		getBooleanArray8 : function(){
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
		},
		
		
		getStringUTF8 : function(){
			
			// retrieve length
			var length = this.view.getUint8(this.offset);
			this.offset+=1;
			
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
		
};
