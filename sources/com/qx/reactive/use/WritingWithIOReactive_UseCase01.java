package com.qx.reactive.use;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.qx.reactive.OutputByteArray;
import com.qx.reactive.QxIOReactive;
import com.qx.reactive.QxIOReactive.Result;

public class WritingWithIOReactive_UseCase01 {

	public static void main(String[] args) {


	}


	
	private static enum Mode {
		STREAMING, RETAINING, RELEASING;
	}



	private class Pullable {
		
		
		private int length;

		/**
		 * input
		 */
		public BlockingQueue<FrameWriting> frames;

		private FrameWriting current;


		private LinkedList<ByteBuffer> retained;
		
		private Mode mode = Mode.STREAMING;
		

		public Pullable(int length) {
			super();
			this.length = length;
			frames = new LinkedBlockingQueue<>();
		}

		
		

		public ByteBuffer pull(int length) {
				
			if() {
				ByteBuffer buffer = retained.getFirst();
				while(isReleasing) {
				
					switch(current.on(buffer)) {

					case FEED:
						buffer = retained.getFirst();
						break;

					case RELEASE_DONE:
					case RETAIN_FEED:
						isReleasing = false;
						release();
						break;

					default: throw new RuntimeException("Unsupported result at this point");
					}
				}
				
			}
			
			if(current==null) {
				current = frames.poll();
				if(current==null) {
					return null;
				}
			}
			// at this point current is non null
			boolean isDone = false;

			boolean isRetaining = false;
			boolean isReleasing = false;


		

		}

		
		private ByteBuffer streaming() {
			ByteBuffer buffer = ByteBuffer.allocate(length);
			while(true) {

				switch(current.on(buffer)) {

				case FEED:
					buffer.clear();
					return buffer;

				case DONE:
					// find next frame to write
					current = frames.poll();
					if(current==null) { // no new frame to write
						return buffer; // return buffer
					}
					break;

				case RETAIN_FEED:



				}
			}
		}
		
		private ByteBuffer retaining() {
			retained = new LinkedList<ByteBuffer>();
			ByteBuffer buffer = ByteBuffer.allocate(length);
			while(true) {
				switch(current.on(buffer)) {

				case FEED:
					retained.addLast(buffer);
					buffer = ByteBuffer.allocate(length);
					break;

				case REWIND:
					mode = Mode.RELEASING;
					return releasing();

				default: throw new RuntimeException("Unsupported result at this point");
				}
			}
		}
		
		private ByteBuffer rewinding() {
			if(retained.isEmpty()) {
				throw new RuntimeException("Nothing retained!!");
			}
			ByteBuffer buffer = retained.poll();
			while(true) {
				switch(current.on(buffer)) {

				case FEED:
					retained.addLast(buffer);
					buffer = ByteBuffer.allocate(length);
					break;

				case RELEASE_DONE:
				case RELEASE_DONE:
					mode = Mode.RELEASING;
					return releasing();

				default: throw new RuntimeException("Unsupported result at this point");
				}
			}
		}
		
		private ByteBuffer releasing() {
			if(!retained.isEmpty()) {
				return retained.poll();
			}
			else {
				mode = Mode.STREAMING;
				return streaming();
			}
		}


		private void push(ByteBuffer buffer) {
			buffer.flip();
			System.out.println("bb issued: "+buffer.remaining());
		}
	}




	/**
	 * Frame is built this way:
	 * <ul>
	 * <li>header with constant bytecount: 8 </li>
	 * <li>body with variable length</li>
	 * </ul>
	 * @author pc
	 *
	 */
	private class FrameWriting implements QxIOReactive {

		private int mode;

		private boolean hasReturnedRetaining;

		private int bookmark;

		private OutputByteArray array;

		private int payloadLength;

		private Payload payload;

		public FrameWriting() {
			super();
			/* We assume it's SUPER HARD to get length of payload; so we don't do it*/
			payload = new Payload();
			payloadLength = 0;
			mode = -1;
			hasReturnedRetaining = false;
		}

		@Override
		public Result on(ByteBuffer buffer) {

			Result r;
			int i;
			while(true) {

				switch (mode) {

				case 0: // try to reserve header directly
					bookmark = buffer.position();
					array = new OutputByteArray(8);
					if(array.pull(buffer)) { // succeed into writing all of the bytes
						mode = 2;
					}
					else { // fail to write in single pass
						mode = 1;
						hasReturnedRetaining = true;
						return Result.RETAIN_FEED;
					}
					break;

				case 1: // complete header reservation
					if(array.pull(buffer)) { // succeed into writing all of the bytes
						mode = 2;
					}
					else {
						return Result.FEED; // we need the next buffer to write the remaining...
					}
					break;

				case 2: // write payload
					i=buffer.position();
					r = payload.on(buffer);
					payloadLength+=buffer.position()-i;
					if(r==Result.DONE) {
						ByteBuffer b = ByteBuffer.allocate(8);
						b.putLong(payloadLength);
						array = new OutputByteArray(b.array());
						mode = 3;
						if(hasReturnedRetaining) {
							return Result.REWIND;
						}
					}
					else if(r==Result.FEED){
						hasReturnedRetaining = true;
						return Result.RETAIN_FEED;
					}
					else {
						return r;
					}
					break;

				case 3: // start write header
					buffer.position(bookmark);
					if(array.pull(buffer)) {
						return hasReturnedRetaining?Result.RELEASE_DONE:Result.DONE;
					}
					else {
						return hasReturnedRetaining?Result.RELEASE_FEED:Result.FEED;
					}
				default:
					break;
				}	
			}
		}

	}

	private class Payload implements QxIOReactive {


		private OutputByteArray bytes;

		public Payload() {
			int length = (int) (8+56*Math.random());
			StringBuilder builder = new StringBuilder();
			builder.append("jqozijaozijaojmnoijpijA&É-122");
			for(int i=0; i<length; i++) {
				builder.append(i%10);
			}
			bytes = new OutputByteArray(builder.toString().getBytes());	
		}

		@Override
		public Result on(ByteBuffer buffer) {
			return bytes.pull(buffer)?Result.DONE:Result.FEED;
		}
	}

}
