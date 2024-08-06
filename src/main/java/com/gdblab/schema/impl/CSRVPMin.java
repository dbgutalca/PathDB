/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.schema.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import com.gdblab.schema.Edge;
import com.gdblab.schema.Graph;
import com.gdblab.schema.Node;

/**
 *
 * @author Roberto
 */
public class CSRVPMin implements Graph{
    
    private final HashMap<String, Node> nodes;
    private final HashMap<String, LinkedList<Edge>> edges;

    public CSRVPMin() {
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
    public Iterator<Edge> getEdgesByLabel(final String label) {
        return edges.get(label).iterator();
    }

    @Override
    public Collection<Node> getNeighbours(final String id) {
        final HashSet<Node> nodesTemp = new HashSet<>();
        for (final Iterator<Edge> edgeIt = getEdgeIterator() ; edgeIt.hasNext();){
            final Edge edge = edgeIt.next(); 
            if (edge.getTarget().getId().equals(id))
                nodesTemp.add(edge.getTarget());
        }
        return nodesTemp;
    }
    

    @Override
    public Collection<Node> getNeighbours(final String id, final String label) {
        final HashSet<Node> nodesTemp = new HashSet<>();
        for (final Iterator<Edge> edgeIt = getEdgeIterator() ; edgeIt.hasNext();){
            final Edge edge = edgeIt.next(); 
            if (edge.getSource().getId().equals(id) && edge.getLabel().equals(label))
                nodesTemp.add(edge.getTarget());
        }
        return nodesTemp;    
    }

    @Override
    public Node insertNode(final Node node) {
        if (! nodes.containsKey(node.getId())){
            nodes.put(node.getId(), node);
            return node;
        }
        return null;
        
    }

    @Override
    public Edge insertEdge(final Edge edge) {
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
    
}
