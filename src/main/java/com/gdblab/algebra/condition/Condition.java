/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.algebra.condition;

import com.gdblab.schema.Path;
import com.gdblab.schema.PathInterface;

/**
 *
 * @author ramhg
 */
public abstract class Condition {

    public Condition() {
    }
    
    public abstract boolean eval (PathInterface p);
    
}
