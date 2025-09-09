package com.gdblab.response.content.impl;

import com.gdblab.graph.schema.Path;
import com.gdblab.response.content.Content;

public class ConentNodeWithProperty extends Content {

    int nodePos;
    String propertyName;

    public ConentNodeWithProperty(int nodePos, String propertyName, String returnName) {
        super(returnName);
        this.nodePos = nodePos - 1;
        this.propertyName = propertyName;
    }

    @Override
    public String getContent(Path p) {
        if (this.nodePos == -2) return p.last().getProperty(propertyName);
        if (nodePos < p.getNodesAmount() && nodePos >= 0) return p.getNodeAt(nodePos).getProperty(propertyName);
        return "";
    }
    
}
