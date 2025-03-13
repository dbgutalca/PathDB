/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.graph.schema;

/**
 *
 * @author ramhg
 */
public class Node extends GraphObject {
    
    public Node(String id, String label) {
        super(id, label);
    } 
    
    public Node(String id) {
        super(id);
    }

    @Override
    public String toString() {
        return "Node{" + "id=" + this.getId() + ", label=" + getLabel() + '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
