package com.gdblab.schema.impl;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.gdblab.schema.*;

public class AdjacentListGraph implements Graph {

    private final HashMap<String, Node> nodes;

    private final HashMap<String, LinkedList<Edge>> adjacentList;

    public AdjacentListGraph() {
        this.nodes = new HashMap<>();
        this.adjacentList = new HashMap<>();
    }

    @Override
    public Node getNode(final String id) {
        return this.nodes.get(id);
    }

    @Override
    public Iterator<Node> getNodeIterator() {
        return this.nodes.values().iterator();
    }

    @Override
    public Iterator<Edge> getEdgeIterator() {
        final LinkedList<Edge> edges = new LinkedList<>();
        for (LinkedList<Edge> edgeList : this.adjacentList.values()) {
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
        if (!this.adjacentList.containsKey(id)) {
            return null;
        }

        LinkedList<Node> neighbours = new LinkedList<>();
        LinkedList<Edge> edges = this.adjacentList.get(id);
        for (Edge edge : edges) {
            neighbours.add(edge.getTarget());
        }

        return neighbours;  
    }

    @Override
    public Collection<Node> getNeighbours(String id, String label) {
        if (!this.adjacentList.containsKey(id)) {
            return null;
        }

        LinkedList<Node> neighbours = new LinkedList<>();
        LinkedList<Edge> edges = this.adjacentList.get(id);
        for (Edge edge : edges) {
            if (edge.getLabel().equals(label)) {
                neighbours.add(edge.getTarget());
            }
        }

        return neighbours;
    }

    @Override
    public Node insertNode(final Node node) {
        if (!this.nodes.containsKey(node.getId())) {
            this.nodes.put(node.getId(), node);
            this.adjacentList.put(node.getId(), new LinkedList<>());
            return node;
        }
        return null;
    }

    @Override
    public Edge insertEdge(final Edge edge) {
        if ( !this.adjacentList.containsKey(edge.getSource().getId()) ||
             !this.adjacentList.containsKey(edge.getTarget().getId()) ) {
            return null;
        }

        this.adjacentList.get(edge.getSource().getId()).add(edge);

        return edge;
    }
    
}
