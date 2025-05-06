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
        if (nodePos < 0 || nodePos > p.getNodesAmount()) return "";
        if (p.getNodeAt(nodePos).getProperties().containsKey(propertyName)) {
            return p.getNodeAt(nodePos).getProperty(propertyName);
        }
        // MAYBE THROW AN EXCEPTION OF PROPERTY NOT EXISTS
        return "";
    }
    
}
