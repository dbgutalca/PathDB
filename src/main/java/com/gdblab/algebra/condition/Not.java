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
public class Not extends Condition{
    
    Condition c1;

    public Not(Condition c1) {
        this.c1 = c1;
    }

    @Override
    public boolean eval(Path p, Condition c) {
        return  !this.getC1().eval(p, this.getC1());
    }

    public Condition getC1() {
        return c1;
    }

    public void setC1(Condition c1) {
        this.c1 = c1;
    }

    

   

   
    
}
