package com.gdblab.graph.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.gdblab.graph.interfaces.InterfaceGraph;
import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.Node;

public class CSRVPMin implements InterfaceGraph {

    private static CSRVPMin instance = null;

    private final HashMap<String, Node> nodes;
    private final HashMap<String, LinkedList<Edge>> edges;

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
    public Node getNode(final String id) {
        return nodes.get(id);
    }

    @Override
    public Iterator<Node> getNodeIterator() {
        return nodes.values().iterator();
    }

    @Override
    public Iterator<Edge> getEdgeIterator() {
        return new Iterator<Edge>() {

            Edge slot = null;

            Iterator<LinkedList<Edge>> valuesIt = edges.values().iterator();
            Iterator<Edge> currentIt = null;
            
            @Override
            public boolean hasNext() {
                for(;;){

                    if (currentIt == null) {

                        if (valuesIt.hasNext()) {
                            currentIt = valuesIt.next().iterator();
                        }

                        else {
                            return false;
                        }
                    }
    
                    if (currentIt.hasNext()) {
                        slot = currentIt.next();
                        return true;
                    }
    
                    else currentIt = null;
                }
            }

            @Override
            public Edge next() {
                Edge e = slot;
                slot = null;
                return e;
            }
        };
    }

    @Override
    public Node insertNode(Node node) {
        if (! nodes.containsKey(node.getId())){
            nodes.put(node.getId(), node);
            return node;
        }
        return null;
    }

    @Override
    public Edge insertEdge(Edge edge) {
        if (!edges.containsKey(edge.getLabel())){
            final LinkedList<Edge> edgesByLabel = new LinkedList<>();
            edges.put(edge.getLabel(), edgesByLabel);
            edgesByLabel.add(edge);
            return edge;
        }
        else{
            final LinkedList<Edge> edgesByLabel = edges.get(edge.getLabel());
            edgesByLabel.add(edge);
            return edge;
        }
    }

    @Override
    public Integer getNodesQuantity() {
        return nodes.size();
    }

    @Override
    public Integer getEdgesQuantity() {
        Integer i = 0;

        for (LinkedList<Edge> list : edges.values()) {
            i += list.size();
        }

        return i;
    }

    @Override
    public Integer getDifferetEdgesQuantity() {
        return edges.size();
    }
    
    @Override
    public HashMap<String, Integer> getEdgesByLabelQuantity() {
        HashMap<String, Integer> edgesByLabel = new HashMap<>();

        for (Map.Entry<String, LinkedList<Edge>> entry : edges.entrySet()) {
            String label = entry.getKey();
            LinkedList<Edge> edgesList = entry.getValue();

            edgesByLabel.put(label, edgesList.size());
        }

        return edgesByLabel;
    }

    @Override
    public ArrayList<Edge> getSampleOfEachlabel() {
        ArrayList<Edge> sample = new ArrayList<>();
        
        for (LinkedList<Edge> list : edges.values()) {
            sample.add(list.get((int) (Math.random() * (list.size() - 1))));
        }

        return sample;
    }

    @Override
    public HashSet<Edge> getNeighbours(final String id) {
        final HashSet<Edge> nodesTemp = new HashSet<>();
        for (final Iterator<Edge> edgeIt = getEdgeIterator() ; edgeIt.hasNext();){
            final Edge edge = edgeIt.next(); 
            if (edge.getSource().getId().equals(id))
                nodesTemp.add(edge);
        }
        return nodesTemp;
    }
    
}
