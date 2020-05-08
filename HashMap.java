/**
 * Liam Creedon, lcreedo1, lcreedo1@jhu.edu
 * Kevin Cameron, kcamer12, kcamer12@jhu.edu
 */

package hw8;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * HashMap containing unique keys, that are converted to hashCode.
 * HashMap data is an ArrayList that contains HashNodes.
 * Collisions handled using separate chaining with a linked list.
 * HashNodes have data fields for an arbitrary key, value, and next node.
 * Next node is used for the linked list aspect of separate chaining.
 * The ArrayList is doubled when a load factor of 0.6 is exceeded.
 * @param <K> Arbitrary key to be used.
 * @param <V> Arbitrary value to be used.
 */
public class HashMap<K, V> implements Map<K, V> {

    /**
     * Node to hold key value pairs.
     * @param <K> Arbitrary key to be used.
     * @param <V> Arbitrary value to be used.
     */
    private class HashNode<K, V> {
        /** Key. */
        K key;
        /** Value. */
        V val;
        /** Node to create linked list if need be. */
        HashNode<K, V> next;

        /**
         * @param k Key.
         * @param v Value.
         */
        HashNode(K k, V v) {
            this.key = k;
            this.val = v;
            this.next = null;
        }
    }

    /** Initial size of bucket. */
    public static final int INITIALSIZE = 10;
    /** ArrayList to hold HashNodes. */
    private ArrayList<HashNode<K, V>> bucket = new ArrayList<>();
    /** Size of the bucket. */
    private int bucketSize;
    /** Number of entries in the bucket. */
    private int size;

    /**
     * Constructor.
     */
    public HashMap() {
        this.bucketSize = INITIALSIZE;
        for (int i = 0; i < bucketSize; i++) {
            bucket.add(null);
        }
        this.size = 0;
    }

    /**
     * Turns key values into hashCode using built in hashCode function.
     * @param k key to be converted.
     * @return hashCode.
     */
    private int hash(K k) {
        int hashed = Math.abs(k.hashCode());
        return hashed % bucketSize;
    }

    /**
     * Resizes the HashMap if load is greater than 0.6.
     */
    private void resize() {
        ArrayList<HashNode<K, V>> temp = bucket;
        bucket = new ArrayList<>();
        bucketSize *= 2;
        //initializes to null
        for (int i = 0; i < bucketSize; i++) {
            bucket.add(null);
        }
        //copies values over
        for (HashNode<K, V> node : temp) {
            HashNode<K, V> link = node;
            while (link != null) {
                insert(link.key, link.val);
                link = link.next;
            }
        }
    }

    /**
     * Helper method to find the HashNode associated with a given key.
     * @param k Key to search for.
     * @return HashNode associated with key.
     */
    private HashNode<K, V> find(K k) {
        int loc = hash(k);
        HashNode<K, V> head = bucket.get(loc);
        while (head != null) {
            if (head.key.equals(k)) {
                return head;
            }
            head = head.next;
        }
        return null;
    }

    @Override
    public void insert(K k, V v) throws IllegalArgumentException {
        if (k == null) {
            throw new IllegalArgumentException();
        }
        int loc = hash(k);
        HashNode<K, V> head = find(k);
        if (head != null) {
            throw new IllegalArgumentException();
        }
        HashNode<K, V> add = new HashNode<>(k, v);

        bucket.set(loc, add);
        size++;
        if (size / bucketSize > 0.6) {
            resize();
        }
    }

    @Override
    public V remove(K k) throws IllegalArgumentException {
        if (k == null) {
            throw new IllegalArgumentException();
        }
        int loc = hash(k);
        HashNode<K, V> trail = bucket.get(loc);
        if (trail == null) {
            throw new IllegalArgumentException();
        }
        HashNode<K, V> head = trail.next;
        if (trail.key.equals(k)) {
            trail.key = null;
            V v = trail.val;
            trail.val = null;
            bucket.add(loc, head);
            size--;
            return v;
        }
        else {
            while (head != null) {
                if (head.key.equals(k)) {
                    head.key = null;
                    V v = head.val;
                    head.val = null;
                    trail.next = head.next;
                    size--;
                    return v;
                }
                trail = head;
                head = head.next;
            }
        }
        return null;
    }

    @Override
    public void put(K k, V v) throws IllegalArgumentException {
        if (k == null) {
            throw new IllegalArgumentException();
        }

        HashNode<K, V> head = find(k);
        if (head == null) {
            throw new IllegalArgumentException();
        }
        if (head.key.equals(k)) {
            head.val = v;
        } else {
            while (head.next != null) {
                if (head.key.equals(k)) {
                    head.val = v;
                    break;
                }
                head = head.next;
            }
        }
    }

    @Override
    public V get(K k) throws IllegalArgumentException {
        if (k == null) {
            throw new IllegalArgumentException();
        }
        if (find(k) == null) {
            throw new IllegalArgumentException();
        }
        return find(k).val;
    }

    @Override
    public boolean has(K k) {
        if (k == null) {
            throw new IllegalArgumentException();
        }
        if (find(k) == null) {
            return false;
        }
        return true;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<K> iterator() {
        List<K> keys = new ArrayList<K>();
        for (HashNode<K, V> node : this.bucket) {
            HashNode<K, V> temp = node;
            if (temp != null) {
                keys.add(temp.key);
                while (temp.next != null) {
                    keys.add(temp.next.key);
                    temp = temp.next;
                }
            }

        }
        return keys.iterator();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        boolean empty = true;
        s.append("{");
        for (HashNode<K, V> hn : bucket) {
            if (hn != null) {
                empty = false;
                s.append("" + hn.key + ": " + hn.val);
                s.append(", ");
                HashNode<K, V> temp = hn.next;
                while (temp != null) {
                    s.append("" + temp.key + ": " + temp.val);
                    s.append(", ");
                    temp = temp.next;
                }
            }
        }
        if (!empty) {
            s.deleteCharAt(s.length() - 1);
            s.deleteCharAt(s.length() - 1);
        }
        s.append("}");
        return s.toString();
    }
}
