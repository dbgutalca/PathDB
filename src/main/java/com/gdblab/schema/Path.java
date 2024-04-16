/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.schema;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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
        else if (Objects.equals(this.Last().getId(), edge.getSource().getId())){
            sequence.add(edge);
            sequence.add(edge.getTarget());
        }
    }
    
    public void insertNode(Node node){
        if(sequence.isEmpty())
            sequence.add(node);
    }

    
    public ArrayList<Node> getNodeSequence() {
        ArrayList<Node> nodes = new ArrayList<>();
         for (int i = 0; i < sequence.size(); i++) {
             if(sequence.get(i) instanceof Node node)
                 nodes.add(node);
        }
        return nodes;
    }

    public String getStringNodeSequence() {
        String nodes = "";
         for (int i = 0; i < sequence.size(); i++) {
             if(sequence.get(i) instanceof Node node)
                 nodes += node.getId() + " ";
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

    public String getStringEdgeSequence() {
        String edges = "";
         for (int i = 0; i < sequence.size(); i++) {
             if(sequence.get(i) instanceof Edge edge)
                 edges += edge.getId() + " ";
        }
        return edges;
    }

    public int getNodeNumber(){
        return getNodeSequence().size();
    }
    
    
    public Node First (){
        return this.getNodeSequence().get(0);
    }
    public Node Last (){
        return this.getNodeSequence().get(this.getNodeSequence().size()-1);
    }
    
    
     public Node GetNodeX (int pos){
        ArrayList<Node> seq = this.getNodeSequence();
        if(seq.size()>= pos)
                return seq.get(pos);
        return null;
    }
    
    public Edge GetEdgeX (int pos){
      ArrayList<Edge> seq = this.getEdgeSequence();
        if(seq.size()>= pos)
                return seq.get(pos);
        return null;
    }
    
    public Path SubPath(int i, int j){
        Node first = GetNodeX(i);
        Node last = GetNodeX(j);
        ArrayList<Edge> seq = this.getEdgeSequence();
        Path new_path = new Path(UUID.randomUUID().toString(), "path");
        boolean last_reached = false;
        boolean first_reached = false;
        
        if(i==j)
            new_path.insertNode(first);
       
        for (int k = 0; k < seq.size() && !last_reached; k++) {
            if(seq.get(k).getSource().getId().equals(first.getId()) && !first_reached)
                first_reached=true;
            if (seq.get(k).getTarget().getId().equals(last.getId()))
                last_reached = true;
           
            if(first_reached)
                new_path.insertEdge(seq.get(k));
        }
        return new_path;
    }
    
    public Path LeftSubPath (int i){
        return SubPath(0, i);
    }
    
    public Path RightSubPath (int j){
        return SubPath(j, this.getNodeNumber()-1);
    }
    
    public boolean equals (Path p2){
        ArrayList<GraphObject> sequence1 = this.getSequence();
        ArrayList<GraphObject> sequence2 = p2.getSequence();
        
        if (sequence1.size() != sequence2.size())
            return false;
        
        for (int i = 0; i < sequence1.size(); i++) 
            if(!sequence1.get(i).getId().equals(sequence2.get(i).getId()))
                return false;
        
        return true;
    }
    
    public boolean isNodeLinkable(Path path2){
        return Last().getId().equals(path2.First().getId());
    }
    
    public Edge isEdgeLinkable(Path path2, Graph graph){
        String node1_id = Last().getId();
        String node2_id = path2.First().getId();
        
        return graph.getEdge(node1_id, node2_id);
    }
    /**
     * Check if a path is in a list of paths.
     * 
     * @param p - Path to check.
     * @param list - List of paths.
     * 
     * @return true if the path is in the list, false otherwise.
     * 
     * @see Path
     */
    public boolean isInList(Path p, List<Path> list) {
        Iterator<Path> i = list.iterator();
        while (i.hasNext()) {
            Path path = i.next();
            if (path.equals(p))
                return true;
        }
        return false;
    }
    
}
