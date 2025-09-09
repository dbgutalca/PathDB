package com.gdblab.response.content.impl;

import com.gdblab.graph.schema.Path;
import com.gdblab.response.content.Content;

public class ContentNode extends Content {

    int nodePos;

    public ContentNode(int nodePos, String returnName) {
        super(returnName);
        this.nodePos = nodePos - 1;
    }

    @Override
    public String getContent(Path p) {
        if (this.nodePos == -2) return p.last().toString();
        if (nodePos < p.getNodesAmount() && nodePos >= 0) return p.getNodeAt(nodePos).toString();
        return "";
    }
    
}
