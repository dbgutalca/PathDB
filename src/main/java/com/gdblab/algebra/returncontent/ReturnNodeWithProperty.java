package com.gdblab.algebra.returncontent;

import com.gdblab.graph.schema.Path;

public class ReturnNodeWithProperty extends ReturnContent {

    int nodePos;
    String propertyName;

    public ReturnNodeWithProperty(int nodePos, String propertyName, String returnName) {
        super(returnName);
        this.nodePos = nodePos - 1;
        this.propertyName = propertyName;
    }

    @Override
    public String getContent(Path p) {
        if (this.nodePos == -2)
            this.nodePos = p.getQuantityOfNodes() - 1;

        if (nodePos < p.getQuantityOfNodes() && nodePos >= 0)
            return p.getPropertyValueOfNodeAtPosition(nodePos, propertyName);
        return "";
    }

}
