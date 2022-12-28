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
    public boolean eval(Path p, Condition c) {
        return p.Last().getId().equals(this.getId());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
