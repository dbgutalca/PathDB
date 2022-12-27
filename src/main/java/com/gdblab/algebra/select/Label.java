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
public class Label  extends Condition{
    
    public String label;

    public Label(String label) {
        this.label = label;
    }
    

    @Override
    public boolean eval(Path p, Condition c) {
        return p.getLabel().equals(this.getLabel());
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

   
    
}
