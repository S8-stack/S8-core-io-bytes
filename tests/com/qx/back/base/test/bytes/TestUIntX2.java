package com.qx.back.base.test.bytes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.qx.level0.utilities.bytes.ByteFile;
import com.qx.level0.utilities.bytes.ByteFileReader;
import com.qx.level0.utilities.bytes.ByteFileReadingException;
import com.qx.level0.utilities.bytes.ByteFileWritingException;
import com.qx.level0.utilities.bytes.ByteInflow;
import com.qx.level0.utilities.bytes.ByteOutflow;

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
		
		public boolean isLoadedSuccessfully;
		
		public int[] values;

		public TestFile(Path path, int nValues) {
			super(path);
			this.values = new int[nValues];
		}

		
		public void load() throws ByteFileReadingException, IOException {
			ByteFile.load(getFilePath(), getBufferCapacity(), new ByteFileReader() {
				
				@Override
				public void consume(ByteInflow inflow) throws IOException {
					int length = values.length;
					for(int i=0; i<length; i++) {
						values[i] = inflow.getUInt();
					}
				}
				
				@Override
				public void onFileDoesNotExist() {
					isLoadedSuccessfully = false;
				}
				
				@Override
				public void onIOException(IOException exception) {
					isLoadedSuccessfully = false;
				}
			});
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
