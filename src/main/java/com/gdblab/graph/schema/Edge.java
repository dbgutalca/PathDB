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
    
    @Override
    public String toString() {
        // CAMBIAR ID LABEL A NODE.TOSTRING();
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"id\": \"").append(this.getId()).append("\",\n");
        sb.append("  \"label\": \"").append(this.getLabel()).append("\",\n");
        sb.append("  \"source\": {\n");
        sb.append("    \"id\": \"").append(this.source.getId()).append("\",\n");
        sb.append("    \"label\": \"").append(this.source.getLabel()).append("\"\n");
        sb.append("  },\n");
        sb.append("  \"target\": {\n");
        sb.append("    \"id\": \"").append(this.target.getId()).append("\",\n");
        sb.append("    \"label\": \"").append(this.target.getLabel()).append("\"\n");
        sb.append("  }\n");
        sb.append("}");
        return sb.toString();
    }
    
}
