package com.qx.web.sources;

import java.io.IOException;
import java.io.InputStream;

import com.qx.base.bytes.BytesChainLink;
import com.qx.web.mime.MIME_Type;

public abstract class WebSource {

	public static enum Status {
		OK, NOT_FOUND, LOAD_FAILED;
	}

	public final static boolean DEBUG_FORCE_RELOADING = true;


	public class Payload {

		public Status status;

		public long bytecount;

		/**
		 * payload
		 */
		public BytesChainLink head;

		public Payload() {
			try(InputStream inputStream = getInpuStream()){
				if(inputStream!=null) {

					bytecount = 0;
					BytesChainLink block = createFragment(), nextBlock;

					head = block;
					boolean isDone = false;
					int nBytes;
					int capacity = block.bytes.length, position = block.offset;
					while(!isDone){

						nBytes = inputStream.read(block.bytes, position, capacity-position);
						// end of stream reached
						if(nBytes==-1){ 
							isDone = true;
						}
						else {
							position+=nBytes;
							block.length+=nBytes;
							bytecount+=nBytes;

							if(position==capacity){ // full loading of block

								// create next block
								nextBlock = createFragment();

								// link next block
								block.next = nextBlock;

								// reset scope vars
								block = nextBlock;
								capacity = block.bytes.length;
								position = block.offset;
							}
							// else{ : incomplete reading of block
						}
					}
					inputStream.close();
					status = Status.OK;
				}
				else {
					status = Status.NOT_FOUND;	
				}
			}
			catch (IOException e) {
				status = Status.LOAD_FAILED;
				e.printStackTrace();
			}
		}

		private BytesChainLink createFragment() {
			return new BytesChainLink(new byte[fragmentLength], 0, 0);
		}
	}

	/**
	 * local pathname to retrieve the resource
	 */
	private String localPathname;

	/**
	 * pathanme (from web side), acts as id
	 */
	private String webPathname;

	private boolean isCached;

	private WebSourceLoader loader;

	private int fragmentLength;

	private WebSourceAccessLevel level;

	public WebSource(
			WebSourceLoader loader,
			WebSourceAccessLevel level, 
			String packagePathname, 
			String webPathname, 
			boolean isCached,
			int fragmentLength) {

		super();

		this.loader = loader;
		this.level = level;
		
		// path
		this.localPathname = packagePathname;

		// pathname
		this.webPathname = webPathname;

		// caching
		this.isCached = isCached;

		// buffering setup
		this.fragmentLength = fragmentLength;
	}



	public InputStream getInpuStream() {
		return loader.getResource(localPathname);
	}


	public boolean isCached() {
		return isCached;
	}

	public String getWebPathname() {
		return webPathname;
	}

	public String getLocalPathname() {
		return localPathname;
	}



	/**
	 * <p>
	 * <code>load()</code> <b> MUST be called prior to accessing a WebSource</b>.
	 * </p>
	 * <p>
	 * This method ensure that the source file has been loaded and return a status
	 * indicating f the opeartion has been successful
	 * </p>
	 * <p>Of course, is caching is enabled, actual loading is not re-launched and previous loading is used.
	 * </p>
	 * 
	 * @return
	 */
	public abstract Status load();


	/**
	 * 
	 * @return file size in bytes
	 */
	public abstract long getBytecount();


	/**
	 * return MIME type
	 * @return
	 */
	public abstract MIME_Type getType();


	/**
	 * 
	 * @throws IOException
	 */
	public abstract BytesChainLink getHead();



	public WebSourceAccessLevel getLevel() {
		return level;
	}

	public boolean isAccessible(WebSourceAccessLevel level) {
		return level.isEqualOrHigherThan(this.level);
	}


	@Override
	public String toString() {
		return webPathname;
	}


}
