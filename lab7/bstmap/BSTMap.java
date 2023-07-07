package bstmap;

import jh61b.junit.In;

import java.security.Key;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V> implements Map61B<K,V>{

   private BSTNode root;
   private int size;
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
    }

    @Override
    public Set<K> keySet() {
        try {
            unsupportOperation();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public V remove(K key) {
        try {
            unsupportOperation();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public V remove(K key, V value) {
        try {
            unsupportOperation();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        try {
            unsupportOperation();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void unsupportOperation() throws Exception {
        Exception e = new UnsupportedOperationException();
        throw e;
    }
}
