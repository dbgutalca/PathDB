/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.pathdatabase.schema;

/**
 *
 * @author ramhg
 */
public class Path extends GraphObject{
    private Next first_next;
    private Next last_next;

    public Path(String id, String label, Next first_next, Next last_next) {
        super(id, label);
        this.first_next = first_next;
        this.last_next = last_next;
    }

    public Next getFirst_next() {
        return first_next;
    }

    public void setFirst_next(Next first_next) {
        this.first_next = first_next;
    }

    public Next getLast_next() {
        return last_next;
    }

    public void setLast_next(Next last_next) {
        this.last_next = last_next;
    }
    
    
    
}
