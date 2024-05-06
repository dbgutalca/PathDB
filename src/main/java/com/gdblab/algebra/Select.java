/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.algebra;

import com.gdblab.algebra.condition.Condition;
import com.gdblab.schema.Path;

import java.util.LinkedList;

/**
 *
 * @author ramhg
 */
public final class Select {
    
    public static LinkedList<Path> eval (LinkedList<Path> path_set, Condition c){
        LinkedList<Path> result_set = new LinkedList<>();
        for (Path p : path_set) {
            if(c.eval(p))
                result_set.add(p);
            }
        return result_set;
    }
}
    
    
    
    
    
    
   
     
    
   
    



