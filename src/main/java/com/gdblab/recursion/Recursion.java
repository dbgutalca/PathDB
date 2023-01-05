/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.recursion;

import com.gdblab.algebra.PathAlgebra;
import com.gdblab.schema.Edge;
import com.gdblab.schema.Node;
import com.gdblab.schema.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author ramhg
 */
public class Recursion {
    
    
    public static ArrayList<Path> arbitrary (ArrayList<Path> s , int n){
        ArrayList<Path> results = (ArrayList<Path>) s.clone();
        ArrayList<Path> last_results = new ArrayList<>();
        int i = 0;
        while (results.size() > last_results.size() && i < n){
            last_results = (ArrayList<Path>) results.clone();
            results = PathAlgebra.Union(PathAlgebra.NodeJoin(results, s), results);
            i++;
        }
        return results;
    }
    
    //Obs: fue necesario revisar si el conjunto de entrada tenía nodos repetidos en sus caminos (bucles)
    public static ArrayList<Path> noRepeatedNodes (ArrayList<Path> s , int n){
        ArrayList<Path> results = removePathsWithRepeatedNodes(s);
        ArrayList<Path> last_results = new ArrayList<>();
        int i = 0;
        while (results.size() > last_results.size() && i < n){
            last_results = (ArrayList<Path>) results.clone();
            results = PathAlgebra.NodeJoin(results, s);
            results = removePathsWithRepeatedNodes(results);
            results = PathAlgebra.Union(results, last_results);
            i++;
        }
        return results;
    }
    
     public static ArrayList<Path> noRepeatedEdges (ArrayList<Path> s , int n){
        ArrayList<Path> results = (ArrayList<Path>) s.clone();
        ArrayList<Path> last_results = new ArrayList<>();
        int i = 0;
        while (results.size() > last_results.size() && i < n){
            last_results = (ArrayList<Path>) results.clone();
            results = PathAlgebra.NodeJoin(results, s);
            results = removePathsWithRepeatedEdges(results);
            results = PathAlgebra.Union(results, last_results);
            i++;
        }
        return results;
    }
    

    public static ArrayList<Path> removePathsWithRepeatedNodes(ArrayList<Path> s) {
        ArrayList<Path> result = (ArrayList<Path>) s.clone();
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
    
    public static ArrayList<Path> removePathsWithRepeatedEdges(ArrayList<Path> s) {
        ArrayList<Path> result = (ArrayList<Path>) s.clone();
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
    
}
