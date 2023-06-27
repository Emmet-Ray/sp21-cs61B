package deque;

public class LinkedListDeque<Type> {

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



    public void addFirst(Type item)  {
        TypeNode tmp = new TypeNode(item);

        tmp.next = sentinel.next;
        sentinel.next.previous = tmp;

        sentinel.next = tmp;
        tmp.previous = sentinel;

        size++;
    }
    public void addLast(Type item)  {
        TypeNode tmp = new TypeNode(item);

        tmp.previous = sentinel.previous;
        sentinel.previous.next = tmp;

        sentinel.previous = tmp;
        tmp.next = sentinel;

        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }
    public int size() {
        return size;
    }

    public void printDeque() {
        TypeNode p = sentinel.next;
        while (p != sentinel) {
            System.out.printf(p.item +  " ");
            p = p.next;
        }
        System.out.println();
    }

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
}
