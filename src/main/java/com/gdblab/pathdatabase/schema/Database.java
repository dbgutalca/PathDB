/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.pathdatabase.schema;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author ramhg
 */
public class Database {
    private Graph graph;

    public Database() {
        graph = new Graph("test1");
        GenerateDemoDatabase(this.graph);
        System.out.println(graph.getName());
        ArrayList<Path> paths = EdgeJoin(graph.getPaths(), graph.getPaths());
        System.out.println("");
        
    }
    
    public Node First (Path p){
        return p.getNodeSequence().get(0);
    }
    public Node Last (Path p){
        return p.getNodeSequence().get(p.getNodeSequence().size()-1);
    }
    
    public Node GetNodeX (Path p, int pos){
        ArrayList<GraphObject> sequence = p.getSequence();
        if(sequence.size()>= pos)
            if (sequence.get(pos) instanceof Node node)
                return node;
        return null;
    }
    
    public Edge GetEdgeX (Path p, int pos){
        ArrayList<GraphObject> sequence = p.getSequence();
        if(sequence.size()>= pos)
            if (sequence.get(pos) instanceof Edge edge)
                return edge;
        return null;
    }
    
    public Path SubPath(Path p, int i, int j){
        Node first = GetNodeX(p, i);
        Node last = GetNodeX(p, j);
        ArrayList<GraphObject> sequence = p .getSequence();
        Path new_path = new Path(UUID.randomUUID().toString(), "path");
        boolean last_reached = false;
        boolean first_reached = true;
       
        for (int k = 0; k < sequence.size() && !last_reached; k++) {
            if (sequence.get(k) instanceof Node node){
                if(node.getId().equals(first.getId()))
                    first_reached=true;
                if (node.getId().equals(last.getId()))
                    last_reached = true;
            }

            if(first_reached)
                new_path.insert(sequence.get(k));
        }
        return new_path;
    }
    
    public Path LeftSubPath (Path p, int i){
        return SubPath(p, 0, i);
    }
    
    public Path RightSubPath (Path p, int j){
        return SubPath(p, j, p.getSequence().size());
    }
    
    public String Label (GraphObject go){
        return go.getLabel();
    }
    
    public boolean equalPath (Path p1, Path p2){
        ArrayList<GraphObject> sequence1 = p1.getSequence();
        ArrayList<GraphObject> sequence2 = p2.getSequence();
        
        if (sequence1.size() != sequence2.size())
            return false;
        
        for (int i = 0; i < sequence1.size(); i++) 
            if(!sequence1.get(i).getId().equals(sequence2.get(i).getId()))
                return false;
        
        return true;
    }
    
    public boolean isNodeLinkable(Path path1,Path path2){
        return Last(path1).getId().equals(First(path2).getId());
    }
    
    public Edge isEdgeLinkable(Path path1,Path path2){
        String node1_id = Last(path1).getId();
        String node2_id = First(path2).getId();
        
        return graph.getEdge(node1_id, node2_id);
    }
    
    public ArrayList<Path> NodeJoin (ArrayList<Path> pathsA, ArrayList<Path> pathsB){
        ArrayList<Path> join_path = new ArrayList<>();
        for (Path path1 : pathsA) {
            for (Path path2 : pathsB) {
                if(isNodeLinkable(path1, path2)){
                    Path p = new Path(UUID.randomUUID().toString());
                    for (GraphObject go : path1.getSequence())
                        p.insert(go);
                    for (int i = 1; i < path2.getSequence().size(); i++)
                        p.insert(path2.getSequence().get(i));
                    join_path.add(p);
                }
            } 
        }
        return join_path;
    }
    
    public ArrayList<Path> EdgeJoin (ArrayList<Path> pathsA, ArrayList<Path> pathsB){
        ArrayList<Path> join_path = new ArrayList<>();
        for (Path path1 : pathsA) {
            for (Path path2 : pathsB) {
                Edge edge = isEdgeLinkable(path1, path2);
                if(edge != null){
                    Path p = new Path(UUID.randomUUID().toString());
                    for (GraphObject go : path1.getSequence())
                        p.insert(go);
                    p.insert(edge);
                    for (GraphObject go : path2.getSequence())
                        p.insert(go);
                    join_path.add(p);
                }
            } 
        }
        return join_path;
    }
    
    public ArrayList<Path> Union (ArrayList<Path> pathsA, ArrayList<Path> pathsB){
        ArrayList<Path> union_path = new ArrayList<>();
        union_path.addAll(pathsA);
        for (Path path2 : pathsB) {
            boolean equal = false;
             for (Path path1 : pathsA) {
                 if(equalPath(path1, path2)){
                     equal = true;
                     break;
                 }
            }
            if(!equal)
                union_path.add(path2);
        }
        return union_path;
    }
    public ArrayList<Path> Intersection (ArrayList<Path> pathsA, ArrayList<Path> pathsB){
        ArrayList<Path> intersection_path = new ArrayList<>();
        for (Path path1 : pathsA) {
            for (Path path2 : pathsB) {
                if(equalPath(path1, path2)){
                    intersection_path.add(path1);
                }
            } 
        }
        return intersection_path;
    }
    
    public ArrayList<Path> Difference (ArrayList<Path> pathsA, ArrayList<Path> pathsB){
        ArrayList<Path> difference_path = new ArrayList<>();
        for (Path path1 : pathsA) {
            boolean equal = false;
            for (Path path2 : pathsB) {
                if(equalPath(path1, path2)){
                    equal = true;
                    break;
                }
            } 
            if (!equal)
                difference_path.add(path1);
        }
        return difference_path;
    }
    
    
    
    
    private void GenerateDemoDatabase(Graph graph){
        Node node1 = new Node("n1", "Node");
        Node node2 = new Node("n2", "Node");
        Node node3 = new Node("n3", "Node");
        Node node4 = new Node("n4", "Node");
        
        Edge edge1 = new Edge("e1", "a", node1, node2);
        Edge edge2 = new Edge("e2", "a", node2, node3);
        Edge edge3 = new Edge("e3", "b", node3, node4);
        Edge edge4 = new Edge("e4", "c", node1, node4);
        
        graph.insertNode("n1", node1);
        graph.insertNode("n2", node2);
        graph.insertNode("n3", node3);
        graph.insertNode("n4", node4);
        
        graph.insertEdge("e1", edge1);
        graph.insertEdge("e2", edge2);
        graph.insertEdge("e3", edge3);
        graph.insertEdge("e4", edge4);
        
        
        Path path1 = new Path("p1", "path1");
        path1.insertEdge(edge1);
        
        Path path2 = new Path("p2", "path2");
        path2.insertEdge(edge2);
        
        Path path3 = new Path("p3", "path3");
        path3.insertEdge(edge3);
        
        Path path4 = new Path("p4", "path4");
        path4.insertEdge(edge4);
        
        graph.insertPath("p1", path1);
        graph.insertPath("p2", path2);
        graph.insertPath("p3", path3);
        graph.insertPath("p4", path4);
        
    }
   
   
   
   
    
}
