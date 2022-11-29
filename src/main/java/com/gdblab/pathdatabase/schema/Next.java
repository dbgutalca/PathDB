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
public class Next {
    private String id;
    private Node source;
    private Edge edge;
    private Node target;
    private Next next;

    public Next(String id, Node source, Edge edge, Node target, Next next) {
        this.id = id;
        this.source = source;
        this.edge = edge;
        this.target = target;
        this.next = next;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Node getSource() {
        return source;
    }

    public void setSource(Node source) {
        this.source = source;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public Node getTarget() {
        return target;
    }

    public void setTarget(Node target) {
        this.target = target;
    }

    public Next getNext() {
        return next;
    }

    public void setNext_id(Next next) {
        this.next = next;
    }
    
    
}
