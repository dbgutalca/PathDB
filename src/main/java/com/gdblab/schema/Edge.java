/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.schema;

/**
 *
 * @author ramhg
 */
public class Edge extends GraphObject {
    private Node source;
    private Node target;

    public Edge(String id, String label, Node source, Node target) {
        super(id, label);
        this.source = source;
        this.target = target;
    }
    
    public Edge(String id, Node source, Node target) {
        super(id);
        this.source = source;
        this.target = target;
    }

    public Node getSource() {
        return source;
    }

    public void setSource(Node source) {
        this.source = source;
    }

    public Node getTarget() {
        return target;
    }

    public void setTarget(Node target) {
        this.target = target;
    }
    
    
    
}
