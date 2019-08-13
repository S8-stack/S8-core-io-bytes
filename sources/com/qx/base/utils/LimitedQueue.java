package com.qx.base.utils;

import java.util.LinkedList;

/**
 * Created by Adel Abbas on 25/04/2017.
 */
public class LimitedQueue<E> extends LinkedList<E> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int limit;

    public LimitedQueue(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(E o) {
        boolean added = super.add(o);
        while (added && size() > limit) {
            super.remove();
        }
        return added;
    }
}
