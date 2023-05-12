/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.compress;

import com.gdblab.database.Database;
import com.gdblab.schema.Edge;
import com.gdblab.schema.Graph;
import com.gdblab.schema.GraphObject;
import com.gdblab.schema.Node;
import com.gdblab.schema.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 *
 * @author ramhg
 */
public class PathCompression {
    
    public static ArrayList<Path>  pathSetCompress (ArrayList<Path> s, Path path, Graph g){
        if (s.isEmpty())
            return s;
        
        for (Path p : s){
            if(p.lenght()< path.lenght()){
                
            }
                
        }
    
        return s;
    }
    
    public static void Test (){
        Graph graph = new Graph("Path Test");
        Node node1 = new Node("n1", "Node");
        Node node2 = new Node("n2", "Node");
        Node node3 = new Node("n3", "Node");
        Node node4 = new Node("n4", "Node");
        
        Edge edge1 = new Edge("e1", "a", node1, node2);
        Edge edge2 = new Edge("e2", "a", node2, node3);
        Edge edge3 = new Edge("e3", "b", node3, node4);
        Edge edge4 = new Edge("e4", "c", node4, node1);
        Edge edge5 = new Edge("e5", "a", node1, node3);
        
        graph.insertNode("n1", node1);
        graph.insertNode("n2", node2);
        graph.insertNode("n3", node3);
        graph.insertNode("n4", node4);
        
        graph.insertEdge("e1", edge1);
        graph.insertEdge("e2", edge2);
        graph.insertEdge("e3", edge3);
        graph.insertEdge("e4", edge4);
        graph.insertEdge("e5", edge5);
        
        
        Path path1 = new Path("p1", "path1");
        path1.insertEdge(edge1);
        
        Path path2 = new Path("p2", "path2");
        path2.insertEdge(edge1);
        path2.insertEdge(edge2);
        path2.insertEdge(edge3);
        
        Path path3 = new Path("p3", "path3");
        path3.insertEdge(edge3);
        
        Path path4 = new Path("p4", "path4");
        path4.insertEdge(edge4);
        
        Path path5 = new Path("p5", "path4");
        path5.insertEdge(edge5);
       
        
        Path path6 = new Path("p6", "path5");
        path6.insertEdge(edge1);
        path6.insertEdge(edge2);
        path6.insertEdge(edge3);
        path6.insertEdge(edge4);
         path6.insertEdge(edge5);
        
        graph.insertCPath("p1",compress_P_based_on_the_paths_in_S(graph.getCPaths(), path1, graph));
        graph.insertCPath("p2",compress_P_based_on_the_paths_in_S(graph.getCPaths(), path2, graph));
        graph.insertCPath("p3",compress_P_based_on_the_paths_in_S(graph.getCPaths(), path3, graph));
        graph.insertCPath("p4",compress_P_based_on_the_paths_in_S(graph.getCPaths(), path4, graph));
        graph.insertCPath("p5",compress_P_based_on_the_paths_in_S(graph.getCPaths(), path5, graph));
        graph.insertCPath("p6",compress_P_based_on_the_paths_in_S(graph.getCPaths(), path6, graph));
        
        
        
        
        Database.printPath(graph.getCPaths());
        
        
        System.out.println("\nAplicar recompresión a p2 y p6");
        graph.setCPath("p2",  compress_P_based_on_the_paths_in_S(graph.getCPaths(), graph.getCPath("p2"), graph));
        graph.setCPath("p6",  compress_P_based_on_the_paths_in_S(graph.getCPaths(), graph.getCPath("p6"), graph));
        
        Database.printPath(graph.getCPaths());
        
    }
    
    public static Path compress_P_based_on_the_paths_in_S (ArrayList<Path> s, Path path, Graph g){
        if(!s.isEmpty()){
            final int  pathSize = size(path);
            ArrayList<Path> s_sorted_filtered = (ArrayList<Path>) s.stream().filter(p -> size(p) < pathSize).collect(Collectors.toList());
            Collections.sort(s_sorted_filtered, (p1, p2) -> p1.compareTo(p2));
            int i = 0;
            while (i < s_sorted_filtered.size() && size(path) > size(s_sorted_filtered.get(i)) ){
                Path pi = s_sorted_filtered.get(i);
                path = pathCompress(pi, path, g);
                i++;
            }
        }
       
        return path;
      
    }
    
    public static Path compress_S_based_on_the_path_P (ArrayList<Path> s, Path path, Graph g){
        if(!s.isEmpty()){
            final int  pathSize = size(path);
            ArrayList<Path> s_sorted_filtered = (ArrayList<Path>) s.stream().filter(p -> size(p) < pathSize).collect(Collectors.toList());
            Collections.sort(s_sorted_filtered, (p1, p2) -> p1.compareTo(p2));
            int i = 0;
            while (i < s_sorted_filtered.size() && size(path) > size(s_sorted_filtered.get(i)) ){
                Path pi = s_sorted_filtered.get(i);
                path = pathCompress(pi, path, g);
                i++;
            }
        }
       
        return path;
      
    }
    
    
    
    
    
    public static Path pathCompress (Path pi, Path p , Graph g){
        int pSize = size(p);
        for (int i = 0 ; i< pSize ;i=i+2){
            int j = size(pi) + i -1;
            Path sp = slicePath(p,i, j,g);
            if (pi.getSequence().equals(sp.getSequence())){
                ArrayList<GraphObject> compPathSequence = new ArrayList<>();
                if(i == 0){
                    ArrayList<GraphObject> subSeqR = (ArrayList<GraphObject>) slicePath(p,j, pSize-1,g).getSequence().clone();
                    compPathSequence.add(pi);
                    compPathSequence.addAll(subSeqR.subList(1, subSeqR.size()));
                }
                else if (i>0 && j < pSize-1){
                    ArrayList<GraphObject> subSeqL = (ArrayList<GraphObject>) slicePath(p,0, i-1,g).getSequence().clone();
                    ArrayList<GraphObject> subSeqR = (ArrayList<GraphObject>) slicePath(p,j+1, pSize-1,g).getSequence().clone();
                    compPathSequence.addAll(subSeqL);
                    compPathSequence.add(pi);
                    compPathSequence.addAll(subSeqR);
                }
                else{
                    int size = j - size(pi)  ;
                    ArrayList<GraphObject> subSeqL = (ArrayList<GraphObject>) slicePath(p,0, size,g).getSequence().clone();
                    compPathSequence.addAll(subSeqL);
                    compPathSequence.add(pi);
                }
                    p.setSequence(compPathSequence);
                    pSize = size(p);
                    return p;
            }
            
        }    
        return p;
    }
    
    public static int size(Path p){
        return p.getSequence().size();
    }
    
    public static Path pathReconstruct (Path p, Graph g){
        ArrayList<GraphObject> seq = (ArrayList<GraphObject>) p.getSequence().clone();
        Boolean isReconstructed = false;
        int i =0;
        while (!isReconstructed){
            GraphObject go = seq.get(i);
            if(go.getId().contains("p")){
                ArrayList<GraphObject> seq2 = g.getCPath(go.getId()).getSequence();
                seq = pathSeqUnion(seq,seq2,i);
            }
            else{
                i++;
                if(seq.size() == i)
                    isReconstructed = true;
            }
        }
        p.setSequence(seq);
        return p;
    }
    
    public static ArrayList<GraphObject> pathSeqUnion (ArrayList<GraphObject> seq, ArrayList<GraphObject> seq2, int i){
        if(i==0){
            ArrayList<GraphObject> subSeq = (ArrayList<GraphObject>) seq.subList(i, seq.size()-1);
            seq2.addAll(subSeq);
            return seq2;
        }
        else if (i>0 && i < seq.size()-1){
            ArrayList<GraphObject> subSeqL = (ArrayList<GraphObject>) seq.subList(0, i-1);
            ArrayList<GraphObject> subSeqR = (ArrayList<GraphObject>) seq.subList(i+1, seq.size()-1);
            subSeqL.addAll(seq2);
            seq2.addAll(subSeqR);
            return seq2;
        }
        else{
            ArrayList<GraphObject> subSeqL = (ArrayList<GraphObject>) seq.subList(0, i-1);
            subSeqL.addAll(seq2);
            return subSeqL;
        }
    }
    
             
    public static Path slicePath (Path p, int source, int target, Graph g){
        if (p.getSequence().size()>target && source >=0){
                Node first;
                Node last;
                Path new_path = new Path("p"+UUID.randomUUID().toString(), "path");
                List<GraphObject> seq =  p.getSequence().subList(source, target+1);
               
                new_path.setSequence(new ArrayList<>(seq));
                return new_path;
             
        }
        return p;
    }
    
    
}
