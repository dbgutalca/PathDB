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
public class Last extends Condition{
    
    public String prop;
    public String condition;
    public String value;

    public Last(String prop, String condition, String value) {
        this.prop = prop;
        this.condition = condition;
        this.value = value;
    }

    @Override
    public boolean eval(Path p) {
        if(p != null){
            Boolean propExists = p.last().getProperties().containsKey(this.prop);
            if(!propExists) return false;

            Float value_1 = null;
            Float value_2 = null;

            switch (this.condition) {
                case "=":
                    return p.last().getProperty(this.prop).equals(this.value);
                case "!=":
                    return !p.last().getProperty(this.prop).equals(this.value);
                case "<":
                    value_1 = Float.parseFloat(this.value);
                    value_2 = Float.parseFloat(p.last().getProperty(this.prop).toString());
                    return value_2 < value_1;
                case "<=":
                    value_1 = Float.parseFloat(this.value);
                    value_2 = Float.parseFloat(p.last().getProperty(this.prop).toString());
                    return value_2 <= value_1;
                case ">":
                    value_1 = Float.parseFloat(this.value);
                    value_2 = Float.parseFloat(p.last().getProperty(this.prop).toString());
                    return value_2 > value_1;
                case ">=":
                    value_1 = Float.parseFloat(this.value);
                    value_2 = Float.parseFloat(p.last().getProperty(this.prop).toString());
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

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Last{" + "prop=" + prop + ", value=" + value + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Last) {
            return ((Last)o).prop.equals(prop) && ((Last)o).value.equals(value);
        }
        return false;
    }
    
}
