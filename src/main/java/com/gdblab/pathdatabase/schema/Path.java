/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.pathdatabase.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author ramhg
 */
public class Path extends GraphObject{
    private ArrayList<GraphObject> sequence;
    

    public Path(String id, String label) {
        super(id, label);
        this.sequence = new ArrayList<>();
    }
    
    public Path(String id) {
        super(id);
        this.sequence = new ArrayList<>();
    }

    public ArrayList<GraphObject> getSequence() {
        return sequence;
    }
    
    public void insertEdge(Edge edge){
        if (sequence.isEmpty()){
            sequence.add(edge.getSource());
            sequence.add(edge);
            sequence.add(edge.getTarget());
        }
        else{
            sequence.add(edge);
            sequence.add(edge.getTarget());
        }
    }
    
     public void insert(GraphObject go){
        sequence.add(go);
    }
    
    
 
    
    public ArrayList<Node> getNodeSequence() {
        ArrayList<Node> nodes = new ArrayList<>();
         for (int i = 0; i < sequence.size(); i++) {
             if(sequence.get(i) instanceof Node node)
                 nodes.add(node);
        }
        return nodes;
    }
    
    public ArrayList<Edge> getEdgeSequence() {
        ArrayList<Edge> edges = new ArrayList<>();
         for (int i = 0; i < sequence.size(); i++) {
             if(sequence.get(i) instanceof Edge edge)
                 edges.add(edge);
        }
        return edges;
    }

    
    
    
    
}
