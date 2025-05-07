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
    
    public String prop;
    public String condition;
    public String value;
    public int pos;

    public Edge(String prop, String condition, String value, int pos) {
        this.prop = prop;
        this.condition = condition;
        this.value = value;
        this.pos = pos;
    }
    

    @Override
    public boolean eval(Path p) {
        if (p != null) {

            com.gdblab.graph.schema.Edge edge = p.getEdgeAt(this.pos);

            if (edge == null) return false;
            if (edge.getProperties().containsKey(this.prop)) return false;

            Float value_1 = null;
            Float value_2 = null;

            switch (this.condition) {
                case "=":
                    return edge.getProperty(this.prop).equals(this.value);
                case "!=":
                    return !edge.getProperty(this.prop).equals(this.value);
                case "<":
                    value_1 = Float.parseFloat(this.value);
                    value_2 = Float.parseFloat(edge.getProperty(this.prop).toString());
                    return value_2 < value_1;
                case "<=":
                    value_1 = Float.parseFloat(this.value);
                    value_2 = Float.parseFloat(edge.getProperty(this.prop).toString());
                    return value_2 <= value_1;
                case ">":
                    value_1 = Float.parseFloat(this.value);
                    value_2 = Float.parseFloat(edge.getProperty(this.prop).toString());
                    return value_2 > value_1;
                case ">=":
                    value_1 = Float.parseFloat(this.value);
                    value_2 = Float.parseFloat(edge.getProperty(this.prop).toString());
                    return value_2 >= value_1;
                default:
                    return false;
            }
        }
        return false;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
