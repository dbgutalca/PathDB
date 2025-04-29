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
public class First extends Condition{
    
    public String prop;
    public String condition;
    public String value;

    public First(String prop, String condition, String value) {
        this.prop = prop;
        this.condition = condition;
        this.value = value;
    }
    
    @Override
    public boolean eval(Path p) {
        if(p != null){
            Boolean propExists = p.first().getProperties().containsKey(this.prop);
            if(!propExists) return false;

            Float value_1 = null;
            Float value_2 = null;

            switch (this.condition) {
                case "=":
                    return p.first().getProperty(this.prop).equals(this.value);
                case "!=":
                    return !p.first().getProperty(this.prop).equals(this.value);
                case "<":
                    value_1 = Float.parseFloat(this.value);
                    value_2 = Float.parseFloat(p.first().getProperty(this.prop).toString());
                    return value_2 < value_1;
                case "<=":
                    value_1 = Float.parseFloat(this.value);
                    value_2 = Float.parseFloat(p.first().getProperty(this.prop).toString());
                    return value_2 <= value_1;
                case ">":
                    value_1 = Float.parseFloat(this.value);
                    value_2 = Float.parseFloat(p.first().getProperty(this.prop).toString());
                    return value_2 > value_1;
                case ">=":
                    value_1 = Float.parseFloat(this.value);
                    value_2 = Float.parseFloat(p.first().getProperty(this.prop).toString());
                    return value_2 >= value_1;
                default:
                    return false;
            }
        }
        return false;
    }

    public String getProp() {
        return prop;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "First{" + "prop=" + prop + ", value=" + value + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof First)
            return ((First)o).prop.equals(prop) && ((First)o).value.equals(value);
        return false;
    }

}
