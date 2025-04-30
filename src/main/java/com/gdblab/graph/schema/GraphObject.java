/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.graph.schema;

import java.util.Objects;

/**
 *
 * @author ramhg
 */
public class GraphObject {
    
    private String id;
    private String label;
    private Integer length;

    public GraphObject(String id, String label, Integer length) {
        this.id = id;
        this.label = label;
        this.length = length;
    }
    
    public GraphObject(String id, Integer length) {
        this.id = id;
        this.length = length;
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

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GraphObject that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label);
    }
}
