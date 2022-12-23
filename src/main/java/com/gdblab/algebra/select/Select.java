/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.algebra.select;

import com.gdblab.schema.Path;
import java.util.ArrayList;

/**
 *
 * @author ramhg
 */
public final class Select {
    
    //public static ArrayList<Path>  select (ArrayList<Path> paths, )
    
    
    
    public static ArrayList<Path> evalFirstNodeId (ArrayList<Path> paths, String value){
        ArrayList<Path> selected_paths = new ArrayList<>();
        for (Path p : paths)
            if(SimpleCondition.equalValue(p.First().getId(), value))
                selected_paths.add(p);
        return selected_paths;
    } 
    
    public static ArrayList<Path> evalLastNodeId (ArrayList<Path> paths, String value){
        ArrayList<Path> selected_paths = new ArrayList<>();
        for (Path p : paths)
            if(SimpleCondition.equalValue(p.Last().getId(), value))
                selected_paths.add(p);
        return selected_paths;
    } 
    
    public static ArrayList<Path> evalNodeId (ArrayList<Path> paths, int pos, String value){
        ArrayList<Path> selected_paths = new ArrayList<>();
        for (Path p : paths)
            if(SimpleCondition.equalValue(p.GetNodeX(pos).getId(), value))
                selected_paths.add(p);
        return selected_paths;
    } 
    
    public static ArrayList<Path> evalNodeLabel (ArrayList<Path> paths, int pos, String value){
        ArrayList<Path> selected_paths = new ArrayList<>();
        for (Path p : paths)
            if(SimpleCondition.equalValue(p.GetNodeX(pos).getLabel(), value))
                selected_paths.add(p);
        return selected_paths;
    } 
    
    public static ArrayList<Path> evalEdgeId (ArrayList<Path> paths, int pos, String value){
        ArrayList<Path> selected_paths = new ArrayList<>();
        for (Path p : paths)
            if(SimpleCondition.equalValue(p.GetEdgeX(pos).getId(), value))
                selected_paths.add(p);
        return selected_paths;
    } 
    
     public static ArrayList<Path> evalEdgeLabel (ArrayList<Path> paths, int pos, String value){
        ArrayList<Path> selected_paths = new ArrayList<>();
        for (Path p : paths)
            if(SimpleCondition.equalValue(p.GetEdgeX(pos).getLabel(), value))
                selected_paths.add(p);
        return selected_paths;
    } 
    
   
    
}


