import { ByteInflow } from "./ByteInflow";
import { ByteOutflow } from "./ByteOutflow";

export function testBytes(){

	let buffer = new ArrayBuffer(1024);

	let inflow = new ByteInflow(buffer);
	let outflow = new ByteOutflow(buffer);

	let a = 197890;
	outflow.putInt32(a);
	if(inflow.getInt32()!=a) throw "error";

	let b = 197.890;
	outflow.putFloat32(b);
	let b_roundtripped = inflow.getFloat32();
	if(Math.abs(b_roundtripped-b)>1e-12) alert("error:"+(Math.abs(b_roundtripped-b)/Math.abs(b)*100));


	let c = "HJreoipeàoik,197.890";
	outflow.putL32StringUTF8(c);
	let c_roundtripped = inflow.getL32StringUTF8();
	if(c_roundtripped!=c) throw "error";

}