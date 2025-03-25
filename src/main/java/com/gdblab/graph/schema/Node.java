/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.graph.schema;

import java.util.HashMap;

/**
 *
 * @author ramhg
 */
public class Node extends GraphObject {

    HashMap<String, String> properties;
    
    public Node(String id, String label, HashMap<String, String> properties) {
        super(id, label);
        this.properties = properties;
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

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }
}
