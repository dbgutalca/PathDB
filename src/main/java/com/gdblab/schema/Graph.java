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
}
