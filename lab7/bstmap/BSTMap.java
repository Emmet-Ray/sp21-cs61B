package bstmap;

import jh61b.junit.In;

import java.security.Key;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V> implements Map61B<K,V>{

   private BSTNode root;
   private int size;
   private Set<K> keys;
    private class BSTNode {

        BSTNode left;
        BSTNode right;
        K key;
        V value;
        public BSTNode(K k, V v) {
            left = null;
            right = null;
            key = k;
            value = v;
        }

    }

    public BSTMap() {
        root = null;
        size = 0;
        keys = new HashSet<>();
    }

    /** search method */
    private BSTNode getHelper(BSTNode root, K k) {
        if (root == null) {
            return null;
        }
        int result = root.key.compareTo(k);
        if (result == 0) {
            return root;
        } else if (result < 0) { // root < k
            return getHelper(root.right, k);
        } else {
            return getHelper(root.left, k);
        }
    }

    // return new root after insert (k, v) into root
    private BSTNode Insert(BSTNode root, K k, V v) {
        if (root == null) {
           return new BSTNode(k, v);
        }
        int result = root.key.compareTo(k);
        if (result == 0) {
            root.value = v;
        } else if (result < 0) { // root < k
            root.right = Insert(root.right, k, v);
        } else {
            root.left = Insert(root.left, k, v);
        }
        return root;
    }

    private BSTNode delete(BSTNode root, K k) {
       if (root == null) {
           return null;
       }
       int result = root.key.compareTo(k);
       if (result < 0) {
           root.right = delete(root.right, k);
       } else if (result > 0) {
           root.left = delete(root.left, k);
       } else {
           if (root.left == null) {
               return root.right;
           }
           if (root.right == null) {
               return root.left;
           } else {
                BSTNode min = findMin(root.right);
                root.right = delete(root.right, min.key);
                min.left = root.left;
                min.right = root.right;
                root = min;
           }
       }
       return root;
    }

    private BSTNode findMin(BSTNode node) {
        if (node == null) {
            return null;
        }
        if (node.left == null) {
            return node;
        }
        return findMin(node.left);

    }
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return getHelper(root, key) != null;
    }

    @Override
    public V get(K key) {
        BSTNode tmp = getHelper(root, key);
        if (tmp != null) {
            return tmp.value;
        } else {
            System.out.println("doesn't contain the key : " + key);
            return null;
        }
    }

    @Override
    public int size() {
        return size;
    }

    /** put (key, value) into the map, increment size */
    @Override
    public void put(K key, V value) {
        root = Insert(root, key, value);
        size++;
        keys.add(key);
    }

    @Override
    public Set<K> keySet() {
       return keys;
    }

    @Override
    public V remove(K key) {
        V tmp = get(key);
        root = delete(root, key);
        if (tmp != null) {
            size--;
            keys.remove(key);
            return tmp;
        } else {
            System.out.println("no such key : " + key);
            return null;
        }
    }

    @Override
    public V remove(K key, V value) {
        V tmp = get(key);
        root = delete(root, key);
        if (tmp != null) {
            size--;
            keys.remove(key);
            return tmp;
        } else {
            System.out.println("no such key : " + key);
            return null;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }
    private class BSTMapIterator<K> implements Iterator<K>{
        Iterator keysIterator;
        public BSTMapIterator() {
            keysIterator = keys.iterator();
        }
        @Override
        public boolean hasNext() {
            return keysIterator.hasNext();
        }

        @Override
        public K next() {
            return (K) keysIterator.next();
        }
    }

    private void unsupportOperation() throws Exception {
        Exception e = new UnsupportedOperationException();
        throw e;
    }
}
