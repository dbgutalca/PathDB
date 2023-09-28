/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.algebra;

import com.gdblab.algebra.condition.Condition;
import com.gdblab.schema.Path;
import java.util.ArrayList;

/**
 *
 * @author ramhg
 */
public final class Select {
    
    public static ArrayList<Path> eval (ArrayList<Path> path_set, Condition c, Boolean negated){
        ArrayList<Path> result_set = new ArrayList<>();
        for (Path p : path_set) {
            if(negated){
                if(!c.eval(p)){
                    result_set.add(p);
                }
            }
            else{
                if(c.eval(p))
                    result_set.add(p);
                }
            }
        return result_set;
    }
}
    
    
    
    
    
    
   
     
    
   
    



