package deque;

public class ArrayDeque<type> {

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

    public void addFirst(type item) {
        if (size == container.length) {
            // need to resize
        }

        container[first] = item;
        first = Math.floorMod(first - 1, container.length);
        size++;
    }

    public void addLast(type item) {
        if (size == container.length) {
            // need to resize
        }

        container[last] = item;
        last = Math.floorMod(last + 1, container.length);
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.println(container[i] + " ");
        }
        System.out.println();
    }

    public type removeFirst() {
        if (isEmpty()) {
            System.out.println("the Deque is empty, invalid removeFirst");
            return null;
        }
        //TODO consider resize
        usageRatio = (double) (size - 1) / container.length;
        if (container.length >= 16 && usageRatio < 0.25) {
            // resize
        }

        // return the first item and set the item to null
        type result = clear(0);

        first = Math.floorMod(first + 1, container.length);
        size--;
        return result;
    }

    public type removeLast() {
        if (isEmpty()) {
            System.out.println("the Deque is empty, invalid removeFirst");
            return null;
        }
        //TODO consider resize
        usageRatio = (double) (size - 1) / container.length;
        if (container.length >= 16 && usageRatio < 0.25) {
            // resize
        }
        // return the last item and set the item to null
        type result = clear(size - 1);

        last = Math.floorMod(last - 1, container.length);
        size--;
        return result;
    }

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
        int rightIndex = Math.floorMod(first + 1 + index, container.length);
        type result = container[rightIndex];
        container[rightIndex] = null;
        return result;
    }
}
