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
        super(id, label, 0);
        this.properties = properties;
    } 
    
    public Node(String id) {
        super(id, 0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(" \"id\": \"").append(this.getId()).append("\",");
        sb.append(" \"label\": \"").append(this.getLabel()).append("\",");
        sb.append(" \"properties\": {");

        if (properties != null && !properties.isEmpty()) {
            int count = 0;
            for (var entry : properties.entrySet()) {
                sb.append("\"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\"");
                if (++count < properties.size()) {
                    sb.append(",");
                }
            }
        }

        sb.append("}");
        sb.append("}");
        return sb.toString();
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
