package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T>{

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

    private T[]container;

    public ArrayDeque() {
        size = 0;
        usageRatio = 0;
        first = 0;
        last = 1;
        //default length is 8
        container = (T[]) new Object[8];
    }

    @Override
    public void addFirst(T item) {
        if (size == container.length) {
            resize(2 * size);
        }

        container[first] = item;
        first = Math.floorMod(first - 1, container.length);
        size++;
    }

    @Override
    public void addLast(T item) {
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
        for (T x : this) {
            System.out.print(x + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
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
        T result = clear(0);

        first = Math.floorMod(first + 1, container.length);
        size--;
        return result;
    }

    @Override
    public T removeLast() {
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
        T result = clear(size - 1);

        last = Math.floorMod(last - 1, container.length);
        size--;
        return result;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            System.out.println("invalid index : " + index + " valid range : (0," + size + ")");
            return null;
        }
        // rightIndex = (first + 1 + index) % container.length
        int rightIndex = Math.floorMod(first + 1 + index, container.length);
        return container[rightIndex];
    }

    private T clear(int index) {
        if (index < 0 || index >= size) {
            System.out.println("invalid index : " + index + " valid range : (0," + size + ")");
            return null;
        }
        // rightIndex = (first + 1 + index) % container.length
        int rightIndex = rightIndex(index);
        T result = container[rightIndex];
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
        T[] newContainer = (T[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            newContainer[i] = get(i);
        }
        container = newContainer;
        first = newContainer.length - 1;
        last = size;
    }

    @Override
    public Iterator<T> iterator() {
        return new arrayDequeIterator<>();
    }

    private class arrayDequeIterator<T> implements Iterator<T> {

        int index;
        public arrayDequeIterator() {
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public T next() {
            T result = (T) container[rightIndex(index)];
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
        if (item == null) {
            return false;
        }
        if (this == item) {
            return true;
        }
        if (item.getClass() != this.getClass())
            if (item.getClass() != LinkedListDeque.class)
                return false;

        Deque <T> tmp = (Deque <T>) item;
        if (size != tmp.size())
            return false;

        for (int i = 0; i < size; i++) {
            if (!get(i).equals(tmp.get(i)))
                return false;
        }
        return true;

    }
}
