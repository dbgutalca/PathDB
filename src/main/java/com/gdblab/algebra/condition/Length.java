/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.algebra.condition;

import com.gdblab.graph.schema.Path;

/**
 *
 * @author ramhg
 */
public class Length  extends Condition{
    
    public int length;
    public String condition;

    public Length(int length, String condition) {
        this.length = length;
        this.condition = condition;
    }
    

    @Override
    public boolean eval(Path p) {
        if (p != null) {
            int pathLength = p.getPathLength();
            switch (this.condition) {
                case "=":
                    return pathLength == this.length;
                case "!=":
                    return pathLength != this.length;
                case "<":
                    return pathLength < this.length;
                case "<=":
                    return pathLength <= this.length;
                case ">":
                    return pathLength > this.length;
                case ">=":
                    return pathLength >= this.length;
                default:
                    return false;
            }
        }
        return false;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

   
    
}
