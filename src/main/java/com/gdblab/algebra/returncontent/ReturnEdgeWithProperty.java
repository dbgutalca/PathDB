package com.gdblab.algebra.returncontent;

import com.gdblab.graph.schema.Path;

public class ReturnEdgeWithProperty extends ReturnContent {

    int edgePos;
    String propertyName;

    public ReturnEdgeWithProperty(int edgePos, String propertyName, String returnName) {
        super(returnName);
        this.edgePos = edgePos - 1;
        this.propertyName = propertyName;
    }

    @Override
    public String getContent(Path p) {
        if (this.edgePos < p.getQuantityOfEdges() && this.edgePos >= 0) {
            return p.getPropertyValueOfEdgeAtPosition(edgePos, propertyName);
        }
        return "";
    }

}
