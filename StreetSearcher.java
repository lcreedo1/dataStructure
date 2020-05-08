/** Liam Creedon, lcreedo1, lcreedo1@jhu.edu */


package hw9;

import exceptions.InsertionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


/**
 * Search for the shortest path between two endpoints using
 * Djikstra's. We use a HashMap to store all the vertices so we can
 * find them by name (i.e. their coordinates) when inserting for a
 * fast duplicates check.
 *
 * Vertex data is the coordinates, stored as a String.
 * Vertex label is the Edge into it on the path found.
 * Edge data is the road name, stored as a String.
 * Edge label is the road length, stored as a Double.
 *
 */
public final class StreetSearcher {

    /**
     * useful for marking distance to nodes, or use Double.POSITIVE_INFINITY.
     */
    private static final double MAX_DISTANCE = 1e18;

    /**
     * Creating a comparable class here because we are implementing the use
     * of a priority queue.
     */
    private static final class CMP implements
            Comparable<CMP> {

        /**
         * Double to represent the distance of the vertex.
         */
        private double dist;
        /**
         * Vertex of type string.
         */
        private Vertex<String> v;

        /**
         * Used to compare the distances of paths.
         * @param v vertex of type string to be compared
         * @param distance the double to be compared between vertices
         */
        private CMP(double distance, Vertex<String> v) {
            this.dist = distance;
            this.v = v;
        }

        @Override
        public int compareTo(CMP o) {

            if (o.dist == this.dist) {
                return 0;
            } else if (o.dist < this.dist) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    // Global variables, bleh
    private static Map<String, Vertex<String>> vertices = new HashMap<>();

    private static SparseGraph<String, String> graph = new SparseGraph<>();

    // Silencing checkstyle
    private StreetSearcher() {}

    // Get the path by tracing labels back from end to start.
    private static List<Edge<String>> getPath(Vertex<String> end,
                                              Vertex<String> start) {
        if (graph.label(end) != null) {
            List<Edge<String>> path = new ArrayList<>();

            Vertex<String> cur = end;
            Edge<String> road;
            while (cur != start) {
                road = (Edge<String>) graph.label(cur);  // unchecked cast ok
                path.add(road);
                cur = graph.from(road);
            }
            return path;
        }
        return null;
    }

    // Print the path found.
    private static void printPath(List<Edge<String>> path,
                                  double totalDistance) {
        if (path == null) {
            System.out.println("No path found");
            return;
        }

        System.out.println("Total Distance: " + totalDistance);
        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.println(path.get(i).get() + " "
                               + graph.label(path.get(i)));
        }
    }


    // Djikstra's Algorithm to find shortest path.
    private static void findShortestPath(String startName, String endName) {
        Vertex<String> start = vertices.get(startName);
        Vertex<String> end = vertices.get(endName);

        Queue<CMP> cmpQ = new PriorityQueue<>();
        for (Vertex<String> v : graph.vertices()) {
            //adds distances to every vertex
            graph.setDistance(MAX_DISTANCE, v);
        }

        CMP q = new CMP(0, start);

        //queue to hold locations
        cmpQ.add(q);

        // run through all verticies
        while (!cmpQ.isEmpty()) {
            CMP curr = cmpQ.poll();
            Vertex<String> v = curr.v;
            if (graph.getDistance(v) > curr.dist) {
                graph.setDistance(curr.dist, v);
                if (v == end) {
                    break;
                }
                for (Edge<String> e : graph.outgoing(v)) {
                    Vertex<String> tempV = graph.to(e);
                    double d = graph.getDistance(v)
                            + (double) graph.label(e);

                    if (d < graph.getDistance(tempV)) {
                        graph.label(tempV, e);

                        CMP toAdd = new CMP(d, tempV);
                        cmpQ.add(toAdd);
                    }
                }

            }
        }

        double totalDist = graph.getDistance(end);
        // These method calls will create and print the path for you
        List<Edge<String>> path = getPath(end, start);
        printPath(path, totalDist);
    }


    // Add an endpoint to the network if it is a new endpoint
    private static Vertex<String> addLocation(String name) {
        if (!vertices.containsKey(name)) {
            Vertex<String> v = graph.insert(name);
            vertices.put(name, v);
            return v;
        }
        return vertices.get(name);
    }


    // Load network from fileName, returns number of roads
    private static int loadNetwork(String fileName)
            throws FileNotFoundException {

        int numRoads = 0;

        // Read in from file fileName
        Scanner input = new Scanner(new FileInputStream(new File(fileName)));
        while (input.hasNext()) {

            // Parse the line in to <end1> <end2> <road-distance> <road-name>
            String[] tokens = input.nextLine().split(" ");
            String fromName = tokens[0];
            String toName = tokens[1];
            double roadDistance = Double.parseDouble(tokens[2]);
            String roadName = tokens[3];

            // Get the from and to endpoints, adding if necessary
            Vertex<String> from = addLocation(fromName);
            Vertex<String> to =  addLocation(toName);

            // Add the road to the network - We assume all roads are two-way and
            // ignore if we've already added the road as a reverse of another
            try {

                Edge<String> road = graph.insert(from, to, roadName);
                Edge<String> backwardsRoad = graph.insert(to, from, roadName);
                numRoads += 2;

                // Label each road with it's weight
                graph.label(road, roadDistance);
                graph.label(backwardsRoad, roadDistance);

            } catch (InsertionException ignored) {
                // Nothing to do.
            }
        }

        return numRoads;
    }

    private static void checkValidEndpoint(String endpointName) {
        if (!vertices.containsKey(endpointName)) {
            throw new IllegalArgumentException(endpointName);
        }
    }

    /**
     * Main method.
     * @param args See usage.
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: " +
                    "StreetSearcher <map_name> <start_coords> <end_coords>");
            return;
        }

        String fileName  = args[0];
        String startName = args[1];
        String endName   = args[2];

        try {

            int numRoads = loadNetwork(fileName);
            System.out.println("Network Loaded!");
            System.out.println("Loaded " + numRoads + " roads");
            System.out.println("Loaded " + vertices.size() + " endpoints");

            checkValidEndpoint(startName);
            checkValidEndpoint(endName);

        } catch (FileNotFoundException e) {
            System.err.println("Could not find file " + fileName);
            return;
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid Endpoint: " + e.getMessage());
            return;
        }

        findShortestPath(startName, endName);
    }
}
