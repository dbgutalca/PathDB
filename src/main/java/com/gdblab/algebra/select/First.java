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
public class First  extends Condition{
    
    public String id;

    public First(String id) {
        this.id = id;
    }
    

    @Override
    public boolean eval(Path p, Condition c) {
        return p.First().getId().equals(this.getId());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
