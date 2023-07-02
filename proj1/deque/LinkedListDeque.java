package deque;

import org.junit.Test;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    /*
        some invariants
        1, size should be the number of items
     */
    private int size;
    private TNode sentinel;

    private class TNode {
        T item;
        TNode next;
        TNode previous;

        private TNode(T value) {
           item = value;
           next = null;
           previous = null;
        }
    }

    /**
     * create an empty circular linked list deque
     */
    public LinkedListDeque() {
       size = 0;
       sentinel = new TNode(null);
       sentinel.next = sentinel.previous = sentinel;
    }


    @Override
    public void addFirst(T item)  {
        TNode tmp = new TNode(item);

        tmp.next = sentinel.next;
        sentinel.next.previous = tmp;

        sentinel.next = tmp;
        tmp.previous = sentinel;

        size++;
    }
    @Override
    public void addLast(T item)  {
        TNode tmp = new TNode(item);

        tmp.previous = sentinel.previous;
        sentinel.previous.next = tmp;

        sentinel.previous = tmp;
        tmp.next = sentinel;

        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        /*
        TNode p = sentinel.next;
        while (p != sentinel) {
            System.out.printf(p.item +  " ");
            p = p.next;
        }
        System.out.println();
         */
        for (T x : this) {
            System.out.printf(x +  " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (isEmpty())
            return null;
        T result = sentinel.next.item;

        TNode firstPointer = sentinel.next;
        sentinel.next.next.previous = sentinel;
        sentinel.next = firstPointer.next;

        size--;
        return result;
    }

    @Override
    public T removeLast() {
        if (isEmpty())
            return null;

        T result = sentinel.previous.item;

        TNode lastPointer = sentinel.previous;
        sentinel.previous.previous.next = sentinel;
        sentinel.previous = lastPointer.previous;
        size--;
        return result;
    }

    @Override
    public T get(int index) {
        if (index >= size()) {
           return null;
        }
        TNode p = sentinel.next;
        int cnt = 0;
        while (cnt < index) {
            p = p.next;
            cnt++;
        }
        return p.item;
    }

    public T getRecursive(int index) {
        return helpeRecursiveGet(index, sentinel.next);
    }
    private T helpeRecursiveGet(int index, TNode node) {
        if (index >= size())
            return null;

        if (index == 0)
            return node.item;
        else {
            return helpeRecursiveGet(index-1, node.next);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new LLDequeIterator();
    }

    private class LLDequeIterator<T> implements Iterator<T> {
        TNode p;

        public LLDequeIterator() {
            p = sentinel.next;
        }
        @Override
        public boolean hasNext() {
           return p != sentinel;
        }
        @Override
        public T next() {
            T result = (T) p.item;
            p = p.next;
            return result;
        }
    }

    @Override
    public boolean equals(Object item) {
        /*
       if (item instanceof LinkedListDeque tmp) {
            if (this.size != tmp.size) {
                return false;
            }

            for (int i = 0; i < size; i++) {
                if (!get(i).equals(tmp.get(i)))
                    return false;
            }
            return true;
       }

       return false;
         */
        if (item == null)
            return false;
        if (this == item)
            return true;
        if (item.getClass() != this.getClass()) {
            if (item.getClass() != ArrayDeque.class)
                return false;
            }

        Deque <T> tmp = (Deque<T>) item;
        if (size != tmp.size())
            return false;

        for (int i = 0; i < size; i++) {
            if (!get(i).equals(tmp.get(i)))
                return false;
        }
        return true;

    }
}
