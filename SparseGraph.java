package hw9;

import exceptions.InsertionException;
import exceptions.PositionException;
import exceptions.RemovalException;

import java.util.ArrayList;
import java.util.List;

/**
    An implementation of a directed graph using incidence lists
    for sparse graphs where most things aren't connected.
    @param <V> Vertex element type.
    @param <E> Edge element type.
*/
public class SparseGraph<V, E> implements Graph<V, E> {

    /**
     * Nested vertex node class for easier access.
     * @param <V> value to be put in.
     */
    private final class VertexNode<V> implements Vertex<V> {
        /** Data to be stored. */
        V data;
        /** Owner of the vertex. */
        Graph<V, E> owner;
        /** Incoming edges. */
        List<Edge<E>> outgoing;
        /** Outgoing edged. */
        List<Edge<E>> incoming;
        /** Label. */
        Object label;
        /** Distance between this vertex. */
        double distance;

        /**
         * Constructor can piss off.
         * @param v value for data.
         */
        VertexNode(V v) {
            this.data = v;
            this.outgoing = new ArrayList<>();
            this.incoming = new ArrayList<>();
            this.distance = 0;
            this.label = null;
        }

        @Override
        public V get() {
            return this.data;
        }

        @Override
        public void put(V v) {
            this.data = v;
        }

        /**
         * Sets distance.
         * @param d distance to be set.
         */
        public void set(double d) {
            this.distance = d;
        }

        /**
         * Gets distance.
         * @return distance.
         */
        public double ret() {
            return this.distance;
        }
    }

    /**
     * Nested edge node class for easier access.
     * @param <E> value to be put in.
     */
    private final class EdgeNode<E> implements Edge<E> {
        /** Data to be stored. */
        E data;
        /** Owner of the edge. */
        Graph<V, E> owner;
        /** Starting vertex. */
        VertexNode<V> from;
        /** Ending vertex. */
        VertexNode<V> to;
        /** Label. */
        Object label;

        /**
         * Constructor can piss off.
         * @param f value for  from vertices.
         * @param t value for to vertices.
         * @param e value type for edge data.
         */
        EdgeNode(VertexNode<V> f, VertexNode<V> t, E e) {
            this.from = f;
            this.to = t;
            this.data = e;
            this.label = null;
        }

        @Override
        public E get() {
            return this.data;
        }

        @Override
        public void put(E e) {
            this.data = e;
        }
    }

    /** List of vertices. */
    private List<Vertex<V>> vertices;

    /** List of edges. */
    private List<Edge<E>> edges;

    /** Constructor for instantiating a graph. */
    public SparseGraph() {
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    // Checks vertex belongs to this graph
    private void checkOwner(VertexNode<V> toTest) {
        if (toTest.owner != this) {
            throw new PositionException();
        }
    }

    // Checks edge belongs to this graph
    private void checkOwner(EdgeNode<E> toTest) {
        if (toTest.owner != this) {
            throw new PositionException();
        }
    }

    // Converts the vertex back to a VertexNode to use internally
    private VertexNode<V> convert(Vertex<V> v) throws PositionException {
        try {
            VertexNode<V> toCv = (VertexNode<V>) v;
            this.checkOwner(toCv);
            return toCv;
        } catch (ClassCastException ex) {
            throw new PositionException();
        }
    }

    // Converts and edge back to a EdgeNode to use internally
    private EdgeNode<E> convert(Edge<E> e) throws PositionException {
        try {
            EdgeNode<E> toCv = (EdgeNode<E>) e;
            this.checkOwner(toCv);
            return toCv;
        } catch (ClassCastException ex) {
            throw new PositionException();
        }
    }

    @Override
    public Vertex<V> insert(V v) {
        VertexNode<V> toAdd = new VertexNode(v);
        toAdd.owner = this;
        this.vertices.add(toAdd);
        return toAdd;
    }

    @Override
    public Edge<E> insert(Vertex<V> from, Vertex<V> to, E e)
            throws PositionException, InsertionException {
        this.checkOwner(this.convert(from));
        this.checkOwner(this.convert(to));

        VertexNode<V> start = this.convert(from);
        VertexNode<V> end = this.convert(to);
        EdgeNode<E> toAdd = new EdgeNode(start, end, e);

        for (Edge<E> q : this.edges) {
            EdgeNode<E> w = this.convert(q);
            if (w.from == from && w.to == to) {
                throw new InsertionException();
            }
        }

        this.convert(from).outgoing.add(toAdd);
        this.convert(to).incoming.add(toAdd);
        this.edges.add(toAdd);
        toAdd.owner = this;
        return toAdd;
    }

    @Override
    public V remove(Vertex<V> v) throws PositionException,
            RemovalException {
        this.checkOwner(this.convert(v));

        Iterable<Edge<E>> in = this.incoming(v);
        Iterable<Edge<E>> out = this.outgoing(v);

        if (in.iterator().hasNext() || out.iterator().hasNext()) {
            throw new RemovalException();
        }

        this.convert(v).owner = null;
        V val = v.get();
        this.vertices.remove(v);
        return val;
    }

    @Override
    public E remove(Edge<E> e) throws PositionException {
        this.checkOwner(this.convert(e));

        E val = this.convert(e).get();
        VertexNode<V> start = this.convert(this.from(e));
        VertexNode<V> end = this.convert(this.to(e));
        start.outgoing.remove(e);
        end.incoming.remove(e);
        this.edges.remove(e);
        this.convert(e).owner = null;

        return val;
    }

    @Override
    public Iterable<Vertex<V>> vertices() {
        return this.vertices;
    }

    @Override
    public Iterable<Edge<E>> edges() {
        return this.edges;
    }

    @Override
    public Iterable<Edge<E>> outgoing(Vertex<V> v) throws PositionException {
        this.checkOwner(this.convert(v));
        return this.convert(v).outgoing;
    }

    @Override
    public Iterable<Edge<E>> incoming(Vertex<V> v) throws PositionException {
        this.checkOwner(this.convert(v));
        return this.convert(v).incoming;
    }

    @Override
    public Vertex<V> from(Edge<E> e) throws PositionException {
        this.checkOwner(this.convert(e));
        return this.convert(e).from;
    }

    @Override
    public Vertex<V> to(Edge<E> e) throws PositionException {
        this.checkOwner(this.convert(e));
        return this.convert(e).to;
    }

    @Override
    public void label(Vertex<V> v, Object l) throws PositionException {
        this.checkOwner(this.convert(v));
        this.convert(v).put((V) l);
    }

    @Override
    public void label(Edge<E> e, Object l) throws PositionException {
        this.checkOwner(this.convert(e));
        this.convert(e).put((E) l);
    }

    @Override
    public Object label(Vertex<V> v) throws PositionException {
        this.checkOwner(this.convert(v));
        return this.convert(v).get();
    }

    @Override
    public Object label(Edge<E> e) throws PositionException {
        this.checkOwner(this.convert(e));
        return this.convert(e).get();
    }

    @Override
    public void clearLabels() {
        for (Edge<E> e : this.edges) {
            e.put(null);
        }
        for (Vertex<V> v : this.vertices) {
            v.put(null);
        }

    }


    public void setDistance(double dist, Vertex<V> v) throws PositionException {
        this.checkOwner(this.convert(v));
        this.convert(v).set(dist);
    }

    /**
     * Gets data held by EdgeNode.
     * @param v vertex.
     * @throws PositionException when vertex is not on this graph.
     * @return length of edge.
     */
    public double getDistance(Vertex<V> v) throws PositionException {
        this.checkOwner(this.convert(v));
        return this.convert(v).ret();
    }

    private String vertexString(Vertex<V> v) {
        return "\"" + v.get() + "\"";
    }

    private String verticesToString() {
        StringBuilder sb = new StringBuilder();
        for (Vertex<V> v : this.vertices) {
            sb.append("  ").append(vertexString(v)).append("\n");
        }
        return sb.toString();
    }

    private String edgeString(Edge<E> e) {
        return String.format("%s -> %s [label=\"%s\"]",
                this.vertexString(this.from(e)),
                this.vertexString(this.to(e)),
                e.get());
    }

    private String edgesToString() {
        String edgs = "";
        for (Edge<E> e : this.edges) {
            edgs += "    " + this.edgeString(e) + ";\n";
        }
        return edgs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph {\n")
                .append(this.verticesToString())
                .append(this.edgesToString())
                .append("}");
        return sb.toString();
    }
}
