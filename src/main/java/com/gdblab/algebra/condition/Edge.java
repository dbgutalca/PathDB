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
public class Edge  extends Condition{
    
    public String id;
    public int pos;

    public Edge(String id, int pos) {
        this.id = id;
        this.pos = pos;
    }
    

    @Override
    public boolean eval(Path p) {
        return p.getEdgeAt(this.getPos()).getId().equals(this.getId());
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
