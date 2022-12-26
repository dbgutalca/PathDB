/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.algebra.select;

/**
 *
 * @author ramhg
 */
public  class SimpleCondition {

    public SimpleCondition() {
    }
    
    
    
    public static boolean equalValue (Object v1, Object v2){
        if(v1 instanceof Integer && v2 instanceof Integer)
            if((Integer) v1 == (Integer) v2)
                return true;
        if(v1 instanceof Float && v2 instanceof Float)
            if(((Float) v1).equals((Float) v2))
                return true;
        if(v1 instanceof Double && v2 instanceof Double)
            if(((Double) v1).equals((Double) v2))
                return true;
        if(v1 instanceof String && v2 instanceof String)
            if((String) v1 == (String) v2)
                return true;
        return false;
    }
    
    public static  boolean lessThanValue (Object v1, Object v2){
        if(v1 instanceof Integer && v2 instanceof Integer)
           if((Integer) v1 < (Integer) v2)
               return true;
       if(v1 instanceof Float && v2 instanceof Float)
           if(((Float) v1).compareTo((Float) v2) < 0 )
               return true;
       if(v1 instanceof Double && v2 instanceof Double)
           if(((Double) v1).compareTo((Double) v2) < 0)
               return true;
       return false;
    }
    
    public static  boolean lessThanOrEqualValue (Object v1, Object v2){
        if(v1 instanceof Integer && v2 instanceof Integer)
           if((Integer) v1 <= (Integer) v2)
               return true;
        if(v1 instanceof Float && v2 instanceof Float)
           if(((Float) v1).compareTo((Float) v2) <= 0 )
               return true;
       if(v1 instanceof Double && v2 instanceof Double)
           if(((Double) v1).compareTo((Double) v2) <= 0  )
               return true;
       return false;
    }
    
    public static  boolean greaterThanValue (Object v1, Object v2){
       if(v1 instanceof Integer && v2 instanceof Integer)
          if((Integer) v1 > (Integer) v2)
              return true;
       if(v1 instanceof Float && v2 instanceof Float)
           if(((Float) v1).compareTo((Float) v2) > 0 )
               return true;
       if(v1 instanceof Double && v2 instanceof Double)
           if(((Double) v1).compareTo((Double) v2) > 0)
               return true;
      return false;
   }
    
    public static  boolean greaterThanOrEqualValue (Object v1, Object v2){
      if(v1 instanceof Integer && v2 instanceof Integer)
         if((Integer) v1 >= (Integer) v2)
             return true;
     if(v1 instanceof Float && v2 instanceof Float)
           if(((Float) v1).compareTo((Float) v2) >= 0 )
               return true;
       if(v1 instanceof Double && v2 instanceof Double)
           if(((Double) v1).compareTo((Double) v2) >= 0)
               return true;
     return false;
    }
    
    
    
    
}
