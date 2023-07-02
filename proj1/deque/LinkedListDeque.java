package deque;

import org.junit.Test;

import java.util.Iterator;

public class LinkedListDeque<Type> implements Deque<Type>, Iterable<Type> {

    /*
        some invariants
        1, size should be the number of items
     */
    private int size;
    private TypeNode sentinel;

    private class TypeNode {
        Type item;
        TypeNode next;
        TypeNode previous;

        private TypeNode(Type value) {
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
       sentinel = new TypeNode(null);
       sentinel.next = sentinel.previous = sentinel;
    }


    @Override
    public void addFirst(Type item)  {
        TypeNode tmp = new TypeNode(item);

        tmp.next = sentinel.next;
        sentinel.next.previous = tmp;

        sentinel.next = tmp;
        tmp.previous = sentinel;

        size++;
    }
    @Override
    public void addLast(Type item)  {
        TypeNode tmp = new TypeNode(item);

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
        TypeNode p = sentinel.next;
        while (p != sentinel) {
            System.out.printf(p.item +  " ");
            p = p.next;
        }
        System.out.println();
         */
        for (Type x : this) {
            System.out.printf(x +  " ");
        }
        System.out.println();
    }

    @Override
    public Type removeFirst() {
        if (isEmpty())
            return null;
        Type result = sentinel.next.item;

        TypeNode firstPointer = sentinel.next;
        sentinel.next.next.previous = sentinel;
        sentinel.next = firstPointer.next;

        size--;
        return result;
    }

    @Override
    public Type removeLast() {
        if (isEmpty())
            return null;

        Type result = sentinel.previous.item;

        TypeNode lastPointer = sentinel.previous;
        sentinel.previous.previous.next = sentinel;
        sentinel.previous = lastPointer.previous;
        size--;
        return result;
    }

    @Override
    public Type get(int index) {
        if (index >= size()) {
           return null;
        }
        TypeNode p = sentinel.next;
        int cnt = 0;
        while (cnt < index) {
            p = p.next;
            cnt++;
        }
        return p.item;
    }

    public Type getRecursive(int index) {
        return helpeRecursiveGet(index, sentinel.next);
    }
    private Type helpeRecursiveGet(int index, TypeNode node) {
        if (index >= size())
            return null;

        if (index == 0)
            return node.item;
        else {
            return helpeRecursiveGet(index-1, node.next);
        }
    }

    @Override
    public Iterator<Type> iterator() {
        return new LLDequeIterator();
    }

    private class LLDequeIterator<Type> implements Iterator<Type> {
        TypeNode p;

        public LLDequeIterator() {
            p = sentinel.next;
        }
        @Override
        public boolean hasNext() {
           return p != sentinel;
        }
        @Override
        public Type next() {
            Type result = (Type) p.item;
            p = p.next;
            return result;
        }
    }

    @Override
    public boolean equals(Object item) {
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
    }
}
