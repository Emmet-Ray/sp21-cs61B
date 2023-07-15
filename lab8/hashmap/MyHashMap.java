package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    @Override
    public void clear() {
        for (Collection c : buckets) {
            c.clear();
        }
        size = 0;
        keySet.clear();
    }

    @Override
    public boolean containsKey(K key) {
        return keySet.contains(key);
    }

    @Override
    public V get(K key) {
        if (!containsKey(key)) {
            return null;
        }
        int index = reduce(key);
        Node n = getFromBucket(index, key);
        return n.value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if ((size + 1) / bucketNumber > loadFactor) {
            resize(2);
        }
        Node newNode = createNode(key, value);
        int index = reduce(key);
        if (containsKey(key)) {
            Node oldNode = getFromBucket(index, key);
            buckets[index].remove(oldNode);
            size--;
        }
        buckets[index].add(newNode);
        size++;
        keySet.add(key);
    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    public V remove(K key) {
        int index = reduce(key);
        Node n = getFromBucket(index, key);
        if (n == null) {
            return null;
        } else {
            buckets[index].remove(n);
            size--;
            keySet.remove(key);
            return n.value;
        }
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }

    /**
     *
     * @param key
     * @return reduce buckets index
     */
    private int reduce(K key) {
        return Math.floorMod(key.hashCode(), bucketNumber);
    }

    /**
     *
     * @param key
     * @return the node with the [key] from buckets[index] OR null if it is not there
     */
    private Node getFromBucket(int index, K key) {
        for (Node n : buckets[index]) {
            if (n.key.equals(key)) {
                return n;
            }
        }
        return null;
    }

    private void resize(int multiply) {
        int newBucketNumber = bucketNumber * multiply;
        int oldBucketNumer = bucketNumber;
        bucketNumber = newBucketNumber;
        Collection<Node>[] newBuckets = createTable(newBucketNumber);
        initializeBuckets(newBuckets);
        Collection<Node>[] temp = buckets;
        buckets = newBuckets;
        // reset size = 0; put items to rearrange all the items to their new position
        size = 0;
        keySet.clear();
        for (int i = 0; i < oldBucketNumer; i++) {
            for (Node n : temp[i]) {
                put(n.key, n.value);
            }
        }
    }

    private void initializeBuckets(Collection[] buckets) {
        for (int i = 0; i < bucketNumber; i++) {
            buckets[i] = createBucket();
        }
    }
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int size;
    private int bucketNumber;
    private double loadFactor;

    private Set<K> keySet;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        size = 0;
        bucketNumber = 16;
        loadFactor = 0.75;
        buckets = createTable(16);
        keySet = new HashSet<>();
        initializeBuckets(buckets);
    }

    public MyHashMap(int initialSize) {
        size = 0;
        bucketNumber = initialSize;
        loadFactor = 0.75;
        buckets = createTable(bucketNumber);
        keySet = new HashSet<>();
        initializeBuckets(buckets);

    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        size = 0;
        bucketNumber = initialSize;
        loadFactor = maxLoad;
        buckets = createTable(bucketNumber);
        keySet = new HashSet<>();
        initializeBuckets(buckets);

    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }


}
