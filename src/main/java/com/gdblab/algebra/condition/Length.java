/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.algebra.condition;

import com.gdblab.schema.Path;

/**
 *
 * @author ramhg
 */
public class Length  extends Condition{
    
    public int length;

    public Length(int length) {
        this.length = length;
    }
    

    @Override
    public boolean eval(Path p, Condition c) {
        return p.lenght() == this.getLength();
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

   
    
}
