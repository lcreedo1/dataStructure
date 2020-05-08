/**
 * Liam Creedon, lcreedo1, lcreedo1@jhu.edu
 */

package hw7;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Treap map from comparable keys to arbitrary values with random priorities,
 * implemented with an ordered map.
 *
 * @param <K> Type for keys.
 * @param <V> Type for values.
 */
public class TreapMap<K extends Comparable<? super K>, V>
    implements OrderedMap<K, V> {

    /** Random number generator for assigning priorities. */
    private static final Random NUMGEN = new Random();

    /** Node class to hold data of tree. */
    private class Node {
        /** Left child node. */
        Node left;
        /** Right child node. */
        Node right;
        /** Key of node. */
        K key;
        /** Value of node. */
        V value;
        /** Priority. **/
        final int pri;

        /**
         * Constructor to make node creation easier to read.
         * @param k key.
         * @param v value.
         */
        Node(K k, V v) {
            this.key = k;
            this.value = v;
            this.left = null;
            this.right = null;
            this.pri = NUMGEN.nextInt();
        }

        /**
         * Just for debugging purposes.
         * @return content of node.
         */
        public String toString() {
            return "Node<key: " + this.key
                + "; value: " + this.value
                + ">";
        }
    }

    /** Root node of the tree. */
    private Node root;
    /** Size of the tree. */
    private int size;
    /** String used for toString method. */
    private StringBuilder stringBuilder;

    @Override
    public int size() {
        return this.size;
    }

    /**
     * Find method to locate a node with key 'k'.
     * @param k key of node to be found.
     * @return Node holding k.
     */
    private Node find(K k) {
        if (k == null) {
            throw new IllegalArgumentException("cannot handle null key");
        }
        Node n = this.root;
        while (n != null) {
            int cmp = k.compareTo(n.key);
            if (cmp < 0) {
                n = n.left;
            } else if (cmp > 0) {
                n = n.right;
            } else {
                return n;
            }
        }
        return null;
    }

    @Override
    public boolean has(K k) {
        if (k == null) {
            return false;
        }
        return this.find(k) != null;
    }

    /**
     * Return node for given key, throw an exception if not in tree.
     * @param k key to search for.
     * @return null if key does not exist, node holding k if it does.
     */
    private Node findForSure(K k) {
        Node n = this.find(k);
        if (n == null) {
            throw new IllegalArgumentException("cannot find key " + k);
        }
        return n;
    }

    @Override
    public void put(K k, V v) {
        Node n = this.findForSure(k);
        n.value = v;
    }

    @Override
    public V get(K k) {
        Node n = this.findForSure(k);
        return n.value;
    }

    /**
     * performs a right rotation on given node and subtrees.
     * @param n root node to be rotated.
     * @return new root node.
     */
    private Node rotateRight(Node n) {
        Node rootLC = n.left;
        n.left = rootLC.right;
        rootLC.right = n;

        return rootLC;
    }

    /**
     * performs a left rotation on given node and subtrees.
     * @param n root node to be rotated.
     * @return new root node.
     */
    private Node rotateLeft(Node n) {
        Node rootRC = n.right;
        n.right = rootRC.left;
        rootRC.left = n;

        return rootRC;
    }

    /**
     * Properly inserts a node into the tree using recursion.
     * Performs necessary rotations if unbalanced.
     * @param n root node.
     * @param k key of node.
     * @param v value of node.
     * @return newly inserted node.
     */
    private Node insertNode(Node n, K k, V v) {
        if (n == null) {
            return new Node(k, v);
        }

        int cmp = k.compareTo(n.key);
        if (cmp < 0) {
            n.left = this.insertNode(n.left, k, v);
            if (n.pri > n.left.pri) {
                return rotateRight(n);
            }
        } else if (cmp > 0) {
            n.right = this.insertNode(n.right, k, v);
            if (n.pri > n.right.pri) {
                return rotateLeft(n);
            }
        } else {
            throw new IllegalArgumentException("duplicate key " + k);
        }
        return n;
    }

    @Override
    public void insert(K k, V v) {
        if (k == null) {
            throw new IllegalArgumentException("cannot handle null key");
        }
        this.root = this.insertNode(this.root, k, v);
        this.size += 1;
    }

    /**
     * Find maximum value (node at bottom right location in tree).
     * @param n root Node.
     * @return max Node.
     */
    private Node max(Node n) {
        while (n.right != null) {
            n = n.right;
        }
        return n;
    }

    /**
     * returns smallest node in given tree.
     * @param n root node.
     * @return min value node.
     */
    private Node minValueNode(Node n) {
        Node current = n;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    /**
     * removes node and performs rotations to keep tree balanced.
     * @param n current root.
     * @param k current key.
     * @return node to be removed
     */
    private Node removeNode(Node n, K k) {
        if (n == null) {
            return n;
        }
        // node in left subtree
        int cmp = k.compareTo(n.key);
        if (cmp < 0) {
            n.left = removeNode(n.left, k);

        // node in right subtree
        } else if (cmp > 0) {
            n.right = removeNode(n.right, k);

        // this is the node to be deleted
        } else {
            if ((n.left == null) || (n.right == null)) {
                Node temp = null;
                if (temp == n.left) {
                    temp = n.right;
                } else {
                    temp = n.left;
                }
                if (temp == null) {
                    temp = n;
                    n = null;
                } else {
                    n = temp;
                }
            } else {
                Node temp = minValueNode(n.right);
                n.key = temp.key;
                n.right = removeNode(n.right, temp.key);
            }
        }
        return n;
    }

    @Override
    public V remove(K k) {
        V v = this.findForSure(k).value;
        this.root = this.removeNode(this.root, k);
        this.size -= 1;
        return v;
    }

    /**
     * Recursively add subtree keys of given node.
     * @param n Node to work from.
     * @param keys List to add keys to.
     */
    private void iteratorHelper(Node n, List<K> keys) {
        if (n == null) {
            return;
        }
        this.iteratorHelper(n.left, keys);
        keys.add(n.key);
        this.iteratorHelper(n.right, keys);
    }

    @Override
    public Iterator<K> iterator() {
        List<K> keys = new ArrayList<K>();
        this.iteratorHelper(this.root, keys);
        return keys.iterator();
    }

    /**
     * If we don't have a StringBuilder yet, make one;
     * otherwise just reset it back to a clean slate.
     */
    private void setupStringBuilder() {
        if (this.stringBuilder == null) {
            this.stringBuilder = new StringBuilder();
        } else {
            this.stringBuilder.setLength(0);
        }
    }

    /**
     * Recursively append string representations of keys and
     * values from subtree rooted at given node.
     * @param n root node to work from.
     * @param s stringBuilder to add to.
     */
    private void toStringHelper(Node n, StringBuilder s) {
        if (n == null) {
            return;
        }
        this.toStringHelper(n.left, s);
        s.append(n.key);
        s.append(": ");
        s.append(n.value);
        s.append(", ");
        this.toStringHelper(n.right, s);
    }

    @Override
    public String toString() {
        this.setupStringBuilder();
        this.stringBuilder.append("{");

        this.toStringHelper(this.root, this.stringBuilder);

        int length = this.stringBuilder.length();
        if (length > 1) {
            // If anything was appended at all, get rid of
            // the last ", " the toStringHelper put in.
            this.stringBuilder.setLength(length - 2);
        }
        this.stringBuilder.append("}");

        return this.stringBuilder.toString();
    }
}
