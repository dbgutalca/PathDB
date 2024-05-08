package com.gdblab.schema;

import java.util.Collection;
import java.util.Iterator;

public interface Graph {
    /**
     * Gets a node object in the graph with the given id. null if not present
     */
    public Node getNode(String id);

    /**
     * Gets an iterator that iterates through all the nodes in the graph
     */
    public Iterator<Node> getNodeIterator();

    /**
     * Gets an iterator that iterates through all the edges in the graph
     */
    public Iterator<Edge> getEdgeIterator();

    /**
     * Gets an iterator that iterates through all the edges in the graph that have a given label
     */
    public Iterator<Edge> getEdgesByLabel(String label);

    /**
     * Gets all the neighbours of the node with the given id. Considers edges with any label
     */
    public Collection<Node> getNeighbours(String id);

    /**
     * Gets the neighbours of the node with the given id, if they are reachable through edges
     * with the given label
     */
    public Collection<Node> getNeighbours(String id, String label);

    /**
     * Insert a node object in the graph and return in. If a node with the same id already
     * exists return null
     */
    public Node insertNode(Node node);

    /**
     * Insert an edge object in the graph and return it. If an edge with the same id already
     * exists return null
     */
    public Edge insertEdge(Edge edge);

}
