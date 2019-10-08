package com.qx.back.base.test.bytes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.qx.base.bytes.ByteFile;
import com.qx.base.bytes.ByteFileReadingException;
import com.qx.base.bytes.ByteFileWritingException;
import com.qx.base.bytes.ByteInflow;
import com.qx.base.bytes.ByteOutflow;

public class TestUIntX2 {

	public static void main(String[] args) throws Exception {
	
		testCorrectness();

	}



	public static void testCorrectness() throws Exception {
		int n = 1024*1024;
		int offset=0, index;
		
		TestFile file = new TestFile(Paths.get("output/testfile"), n);
		
		while(offset<2e9) {
			System.out.println("testing correctness for range "+offset+" to "+(offset+n-1));

			// fill
			index = offset;
			for(int i=0; i<n; i++) {
				file.values[i] = index++;
			}
			file.save();
			
			file.load();

			// check
			index = offset;
			for(int i=0; i<n; i++) {
				if(file.values[i]!=index++) {
					throw new Exception("Bug with index="+index);
				}
			}
			offset+=17*n;
		}
	}

	public static class TestFile extends ByteFile {
		
		public int[] values;

		public TestFile(Path path, int nValues) {
			super(path);
			this.values = new int[nValues];
		}

		@Override
		public void read(ByteInflow inflow) throws ByteFileReadingException, IOException {
			int length = values.length;
			for(int i=0; i<length; i++) {
				values[i] = inflow.getUInt();
			}
		}

		@Override
		public void write(ByteOutflow outflow) throws ByteFileWritingException, IOException {
			int length = values.length;
			for(int i=0; i<length; i++) {
				outflow.putUInt(values[i]);
			}
		}
		
	}


}
