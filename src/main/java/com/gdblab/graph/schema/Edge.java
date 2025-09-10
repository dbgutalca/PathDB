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
public class Edge extends GraphObject {

    private Node source;
    private Node target;
    private HashMap<String, String> properties;

    public Edge(String id, String label, Node source, Node target, HashMap<String, String> properties) {
        super(id, label, 1);
        this.source = source;
        this.target = target;
        this.properties = properties;
    }
    
    public Edge(String id, Node source, Node target) {
        super(id, 1);
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
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(" \"id\": \"").append(this.getId()).append("\",");
        sb.append("\"label\": \"").append(this.getLabel()).append("\",");
        sb.append("\"source\": ");
        sb.append(this.source.toString());
        sb.append(",");
        sb.append("\"target\": ");
        sb.append(this.target.toString());
        sb.append("}");
        return sb.toString();
    }
    
}
