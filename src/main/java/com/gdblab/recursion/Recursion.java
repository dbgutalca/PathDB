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
    
    public static ArrayList<Path> shortestPath (ArrayList<Path> s , int n){
        ArrayList<Path> results = (ArrayList<Path>) s.clone();
        ArrayList<Path> last_results = new ArrayList<>();
        ArrayList<Path> reached_paths = new ArrayList<>();
        int i = 0;
        while (results.size() > last_results.size() && i < n){
            last_results = (ArrayList<Path>) results.clone();
            results = PathAlgebra.Union(PathAlgebra.NodeJoin(results, s), last_results);
            results = removeReachedPaths(results,reached_paths);
            i++;
        }
        return results;
    }
    
    private static ArrayList<Path> removeReachedPaths(ArrayList<Path> results, ArrayList<Path> reached_paths) {
        ArrayList <Path> reached_paths_clon = (ArrayList <Path>) reached_paths.clone();
        ArrayList <Path> results_clon = (ArrayList <Path>) results.clone();
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
        reached_paths = (ArrayList<Path>) reached_paths_clon.clone();
        return results_clon;
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
