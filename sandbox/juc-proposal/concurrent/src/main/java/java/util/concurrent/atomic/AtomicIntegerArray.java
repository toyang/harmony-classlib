/* Copyright 2006 The Apache Software Foundation or its licensors, as applicable
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */

package java.util.concurrent.atomic;
import java.util.Arrays;

import org.apache.harmony.concurrent.AtomicSupport;

/**
 * An <tt>int</tt> array in which elements may be updated atomically.
 * See the {@link java.util.concurrent.atomic} package
 * specification for description of the properties of atomic
 * variables.
 * @since 1.5
 * @author Doug Lea
 */
public class AtomicIntegerArray implements java.io.Serializable { 
    private static final long serialVersionUID = 2862133569453604235L;
    
    private static final AtomicSupport SUPPORT = AtomicSupport.getInstance();

    private final int[] array;

    /**
     * Create a new AtomicIntegerArray of given length.
     *
     * @param length the length of the array
     */
    public AtomicIntegerArray(int length) {
        array = new int[length];
        // must perform at least one volatile write to conform to JMM
        if (length > 0) {
            SUPPORT.volatileSet(array, 0, 0);
        }
    }

    /**
     * Create a new AtomicIntegerArray with the same length as, and
     * all elements copied from, the given array.
     *
     * @param array the array to copy elements from
     * @throws NullPointerException if array is null
     */
    public AtomicIntegerArray(int[] array) {
        if (array == null) 
            throw new NullPointerException();
        int length = array.length;
        this.array = new int[length];
        if (length > 0) {
            int last = length-1;
            for (int i = 0; i < last; ++i)
                this.array[i] = array[i];
            // Do the last write as volatile
            SUPPORT.volatileSet(this.array, last, array[last]);
        }
    }

    /**
     * Returns the length of the array.
     *
     * @return the length of the array
     */
    public final int length() {
        return array.length;
    }

    /**
     * Get the current value at position <tt>i</tt>.
     *
     * @param i the index
     * @return the current value
     */
    public final int get(int i) {
        return SUPPORT.volatileGet(array, i);
    }
 
    /**
     * Set the element at position <tt>i</tt> to the given value.
     *
     * @param i the index
     * @param newValue the new value
     */
    public final void set(int i, int newValue) {
        SUPPORT.volatileSet(array, i, newValue);
    }
  
    /**
     * Set the element at position <tt>i</tt> to the given value and return the
     * old value.
     *
     * @param i the index
     * @param newValue the new value
     * @return the previous value
     */
    public final int getAndSet(int i, int newValue) {
        while (true) {
            int current = get(i);
            if (compareAndSet(i, current, newValue))
                return current;
        }
    }
  
    /**
     * Atomically set the value to the given updated value
     * if the current value <tt>==</tt> the expected value.
     *
     * @param i the index
     * @param expect the expected value
     * @param update the new value
     * @return true if successful. False return indicates that
     * the actual value was not equal to the expected value.
     */
    public final boolean compareAndSet(int i, int expect, int update) {
        return SUPPORT.compareAndSet(array, i, expect, update);
    }

    /**
     * Atomically set the value to the given updated value
     * if the current value <tt>==</tt> the expected value.
     * May fail spuriously.
     *
     * @param i the index
     * @param expect the expected value
     * @param update the new value
     * @return true if successful.
     */
    public final boolean weakCompareAndSet(int i, int expect, int update) {
        return compareAndSet(i, expect, update);
    }

    /**
     * Atomically increment by one the element at index <tt>i</tt>.
     *
     * @param i the index
     * @return the previous value;
     */
    public final int getAndIncrement(int i) {
        while (true) {
            int current = get(i);
            int next = current + 1;
            if (compareAndSet(i, current, next))
                return current;
        }
    }
  
    /**
     * Atomically decrement by one the element at index <tt>i</tt>.
     *
     * @param i the index
     * @return the previous value;
     */
    public final int getAndDecrement(int i) {
        while (true) {
            int current = get(i);
            int next = current - 1;
            if (compareAndSet(i, current, next))
                return current;
        }
    }
  
    /**
     * Atomically add the given value to element at index <tt>i</tt>.
     *
     * @param i the index
     * @param delta the value to add
     * @return the previous value;
     */
    public final int getAndAdd(int i, int delta) {
        while (true) {
            int current = get(i);
            int next = current + delta;
            if (compareAndSet(i, current, next))
                return current;
        }
    }

    /**
     * Atomically increment by one the element at index <tt>i</tt>.
     *
     * @param i the index
     * @return the updated value;
     */
    public final int incrementAndGet(int i) {
        while (true) {
            int current = get(i);
            int next = current + 1;
            if (compareAndSet(i, current, next))
                return next;
        }
    }
  
    /**
     * Atomically decrement by one the element at index <tt>i</tt>.
     *
     * @param i the index
     * @return the updated value;
     */
    public final int decrementAndGet(int i) {
        while (true) {
            int current = get(i);
            int next = current - 1;
            if (compareAndSet(i, current, next))
                return next;
        }
    }
  
    /**
     * Atomically add the given value to element at index <tt>i</tt>.
     *
     * @param i the index
     * @param delta the value to add
     * @return the updated value;
     */
    public final int addAndGet(int i, int delta) {
        while (true) {
            int current = get(i);
            int next = current + delta;
            if (compareAndSet(i, current, next))
                return next;
        }
    }
 
    /**
     * Returns the String representation of the current values of array.
     * @return the String representation of the current values of array.
     */
    public String toString() {
        if (array.length > 0) // force volatile read
            get(0);
        return Arrays.toString(array);
    }

}
