package com.qx.struct;



/**
 * <p>
 * <code>QxIndexMap</code> is a specially dedicated hashmap for indexed content.
 * </p>
 * <p>
 * Key driver is <code>exponent</code> int variable, since:
 * </p>
 * <ul>
 * <li>The number of buckets of the hashmap is 2^<code>exponent</code></li>
 * <li>mask applied to the Int32 hascode derived from the index is
 * <code>0b0000111..11</code>, where the number of 1 is equals to
 * <code>exponent</code>-1. The number of indices that passes this mask is
 * 2^<code>exponent</code>.</li>
 * </ul>
 * 
 * @author pc
 *
 */
public class QxIndexMap {

	public final static boolean IS_DEBUG_ACTIVE = true;

	private class Node {

		public QxIndex index;

		public Object object;

		public Node next;

		public Node(QxIndex index, Object object) {
			super();
			this.index = index;
			this.object = object;
		}
		
		public Node(QxIndex index, Object object, Node next) {
			super();
			this.index = index;
			this.object = object;
			this.next = next;
		}

		
		public int getBucketIndex() {
			return index.toInt32() & mask;
		}
	}


	private Node[] buckets;

	private int size;

	private int mask;

	private int exponent;

	public QxIndexMap() {
		super();
		size = 0;
		this.exponent = 6;
		mask = mask(exponent);
		buckets = new Node[powerOfTwo(exponent)];
	}


	private void update() {
		double loadFactor = ((double) size)/((double) buckets.length);
		if(loadFactor>8.0) {
			expand();
		}
		if(loadFactor<2.0) {
			collapse();
		}
	}


	private void expand() {
		if(exponent<24) {
		

			exponent++;
			mask = mask(exponent);
			if(IS_DEBUG_ACTIVE) {
				System.out.println("[QxIndexMap] index map is expanded to exp="+exponent);
			}
			
			int nBuckets = buckets.length;
			Node[] expandedBuckets = new Node[2*nBuckets];
			
			int expandedBucketIndex;
			Node node, expandedNode;
			for(int bucketIndex=0; bucketIndex<nBuckets; bucketIndex++) {
				node = buckets[bucketIndex];
				while(node!=null) {
					expandedNode = new Node(node.index, node.object);
					
					// store
					expandedBucketIndex = expandedNode.getBucketIndex();
					expandedNode.next = expandedBuckets[expandedBucketIndex];
					expandedBuckets[expandedBucketIndex] = expandedNode;
				
					// move to next
					node = node.next;
				}
			}
			buckets = expandedBuckets;
		}
	}

	private void collapse() {
		if(exponent>6) {
			
			exponent--;
			mask = mask(exponent);
			if(IS_DEBUG_ACTIVE) {
				System.out.println("[QxIndexMap] index map is collasped to exp="+exponent);
			}
			int nBuckets = buckets.length;
			Node[] collapsedBuckets = new Node[nBuckets/2];
			
			int collapsedBucketIndex;
			Node node, collapsedNode;
			for(int bucketIndex=0; bucketIndex<nBuckets; bucketIndex++) {
				node = buckets[bucketIndex];
				while(node!=null) {
					collapsedNode = new Node(node.index, node.object);
					
					// store
					collapsedBucketIndex = collapsedNode.getBucketIndex();
					collapsedNode.next = collapsedBuckets[collapsedBucketIndex];
					collapsedBuckets[collapsedBucketIndex] = collapsedNode;
				
					// move to next
					node = node.next;
				}
			}
			buckets = collapsedBuckets;
		}
	}


	public int size() {
		return (int) size;
	}

	
	/**
	 * /!\ Index is copied to avoid messing all things up with external
	 * incrementation of index.
	 * @param index: the index used to store the object.
	 * @param object
	 */
	public void put(QxIndex index, Object object) {
		int hascode = mask & index.toInt32();
		Node head = buckets[hascode];
		Node node = head;
		boolean isStored = false;
		while(node!=null && !isStored) {
			if(node.index.equals(index)) {
				node.object = object;
			}
			else {
				node = node.next;
			}
		}
		if(!isStored) {
			buckets[hascode] = new Node(index.copy(), object, head);
			size++;
			update();
		}
	}



	public boolean contains(QxIndex index) {
		return get(index)!=null;
	}


	public Object get(QxIndex index) {
		int hascode = mask & index.toInt32();
		Node node = buckets[hascode];
		while(node!=null) {
			if(node.index.equals(index)) {
				return node.object;
			}
			else {
				node = node.next;
			}
		}
		return null;
	}


	public void remove(QxIndex index) {
		int hascode = mask & index.toInt32();
		Node node = buckets[hascode];
		Node previous = null;
		boolean isRemoved = false;
		while(node!=null && !isRemoved) {
			if(node.index.equals(index)) {
				if(previous==null) { // is head
					buckets[hascode] = node.next;
				}
				else {
					previous.next = node.next;
				}
				update();
				isRemoved = true;
			}
			else {
				previous = node;
				node = node.next;
			}
		}
	}



	/*
	 * UTILITIES
	 */


	/**
	 * 
	 * @param n
	 * @return
	 */
	public static int lowerPowerOfTwoExponent(int n) {
		int iterExponent = 0;
		while(n!=0x00) {
			n = n >> 1; iterExponent++;
		}
		return --iterExponent;
	}


	/**
	 * 
	 * @param exponent: must be greater or equal than 2
	 * @return 2^exponent
	 */
	public static int powerOfTwo(int exponent) {
		return 0x01 << exponent;
	}


	/**
	 * 
	 * @param length
	 * @return
	 */
	public static int mask(int length) {
		int mask = 0x0;
		for(int i=0; i<length; i++) {
			mask = (mask << 1) | 0x1;
		}
		return mask;
	}
}
