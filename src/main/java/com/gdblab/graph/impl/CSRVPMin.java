package com.gdblab.graph.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.gdblab.graph.interfaces.InterfaceGraph;

public class CSRVPMin implements InterfaceGraph {

    private static CSRVPMin instance = null;

    private final HashMap<String, String> nodes; // Formar: <NodeID, NodeObjectString>
    private final HashMap<String, LinkedList<String>> edges; // Formar: <EdgeLabel, List of EdgeObjectStrings>

    public static InterfaceGraph getInstance() {
        if (instance == null) {
            instance = new CSRVPMin();
        }
        return instance;
    }

    private CSRVPMin() {
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
    }

    @Override
    public String getNode(String id) {
        return this.nodes.get(id);
    }

    @Override
    public String getEdge(String id) {
        for (LinkedList<String> edgeList : this.edges.values()) {
            for (String edge : edgeList) {
                if (edge.split("\\|")[0].equals(id)) {
                    System.out.println("Edge found: " + edge);
                    return edge;
                }
            }
        }
        return null;
    }

    @Override
    public Iterator<String> getNodeIterator() {
        return this.nodes.keySet().iterator();
    }

    @Override
    public Iterator<String> getEdgeIteratorByLabel(String label) {
        LinkedList<String> labeledEdges = this.edges.get(label);
        if (labeledEdges == null)
            return new LinkedList<String>().iterator();

        return new Iterator<String>() {
            private final Iterator<String> it = labeledEdges.iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public String next() {
                String[] edge = it.next().split("\\|");

                return edge[3].split(":")[1] + "|" +
                        edge[0].split(":")[1] + "|" +
                        edge[4].split(":")[1] + "|";
            }
        };
    }

    @Override
    public Iterator<String> getEdgeIteratorByNegatedLabel(String label) {
        LinkedList<String> negatedEdges = new LinkedList<>();
        for (String key : this.edges.keySet()) {
            if (!key.equals(label)) {
                negatedEdges.addAll(this.edges.get(key));
            }
        }
        if (negatedEdges.isEmpty())
            return new LinkedList<String>().iterator();

        return new Iterator<String>() {
            private final Iterator<String> it = negatedEdges.iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public String next() {
                String[] edge = it.next().split("\\|");
                return edge[3].split(":")[1] + "|" +
                        edge[0].split(":")[1] + "|" +
                        edge[4].split(":")[1] + "|";
            }
        };
    }

    @Override
    public void insertNode(String id, String nodeObject) {
        this.nodes.put(id, nodeObject);
    }

    @Override
    public void insertEdge(String label, String edgeObject) {
        if (!this.edges.containsKey(label)) {
            this.edges.put(label, new LinkedList<>());
        }
        this.edges.get(label).add(edgeObject);
    }

    @Override
    public void cleanNodes() {
        this.nodes.clear();
    }

}
