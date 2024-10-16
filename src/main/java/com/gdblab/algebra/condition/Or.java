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
public class Or  extends Condition{
    
    Condition c1;
    Condition c2;

    public Or(Condition c1, Condition c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    public boolean eval(Path p) {
        return getC1().eval(p) || getC2().eval(p);
    }

    public Condition getC1() {
        return c1;
    }

    public void setC1(Condition c1) {
        this.c1 = c1;
    }

    public Condition getC2() {
        return c2;
    }

    public void setC2(Condition c2) {
        this.c2 = c2;
    }

   

   
    
}
