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
public class Last extends Condition{
    
    public String prop;
    public String value;

    public Last(String prop, String value) {
        this.prop = prop;
        this.value = value;
    }

    @Override
    public boolean eval(Path p) {
        if(p != null)
            return p.last().getProperty(prop).equals(value);
        return false;
    }

    public String getProp() {
        return prop;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Last{" + "prop=" + prop + ", value=" + value + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Last) {
            return ((Last)o).prop.equals(prop) && ((Last)o).value.equals(value);
        }
        return false;
    }
    
}
