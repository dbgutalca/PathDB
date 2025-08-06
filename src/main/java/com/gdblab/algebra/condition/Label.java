package com.gdblab.algebra.condition;

import com.gdblab.graph.schema.Path;

/**
 *
 * @author ramhg
 */
public class Label extends Condition {
    
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
                String nodeLabel = p.getLabelOfNodeAtPosition(pos);
                if (nodeLabel == null) return false;
                return nodeLabel.equals(this.label);

            case "edge":
                String edgeLabel = p.getLabelOfEdgeAtPosition(pos);
                if (edgeLabel == null) return false;
                return edgeLabel.equals(this.label);
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
