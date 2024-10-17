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
public class Last  extends Condition{
    
    public String id;

    public Last(String id) {
        this.id = id;
    }
    

    @Override
    public boolean eval(Path p) {
        return p.last().getId().equals(this.getId());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Last: "+id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Last) {
            return id.equals(((Last) o).id);
        }
        return false;
    }
    
}
