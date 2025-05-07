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
        if (this.nodePos == -2) return p.last().getProperty(propertyName).toString();
        if (nodePos < p.getNodesAmount() && nodePos >= 0) return p.getNodeAt(nodePos).getProperty(propertyName).toString();
        return "";
    }
    
}
