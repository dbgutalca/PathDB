/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.algebra.condition;

import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.Node;
import com.gdblab.graph.schema.Path;

/**
 *
 * @author ramhg
 */
public class Label extends Condition{
    
    public String label;
    public String type;
    public int pos;

    public Label(String label, String type, int pos) {
        this.label = label;
        this.type = type;
        this.pos = pos;
    }

    @Override
    public boolean eval(Path p) {
        switch (this.type) {
            case "node":
                if (p.getNodeSequence().size() <= this.pos) return false;
                Node n = p.getNodeAt(this.pos); if (n == null) return false;
                return n.getLabel().equals(this.label);
            case "edge":
                if (p.getEdgeSequence().size() <= this.pos) return false;
                Edge e = p.getEdgeAt(this.pos); if (e == null) return false;
                return e.getLabel().equals(this.label);
            default:
                return false;
        }
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "label("+label + "," + pos+")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Label that) {
            return this.label.equals(that.label) &&
                    this.pos == that.pos;
        }
        return false;
    }
}
