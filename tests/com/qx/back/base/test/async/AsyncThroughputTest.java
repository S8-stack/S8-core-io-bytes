package com.qx.back.base.test.async;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 
 * @author pc
 * <ul>
 * <li> buffering size = 65536: -> 1.12 GB/s</li>
 * <li> buffering size = 32768: -> 0.99 GB/s</li>
 * <li> buffering size = 16384: -> 0.75 GB/s</li>
 * <ul>
 */
public class AsyncThroughputTest {

	/**
	 * serie length
	 * 1024^3~1B records, so that 1024^3 x 8 roughly ~ 1GB.
	 */
	public final static long SERIE_LENGTH = 1073741824; // 

	public final static int CAPACITY = 65536;

	public final static boolean IS_VERBOSE = false;
	
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		Server server = new Server(1336);
		server.start();
		
		Client client = new Client(1336);
		client.start();
	}
	
	
	
	public static class Client {
		
		private int port;
		
		public Client(int port) {
			super();
			this.port = port;
		}

		public void start() throws InterruptedException, ExecutionException, IOException {
			AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
			InetSocketAddress hostAddress = new InetSocketAddress("localhost", port);
			channel.connect(hostAddress).get();
			new ClientTask(channel).run();
		}
		
		
	}
	
	public static class ClientTask implements Runnable {
		
		private AsynchronousSocketChannel channel;
		
		private ByteBuffer buffer;
		
		private String jobName;
		
		private int checkSum;
		
		private long index;
		
		private long seed;
		
		private long time;
		
		public ClientTask(AsynchronousSocketChannel channel) {
			super();
			this.channel = channel;
			buffer = ByteBuffer.allocate(CAPACITY);
			seed = 1683876876863l;
		}
		
		private void push() {
			while(buffer.remaining()>=8 && index<SERIE_LENGTH) {
				// append
				checkSum+=seed;
				index++;
				buffer.putLong(seed);
				
				// update seed
				seed = seed*317+287798611;
			}
			if(IS_VERBOSE) {
				System.out.println("client side index is now: "+index+" , check-sum="+checkSum);	
			}
		}
		
		private CompletionHandler<Integer, Void> callback = new CompletionHandler<Integer, Void>() {
			
			@Override
			public void completed(Integer result, Void attachment) {
				
				if(index==SERIE_LENGTH) {
					if(!buffer.hasRemaining()) {
						double elapsed = ((double) (System.nanoTime() - time))/1e9; // in ms
						System.out.println(jobName+" [Client-side] is done! check-sum="+checkSum);	
						System.out.println(jobName+" [Client-side] performed in "+(elapsed)+" s!");	
						System.out.println(jobName+" [Client-side] Throughput is :"+(SERIE_LENGTH*8/1e9/elapsed)+" GB/s!");	
						
						
					}
					else {
						channel.write(buffer, null, callback);
					}
				}
				else {
					buffer.compact();
					push();
					buffer.flip();
					
					channel.write(buffer, null, callback);
				}
				
			
			}

			@Override
			public void failed(Throwable exc, Void attachment) {
				System.out.println(jobName+" [Client-side] failed due to I/O problems: "+exc.getMessage());
			}
		};

		

		@Override
		public void run() {
			
			// saving time stamp
			time = System.nanoTime();
			
			push();
			buffer.flip();
			channel.write(buffer, null, callback);
		}
	}
	


	public static class Server {

		private AsynchronousServerSocketChannel serverSocketChannel;

		private AtomicBoolean isRunning;

		public Server(int port) throws IOException {
			super();
			isRunning = new AtomicBoolean(false);
			ExecutorService pool = Executors.newSingleThreadExecutor();
			AsynchronousChannelGroup group = AsynchronousChannelGroup.withThreadPool(pool);
			serverSocketChannel = AsynchronousServerSocketChannel.open(group).bind(new InetSocketAddress(port));
			System.out.println("Server listening on " + port);
		}


		public void start() {
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					while(isRunning.get()) {
						try {
							AsynchronousSocketChannel channel = serverSocketChannel.accept().get();
							Thread job = new Thread(new ServerJob("job", channel));
							job.start();

						} 
						catch (InterruptedException e) {
							e.printStackTrace();
						} 
						catch (ExecutionException e) {
							e.printStackTrace();
						}
					}

				}
			});
			isRunning.set(true);
			thread.start();
		}	
	}


	public static class ServerJob implements Runnable {


		private AsynchronousSocketChannel channel;

		private String jobName;
		
		private ByteBuffer buffer;

		private CompletionHandler<Integer, Void> callback = new CompletionHandler<Integer, Void>(){

			@Override
			public void completed(Integer result, Void attachment) {

				buffer.flip();
				pull();
				buffer.compact();

				if(index<SERIE_LENGTH) {
					channel.read(buffer, null, callback);	
				}
				else {
					System.out.println(jobName+" [server side] is done! check-sum="+checkSum);
				}
			}

			@Override
			public void failed(Throwable exc, Void attachment) {
				System.out.println(jobName+" failed due to I/O problems: "+exc.getMessage());
			}		
		};

		private int checkSum;

		private long index;

		public ServerJob(String jobName, AsynchronousSocketChannel channel) {
			super();
			this.jobName = jobName;
			this.channel = channel;
			buffer = ByteBuffer.allocate(CAPACITY);
			checkSum = 0;
		}

		
		private void pull() {
			while(buffer.remaining()>=8 && index<SERIE_LENGTH) {
				checkSum+=buffer.getLong();
				index++;
			}
			if(IS_VERBOSE) {
				System.out.println("Server side index is now: "+index);	
			}
		}
		
		
		@Override
		public void run() {
			channel.read(buffer, null, callback);
		}
	}
}
