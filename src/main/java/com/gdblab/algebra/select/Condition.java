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
public abstract class Condition {

    public Condition() {
    }
    
    public abstract boolean eval (Path p, Condition c);
    
}
