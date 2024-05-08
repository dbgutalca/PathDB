package com.gdblab.schema.impl;

import java.util.*;

import com.gdblab.schema.*;

public class AdjacentList implements Graph {

    private final HashMap<String, Node> nodes;

    private final LinkedList<LinkedList<Edge>> adjList;

    public AdjacentList() {
        this.nodes = new HashMap<>();
        this.adjList = new LinkedList<>();
    }

    @Override
    public Node getNode(String id) {
        return this.nodes.get(id);
    }

    @Override
    public Iterator<Node> getNodeIterator() {
        return this.nodes.values().iterator();
    }

    @Override
    public Iterator<Edge> getEdgeIterator() {
        LinkedList<Edge> edges = new LinkedList<>();
        for (LinkedList<Edge> edgeList : adjList) {
            edges.addAll(edgeList);
        }
        return edges.iterator();
    }

    @Override
    public Iterator<Edge> getEdgesByLabel(String label) {
        return new Iterator<Edge>() {
            Edge slot = null;
            Iterator<Edge> edges = getEdgeIterator();

            @Override
            public boolean hasNext() {
                if (slot == null) {
                    while (edges.hasNext()) {
                        final Edge e = edges.next();
                        if (e.getLabel().equals(label)) {
                            slot = e;
                            break;
                        }
                    }
                }
                return slot != null;
            }

            @Override
            public Edge next() {
                final Edge e = slot;
                slot = null;
                return e;
            }
        };
    }

    @Override
    public Collection<Node> getNeighbours(String id) {
        Iterator<Edge> edges = getEdgeIterator();
        HashSet<Node> neighbours = new HashSet<>();
        while (edges.hasNext()) {
            Edge edge = edges.next();
            if (edge.getSource().getId().equals(id)) {
                neighbours.add(edge.getTarget());
            }
        }
        return neighbours;
    }

    @Override
    public Collection<Node> getNeighbours(String id, String label) {
        Iterator<Edge> edges = getEdgeIterator();
        HashSet<Node> neighbours = new HashSet<>();
        while (edges.hasNext()) {
            Edge edge = edges.next();
            if (edge.getLabel().equals(label) && edge.getSource().getId().equals(id)) {
                neighbours.add(edge.getTarget());
            }
        }
        return neighbours;
    }

    @Override
    public Node insertNode(Node node) {
        if (!this.nodes.containsKey(node.getId())) {
            this.nodes.put(node.getId(), node);
            return node;
        }
        return null;
    }

    @Override
    public Edge insertEdge(Edge edge) {
        /*
         * Check if the source and target nodes exist in the graph.
         * If not, return null
         *
         * Note: Maybe if doesn't exist, insert the nodes in the graph and then add the edge.
         */
        if (getNode(edge.getSource().getId()) == null ||
            getNode(edge.getTarget().getId()) == null) {
            return null;
        }

        /*
         * Search for the source node in the adjacency list.
         */
        for (LinkedList<Edge> edgeList : adjList) {
            if (edgeList.getFirst().getSource().getId().equals(edge.getSource().getId())) {
                edgeList.add(edge);
                return edge;
            }
        }

        /*
         * If the source node is not found in the adjacency list, create a new list and add the edge.
         */
        LinkedList<Edge> edgeList = new LinkedList<>();
        edgeList.add(edge);
        adjList.add(edgeList);
        return edge;
    }
    
}
