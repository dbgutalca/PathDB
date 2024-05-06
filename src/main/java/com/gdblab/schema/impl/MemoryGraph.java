/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.schema.impl;

import com.gdblab.schema.*;

import java.util.*;

/**
 *
 * @author ramhg
 */
public class MemoryGraph implements Graph {

    private final HashMap<String, Node> nodes;
    private final HashMap<String, Edge> edges;

    public MemoryGraph() {
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
    }

    @Override
    public Node getNode(String id) {
        return nodes.get(id);
    }

    @Override
    public Iterator<Node> getNodeIterator() {
        return nodes.values().iterator();
    }

    @Override
    public Iterator<Edge> getEdgeIterator() {
        return edges.values().iterator();
    }

    @Override
    public Iterator<Edge> getEdgesByLabel(String label) {
        return new Iterator<>() {

            Edge slot = null;
            Iterator<Edge> edgez = edges.values().iterator();

            @Override
            public boolean hasNext() {
                if (slot == null) {
                    while (edgez.hasNext()) {
                        final Edge e = edgez.next();
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
        final List<Node> neighbours = new ArrayList<>();
        for (Edge e : edges.values()) {
            if (e.getSource().getId().equals(id))
                neighbours.add(e.getTarget());
        }
        return neighbours;
    }

    @Override
    public Collection<Node> getNeighbours(String id, String label) {
        final List<Node> neighbours = new ArrayList<>();
        for (Edge e : edges.values()) {
            if (e.getLabel().equals(label) && e.getSource().getId().equals(id))
                neighbours.add(e.getTarget());
        }
        return neighbours;
    }

    public void setNode(String id, Node node) {
        nodes.remove(id);
        insertNode(id, node);
    }
    
    public void insertNode(String id, Node node) {
        nodes.put(id,node);
    }
    
    public Edge getEdge(String id) {
        return edges.get(id);
    }
    
     public Edge getEdge(String source_id, String target_id) {
        for (Edge go : edges.values()){
            if (go.getSource().getId().equals(source_id) && go.getTarget().getId().equals(target_id))
                return go;
        }
         
        return null;
    }

    public void setEdge(String id, Edge edge) {
        edges.remove(id);
        insertEdge(id, edge);
    }
    
    public void insertEdge(String id, Edge edge) {
        edges.put(id,edge);
    }

    public HashMap<String,Node> getNodes(){
        return nodes;
    }

    public HashMap<String,Edge> getEdges(){
        return edges;
    }
    
}
