/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.algebra;

import com.gdblab.schema.Edge;
import com.gdblab.schema.impl.MemoryGraph;
import com.gdblab.schema.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.UUID;
import com.gdblab.main.Main;

/**
 *
 * @author ramhg
 */
public class PathAlgebra {

    MemoryGraph graph;
    public PathAlgebra(MemoryGraph graph) {
        this.graph = graph;
    }
    
    public static Path NodeLink (Path pathA, Path pathB){
        if(pathA.isNodeLinkable(pathB)){
            Path join_path = null; 

            if (Main.semantic.equals("Simple Path")) {
                ArrayList<String> pathAString = new ArrayList<>(Arrays.asList(pathA.getStringNodeSequence().split(" ")));
                ArrayList<String> pathBString = new ArrayList<>(Arrays.asList(pathB.getStringNodeSequence().split(" ")));
                pathBString.remove(0);
                pathAString.addAll(pathBString);

                if(hasDuplicateNodes(pathAString)) {
                    return null;
                }
            }

            else if (Main.semantic.equals("Trail")) {
                ArrayList<String> pathAString = new ArrayList<>(Arrays.asList(pathA.getStringEdgeSequence().split(" ")));
                ArrayList<String> pathBString = new ArrayList<>(Arrays.asList(pathB.getStringEdgeSequence().split(" ")));
                pathAString.addAll(pathBString);
                
                if(hasDuplicateEdges(pathAString)) {
                    return null;
                }
            }
 
            join_path = new Path(UUID.randomUUID().toString());
            if(pathA.getNodesAmount()== 1 && pathB.getNodesAmount()== 1){
                join_path.insertNode(pathA.first());
            }
            else {
                for (Edge ed : pathA.getEdgeSequence())
                    join_path.insertEdge(ed);
                for (Edge ed : pathB.getEdgeSequence())

                    join_path.insertEdge(ed);
            }
        
            return join_path;
        }
        
        return null;
    }
    
    public static LinkedList<Path> NodeJoin (LinkedList<Path> pathsA, LinkedList<Path> pathsB){
        LinkedList<Path> join_path = new LinkedList<>();
        
        for (Path path1 : pathsA) {
            for (Path path2 : pathsB) {

                Path p = NodeLink(path1, path2);
                if(p != null) {
                    boolean equal = false;
                    for (Path jp : join_path) {
                        if(jp.equals(p)){
                            equal = true;
                            break;
                        }
                    }
                    if(!equal)
                        join_path.add(p);
                }
            } 
        }
        return join_path;
    }
    
    public Path EdgeLink (Path pathA, Path pathB){
        Path join_path = null;
        Edge edge = pathA.isEdgeLinkable(pathB, graph);
        if(edge != null){
            join_path = new Path(UUID.randomUUID().toString());
            for (Edge ed : pathA.getEdgeSequence())
                join_path.insertEdge(ed);
            join_path.insertEdge(edge);
            for (Edge ed : pathB.getEdgeSequence())
                join_path.insertEdge(ed);
        }
        return join_path;
    }
    
    public ArrayList<Path> EdgeJoin (ArrayList<Path> pathsA, ArrayList<Path> pathsB){
        ArrayList<Path> join_path = new ArrayList<>();
        for (Path path1 : pathsA) {
            for (Path path2 : pathsB) {
                Path p = EdgeLink(path1, path2);
                if(p != null)
                    join_path.add(p);
            } 
        }
        return join_path;
    }
    
    public static LinkedList<Path> Union (LinkedList<Path> pathsA, LinkedList<Path> pathsB){
        LinkedList<Path> union_path = new LinkedList<>();

        union_path.addAll(pathsA);
        for (Path path2 : pathsB) {
            boolean equal = false;
             for (Path path1 : pathsA) {
                if(path1.equals(path2)){
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
                if(path1.equals(path2)){
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
                if(path1.equals(path2)){
                    equal = true;
                    break;
                }
            } 
            if (!equal)
                difference_path.add(path1);
        }
        return difference_path;
    }
    
    public ArrayList<Path> LeftSubPaths (ArrayList<Path> paths){
        ArrayList<Path> left_sub_paths = new ArrayList<>();
        for (Path path : paths){
            for (int i = 0; i < path.getNodesAmount(); i++) {
                Path lsp = path.leftSubPath(i);
                boolean contains = false;
                for (Path elsp : left_sub_paths) 
                    if(lsp.equals(elsp))
                        contains=true;
                if (!contains)
                    left_sub_paths.add(lsp);
            }
        }
        return left_sub_paths;
    }
    
    public ArrayList<Path> RightSubPaths (ArrayList<Path> paths){
        ArrayList<Path> right_sub_paths = new ArrayList<>();
        for (Path path : paths){
            for (int i = 0; i < path.getNodesAmount(); i++) {
                Path rsp = path.rightSubPath(i);
                boolean contains = false;
                for (Path elsp : right_sub_paths) 
                    if(rsp.equals(elsp))
                        contains=true;
                if (!contains)
                    right_sub_paths.add(rsp);
            }
        }
        return right_sub_paths;
    }
    
    public ArrayList<Path> NodeCrossJoin (ArrayList<Path> pathsA, ArrayList<Path> pathsB){
       ArrayList<Path> left_sub_paths = LeftSubPaths(pathsA);
       ArrayList<Path> right_sub_paths = RightSubPaths(pathsB);
       ArrayList<Path> join_path = new ArrayList<>();
       for (Path path1 : left_sub_paths) {
           for (Path path2 : right_sub_paths) {
               Path p = NodeLink(path1, path2);
               if(p != null)
                   if(!IsPathInSet(join_path, p))
                       join_path.add(p);
           } 
       }
       return join_path;
    }

    public ArrayList<Path> EdgeCrossJoin (ArrayList<Path> pathsA, ArrayList<Path> pathsB){
       ArrayList<Path> left_sub_paths = LeftSubPaths(pathsA);
       ArrayList<Path> right_sub_paths = RightSubPaths(pathsB);
       ArrayList<Path> join_path = new ArrayList<>();
       for (Path path1 : left_sub_paths) {
           for (Path path2 : right_sub_paths) {
               Path p = EdgeLink(path1, path2);
               if(p != null)
                   if(!IsPathInSet(join_path, p))
                       join_path.add(p);
           } 
       }
       return join_path;
    }
 
    private boolean IsPathInSet (ArrayList<Path> paths, Path path){
        for (Path p : paths)
            if(p.equals(path))
                return true;
        return false;
    }
    
    public static boolean hasDuplicateNodes(ArrayList<String> tokens) {
        HashSet<String> visitedNodes = new HashSet<>();
        for (String token : tokens) {
            if (token.startsWith("N")) {
                if (!visitedNodes.add(token)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasDuplicateEdges(ArrayList<String> tokens) {
        HashSet<String> visitedEdges = new HashSet<>();

        for (String token : tokens) {
            if (token.startsWith("e")) {
                if (!visitedEdges.add(token)) {
                    return true;
                }
            }
        }

        return false;
    }

}
