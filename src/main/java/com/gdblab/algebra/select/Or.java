/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.algebra.select;

import com.gdblab.schema.Path;

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
    public boolean eval(Path p, Condition c) {
        return c.eval(p, getC1()) || c.eval(p, getC2());
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
