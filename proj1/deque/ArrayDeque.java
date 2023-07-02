package deque;

import java.util.Arrays;
import java.util.Iterator;

public class ArrayDeque<type> implements Deque<type>, Iterable<type>{

    /*
        some invariants :
        1.size should always be the number of items in container
        2.first should always be the index of the next first item
        3.last should always be the index of the next last item
     */

    private int size;

    private int first;
    private int last;
    private double usageRatio;

    private type[]container;

    public ArrayDeque() {
        size = 0;
        usageRatio = 0;
        first = 0;
        last = 1;
        //default length is 8
        container = (type[]) new Object[8];
    }

    @Override
    public void addFirst(type item) {
        if (size == container.length) {
            resize(2 * size);
        }

        container[first] = item;
        first = Math.floorMod(first - 1, container.length);
        size++;
    }

    @Override
    public void addLast(type item) {
        if (size == container.length) {
            resize(2 * size);
        }

        container[last] = item;
        last = Math.floorMod(last + 1, container.length);
        size++;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        /*
        int rightIndex;
        for (int i = 0; i < size; i++) {
            rightIndex = rightIndex(i);
            System.out.println(container[rightIndex] + " ");
        }
        System.out.println();
         */
        for (type x : this) {
            System.out.print(x + " ");
        }
        System.out.println();
    }

    @Override
    public type removeFirst() {
        if (isEmpty()) {
            System.out.println("the Deque is empty, invalid removeFirst");
            return null;
        }
        usageRatio = (double) (size - 1) / container.length;
        if (container.length >= 16 && usageRatio < 0.25) {
            resize(container.length / 2);
        }
        if (size >= 16)
            System.out.println("usage : " + usageRatio);
        // return the first item and set the item to null
        type result = clear(0);

        first = Math.floorMod(first + 1, container.length);
        size--;
        return result;
    }

    @Override
    public type removeLast() {
        if (isEmpty()) {
            System.out.println("the Deque is empty, invalid removeFirst");
            return null;
        }
        usageRatio = (double) (size - 1) / container.length;
        if (container.length >= 16 && usageRatio < 0.25) {
            resize(container.length / 2);
        }
        if (size >= 16)
            System.out.println("usage : " + usageRatio);
        // return the last item and set the item to null
        type result = clear(size - 1);

        last = Math.floorMod(last - 1, container.length);
        size--;
        return result;
    }

    @Override
    public type get(int index) {
        if (index < 0 || index >= size) {
            System.out.println("invalid index : " + index + " valid range : (0," + size + ")");
            return null;
        }
        // rightIndex = (first + 1 + index) % container.length
        int rightIndex = Math.floorMod(first + 1 + index, container.length);
        return container[rightIndex];
    }

    private type clear(int index) {
        if (index < 0 || index >= size) {
            System.out.println("invalid index : " + index + " valid range : (0," + size + ")");
            return null;
        }
        // rightIndex = (first + 1 + index) % container.length
        int rightIndex = rightIndex(index);
        type result = container[rightIndex];
        container[rightIndex] = null;
        return result;
    }

    private int rightIndex(int index) {
        return  Math.floorMod(first + 1 + index, container.length);
    }
    /**
     *  resize the length of the array to capacity
     * @param capacity
     */
    private void resize(int capacity) {
        type[] newContainer = (type[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            newContainer[i] = get(i);
        }
        container = newContainer;
        first = newContainer.length - 1;
        last = size;
    }

    @Override
    public Iterator<type> iterator() {
        return new arrayDequeIterator<>();
    }

    private class arrayDequeIterator<type> implements Iterator<type> {

        int index;
        public arrayDequeIterator() {
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public type next() {
            type result = (type) container[rightIndex(index)];
            index++;
            return result;
        }
    }

    @Override
    public boolean equals(Object item) {
        /*
        if (item instanceof ArrayDeque tmp) {
            if (size != tmp.size)
                return false;

            for (int i = 0; i < size; i++) {
                if (!get(i).equals(tmp.get(i)))
                    return false;
            }
            return true;
        }
         */
        if (item == null)
            return false;
        if (this == item)
            return true;
        if (item.getClass() != this.getClass())
            return false;

        ArrayDeque<type> tmp = (ArrayDeque<type>) item;
        if (size != tmp.size)
            return false;

        for (int i = 0; i < size; i++) {
            if (!get(i).equals(tmp.get(i)))
                return false;
        }
        return true;

    }
}
