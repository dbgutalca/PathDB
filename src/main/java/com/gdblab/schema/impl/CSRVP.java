package com.gdblab.schema.impl;

import java.util.*;

import com.gdblab.schema.*;

public class CSRVP implements Graph{

    private final ArrayList<Node> nodes;

    private final ArrayList<String> labels;
    private final ArrayList<Integer> offsets;
    private final ArrayList<Edge> edges;

    public CSRVP() {
        this.nodes = new ArrayList<>();
        this.labels = new ArrayList<>();
        this.offsets = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    @Override
    public Node getNode(String id) {
        for (Node node : nodes) {
            if (node.getId().equals(id)) {
                return node;
            }
        }

        return null;
    }

    @Override
    public Iterator<Node> getNodeIterator() {
        return nodes.iterator();
    }

    @Override
    public Iterator<Edge> getEdgeIterator() {
        return edges.iterator();
    }

    @Override
    public Iterator<Edge> getEdgesByLabel(String label) {
        return new Iterator<>() {

            Edge slot = null;
            Iterator<Edge> edgeIterator = edges.iterator();

            @Override
            public boolean hasNext() {
                if (slot == null) {
                    while (edgeIterator.hasNext()) {
                        final Edge e = edgeIterator.next();
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
            if (edge.getSource().getId().equals(id) && edge.getLabel().equals(label)) {
                neighbours.add(edge.getTarget());
            }
        }
        return neighbours;
    }

    @Override
    public Node insertNode(Node node) {
        if (getNode(node.getId()) != null) {
            return null;
        }

        nodes.add(node);

        return node;
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
         * Check if the label exists in the labels list.
         * If not, add the label to the end of the list and update the offsets list.
         */
        if (!labels.contains(edge.getLabel())) {
            labels.add(edge.getLabel());
            offsets.add(edges.size());
        }

        /*
         * Get the index of the label in the labels list.
         */
        int labelIndex = labels.indexOf(edge.getLabel());

        /*
         * Get the offset of the label in the offsets list.
         */
        int offset = offsets.get(labelIndex);

        /*
         * Add the edge to the edges list.
         * 
         * Note: The add method of the ArrayList class inserts the element at the specified position in the list.
         *      The elements at the specified position and beyond are shifted to the right.
         */
        edges.add(offset, edge);

        /*
         * Update the offsets list.
         */
        for (int i = labelIndex + 1; i < offsets.size(); i++) {
            offsets.set(i, offsets.get(i) + 1);
        }

        return edge;
    }
    
}
