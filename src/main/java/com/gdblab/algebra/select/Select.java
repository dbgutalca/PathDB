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
    
    public static ArrayList<Path> evalPathsFirstNode(ArrayList<Path> paths, String firstNodeId){
        ArrayList<Path> selectedPaths = new ArrayList<>();
        for(Path p : paths)
            if(evalFirstNodeId(p, firstNodeId))
                selectedPaths.add(p);
        return selectedPaths;
    }
    
    public static ArrayList<Path> evalPathsLastNode(ArrayList<Path> paths, String lastNodeId){
        ArrayList<Path> selectedPaths = new ArrayList<>();
        for(Path p : paths)
            if(evalLastNodeId(p, lastNodeId))
                selectedPaths.add(p);
        return selectedPaths;
    }
    
    
    public static boolean evalAndCondition (Path path, boolean cond1, boolean cond2){
        return ComplexCondition.andCondition(cond1, cond2);
    }
    
    public static boolean evalOrCondition (Path path, boolean cond1, boolean cond2){
        return ComplexCondition.orCondition(cond1, cond2);
    }
    public static boolean evalNotCondition (Path path, boolean cond){
        return ComplexCondition.notCondition(cond);
    }
    
    
    public static boolean evalFirstNodeId (Path path, String value){
        return SimpleCondition.equalValue(path.First().getId(), value);
    } 
    
    public static boolean evalLastNodeId (Path path, String value){
        return SimpleCondition.equalValue(path.Last().getId(), value);
    } 
    
    public static boolean evalNodeId (Path path, int pos, String value){
        return SimpleCondition.equalValue(path.GetNodeX(pos).getId(), value);
    } 
    
    public static boolean evalNodeLabel (Path path, int pos, String value){
        return SimpleCondition.equalValue(path.GetNodeX(pos).getLabel(), value);
    } 
    
    public static boolean evalEdgeId (Path path, int pos, String value){
        return SimpleCondition.equalValue(path.GetEdgeX(pos).getId(), value);
    } 
    
    public static boolean evalEdgeLabel (Path path, int pos, String value){
        return SimpleCondition.equalValue(path.GetEdgeX(pos).getLabel(), value) ;
    } 
    
    public static boolean evalEqualLength (Path path, int value){
        return SimpleCondition.equalValue(path.lenght(), value) ;
    } 
    
    public static boolean evalLessThanLength (Path path, int value){
        return SimpleCondition.lessThanValue(path.lenght(), value) ;
    } 
    
    public static boolean evalLessThanOrEqualLength (Path path, int value){
        return SimpleCondition.lessThanOrEqualValue(path.lenght(), value) ;
    } 
    
     public static boolean evalGreaterThanLength (Path path, int value){
        return SimpleCondition.greaterThanValue(path.lenght(), value) ;
    } 
    
    public static boolean evalGreaterThanOrEqualLength (Path path, int value){
        return SimpleCondition.greaterThanOrEqualValue(path.lenght(), value) ;
    } 
     
     
    
   
    
}


