/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.algebra.condition;

import com.gdblab.schema.GraphObject;
import com.gdblab.schema.Path;

/**
 *
 * @author ramhg
 */
public class Label  extends Condition{
    
    public String label;
    public int pos;

    public Label(String label, int pos) {
        this.label = label;
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

   
    

    @Override
    public boolean eval(Path p) {
        if (p.getSequence().size() > pos)
            return p.getSequence().get(pos).getLabel().equals(label);
        else
            return false;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

   
    
}
