package com.gdblab.algebra.condition;

import com.gdblab.graph.schema.Path;

/**
 *
 * @author ramhg
 */
public class First extends Condition {
    
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

            String property_value = p.getPropertyValueOfNodeAtPosition(0, prop);

            if (property_value == null) return false;

            Float value_1 = null;
            Float value_2 = null;

            switch (this.condition) {
                case "=":
                    return property_value.equals(this.value);
                case "!=":
                    return !property_value.equals(this.value);
                case "<":
                    value_1 = Float.parseFloat(this.value);
                    value_2 = Float.parseFloat(property_value);
                    return value_2 < value_1;
                case "<=":
                    value_1 = Float.parseFloat(this.value);
                    value_2 = Float.parseFloat(property_value);
                    return value_2 <= value_1;
                case ">":
                    value_1 = Float.parseFloat(this.value);
                    value_2 = Float.parseFloat(property_value);
                    return value_2 > value_1;
                case ">=":
                    value_1 = Float.parseFloat(this.value);
                    value_2 = Float.parseFloat(property_value);
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
