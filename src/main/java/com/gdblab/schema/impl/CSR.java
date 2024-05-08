package com.gdblab.schema.impl;

import java.util.*;

import com.gdblab.schema.*;

public class CSR implements Graph {

    private final ArrayList<Node> nodes;
    private final ArrayList<Integer> offsets;
    private final ArrayList<Edge> edges;

    public CSR() {
        this.nodes = new ArrayList<>();
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
        ArrayList<Node> neighbours = new ArrayList<>();

        int index = nodes.indexOf(getNode(id));

        if ( index == -1 ) {
            return neighbours;
        }

        int start = offsets.get(index);
        int end = (index + 1 < offsets.size()) ? offsets.get(index + 1) : edges.size();

        for (int i = start; i < end; i++) {
            neighbours.add(edges.get(i).getTarget());
        }

        return neighbours;
    }

    @Override
    public Collection<Node> getNeighbours(String id, String label) {
        ArrayList<Node> neighbours = new ArrayList<>();
        
        int index = nodes.indexOf(getNode(id));

        if ( index == -1 ) {
            return neighbours;
        }

        int start = offsets.get(index);
        int end = (index + 1 < offsets.size()) ? offsets.get(index + 1) : edges.size();

        for (int i = start; i < end; i++) {
            if (edges.get(i).getLabel().equals(label)) {
                neighbours.add(edges.get(i).getTarget());
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
        offsets.add(edges.size());

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
         * Get the index of the source node in the nodes list.
         */
        int index = nodes.indexOf(edge.getSource());

        /*
         * Get the offset of the source node in the offsets list.
         */
        int offset = offsets.get(index);

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
        for (int i = index + 1; i < offsets.size(); i++) {
            offsets.set(i, offsets.get(i) + 1);
        }

        return edge;

    }
    
}
