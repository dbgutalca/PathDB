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
public class GraphObject {
    
    private String id;
    private String label;

    public GraphObject(String id, String label) {
        this.id = id;
        this.label = label;
    }
    
    public GraphObject(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    
}
