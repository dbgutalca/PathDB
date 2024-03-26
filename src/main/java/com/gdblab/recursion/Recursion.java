/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.recursion;

import com.gdblab.algebra.PathAlgebra;
import com.gdblab.main.Main;
import com.gdblab.schema.Edge;
import com.gdblab.schema.GraphObject;
import com.gdblab.schema.Node;
import com.gdblab.schema.Path;
import com.ibm.icu.text.Edits.Iterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ramhg
 */
public class Recursion {
    
    public static LinkedList<Path> arbitrary (LinkedList<Path> s , int n){
        if (Main.semantic.equals("Simple Path")) s = removeDuplicatedNodes(s);
        else if (Main.semantic.equals("Trail")) s = removeDuplicatedEdges(s);
        
        LinkedList<Path> results = (LinkedList<Path>) s.clone();
        LinkedList<Path> last_results = new LinkedList<>();
        LinkedList<Path> temp = new LinkedList<>();
        int i = 0;
        while (results.size() > last_results.size() && i < 3){
            last_results = (LinkedList<Path>) results.clone();
            temp = PathAlgebra.NodeJoin(results, s);
            results = PathAlgebra.Union(temp, results);
            checkPath(results);
            i++;
        }
        
        return results;
    }
    
    //Obs: fue necesario revisar si el conjunto de entrada tenía nodos repetidos en sus caminos (bucles)
    public static LinkedList<Path> noRepeatedNodes (LinkedList<Path> s , int n){
        LinkedList<Path> results = removePathsWithRepeatedNodes(s);
        LinkedList<Path> last_results = new LinkedList<>();
        int i = 0;
        while (results.size() > last_results.size() && i < n){
            last_results = (LinkedList<Path>) results.clone();
            results = PathAlgebra.NodeJoin(results, s);
            results = removePathsWithRepeatedNodes(results);
            results = PathAlgebra.Union(results, last_results);
            i++;
        }
        return results;
    }
    
    public static LinkedList<Path> noRepeatedEdges (LinkedList<Path> s , int n){
        LinkedList<Path> results = (LinkedList<Path>) s.clone();
        LinkedList<Path> last_results = new LinkedList<>();
        int i = 0;
        while (results.size() > last_results.size() && i < n){
            last_results = (LinkedList<Path>) results.clone();
            results = PathAlgebra.NodeJoin(results, s);
            results = removePathsWithRepeatedEdges(results);
            results = PathAlgebra.Union(results, last_results);
            i++;
        }
        return results;
    }
    
    public static LinkedList<Path> shortestPath (LinkedList<Path> s , int n){
        LinkedList<Path> results = (LinkedList<Path>) s.clone();
        LinkedList<Path> last_results = new LinkedList<>();
        LinkedList<Path> reached_paths = new LinkedList<>();
        int i = 0;
        while (results.size() > last_results.size() && i < n){
            last_results = (LinkedList<Path>) results.clone();
            results = PathAlgebra.Union(PathAlgebra.NodeJoin(results, s), last_results);
            results = removeReachedPaths(results,reached_paths);
            i++;
        }
        return results;
    }
    
    private static LinkedList<Path> removeReachedPaths(LinkedList<Path> results, LinkedList<Path> reached_paths) {
        LinkedList <Path> reached_paths_clon = (LinkedList <Path>) reached_paths.clone();
        LinkedList <Path> results_clon = (LinkedList <Path>) results.clone();
        for (Path rs : results){
            if(!reached_paths.contains(rs)){
                boolean new_path = true;
                for (Path rp: reached_paths){
                    if(rs.getId() != rp.getId() && rs.First() == rp.First() &&  rs.Last() == rp.Last() && rp.lenght() <= rs.lenght()){
                        results_clon.remove(rs);
                        new_path = false;
                    }
                    else if (rs.getId() != rp.getId() && rs.First() == rp.First() &&  rs.Last() == rp.Last() && rp.lenght() > rs.lenght()){
                        reached_paths_clon.add(rs);
                        reached_paths_clon.remove(rp);
                        new_path = false;
                    }
                }
                //Si no existe ningun path similar, es necesario agregarlo a los alcanzados.
                if(new_path)
                    reached_paths.add(rs);
                    reached_paths_clon.add(rs);
            }
               
        }
        reached_paths = (LinkedList<Path>) reached_paths_clon.clone();
        return results_clon;
    }
    
    
    
    

    public static LinkedList<Path> removePathsWithRepeatedNodes(LinkedList<Path> s) {
        LinkedList<Path> result = (LinkedList<Path>) s.clone();
        for(Path p : s){
            for(Node n: p.getNodeSequence()){
                if (Collections.frequency(p.getNodeSequence(), n) >1){
                    result.remove(p);
                    break;
                }
            }
        }
        return result;
    }
    
    public static LinkedList<Path> removePathsWithRepeatedEdges(LinkedList<Path> s) {
        LinkedList<Path> result = (LinkedList<Path>) s.clone();
        for(Path p : s){
            for(Edge e: p.getEdgeSequence()){
                if (Collections.frequency(p.getEdgeSequence(), e) >1){
                    result.remove(p);
                    break;
                }
            }
        }
        return result;
    }

    private static void checkPath(LinkedList<Path> paths) {
        LinkedList<Path> pathsToRemove = new LinkedList<>();

        for (Path p : paths) {
            Map<String, Integer> c = new HashMap<>();

            for (Edge e : p.getEdgeSequence()) {
                if (c.containsKey(e.getId())) {
                    c.put(e.getId(), c.get(e.getId()) + 1);
                } else {
                    c.put(e.getId(), 1);
                }
            }

            for (Map.Entry<String, Integer> entry : c.entrySet()) {
                if (entry.getValue() >= 3) {
                    pathsToRemove.add(p);
                }
            }

        }

        paths.removeAll(pathsToRemove);
    }

    public static LinkedList<Path> removeDuplicatedNodes(LinkedList<Path> paths) {
        LinkedList<Path> filteredPaths = new LinkedList<>();
        for (Path input : paths) {
            boolean flag = false;
            String[] tokens = input.getStringNodeSequence().split(" ");
            HashSet<String> visitedNodes = new HashSet<>();
            for (String token : tokens) {
                if (token.startsWith("N")) {
                    if (!visitedNodes.add(token)) {
                        flag = true;
                        break;
                    }
                }
            }   
            if (!flag) filteredPaths.add(input);
        }
        return filteredPaths;
    }

    public static LinkedList<Path> removeDuplicatedEdges(LinkedList<Path> paths) {
        LinkedList<Path> filteredPaths = new LinkedList<>();
        for (Path input : paths) {
            boolean flag = false;
            String[] tokens = input.getStringNodeSequence().split(" ");
            HashSet<String> visitedEdges = new HashSet<>();

            for (String token : tokens) {
                if (token.startsWith("e")) {
                    if (!visitedEdges.add(token)) {
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag) filteredPaths.add(input);
        }
        return filteredPaths;
    }
}
