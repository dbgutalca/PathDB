/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.schema.impl;

import com.gdblab.schema.Edge;
import com.gdblab.schema.Graph;
import com.gdblab.schema.Node;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

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
        LinkedList<Edge> temp = new LinkedList<>();
        for (Iterator<LinkedList<Edge>> iterator = edges.values().iterator(); iterator.hasNext();) 
            temp.addAll(iterator.next());
        return temp.iterator();
    }

    @Override
    public Iterator<Edge> getEdgesByLabel(String label) {
        return edges.get(label).iterator();
    }

    @Override
    public Collection<Node> getNeighbours(String id) {
        HashSet<Node> nodesTemp = new HashSet<>();
        for (Iterator<Edge> edgeIt = getEdgeIterator() ; edgeIt.hasNext();){
            Edge edge = edgeIt.next(); 
            if (edge.getTarget().getId().equals(id))
                nodesTemp.add(edge.getTarget());
        }
        return nodesTemp;
    }
    

    @Override
    public Collection<Node> getNeighbours(String id, String label) {
        HashSet<Node> nodesTemp = new HashSet<>();
        for (Iterator<Edge> edgeIt = getEdgeIterator() ; edgeIt.hasNext();){
            Edge edge = edgeIt.next(); 
            if (edge.getSource().getId().equals(id) && edge.getLabel().equals(label))
                nodesTemp.add(edge.getTarget());
        }
        return nodesTemp;    
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
            LinkedList<Edge> edgesByLabel = new LinkedList<>();
            edges.put(edge.getLabel(), edgesByLabel);
            edgesByLabel.add(edge);
            return edge;
        }
        else{
            LinkedList<Edge> edgesByLabel = edges.get(edge.getLabel());
            boolean found = false;
            for (Edge edge1 : edgesByLabel) 
                if (edge1.getId().equals(edge.getId())){
                    found = true;
                    break;
                }
            if(!found){
                edgesByLabel.add(edge);
                return edge;
            }
            else
                return null;
        }
    }
    
}
