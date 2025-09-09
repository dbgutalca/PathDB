package com.gdblab.response.content.impl;

import com.gdblab.graph.schema.Path;
import com.gdblab.response.content.Content;

public class ContentLabelNode extends Content {

    int nodePos;

    public ContentLabelNode(int nodePos, String returnName) {
        super(returnName);
        this.nodePos = nodePos - 1;
    }

    @Override
    public String getContent(Path p) {
        if (this.nodePos == -2) return p.last().getLabel();
        if (nodePos < p.getNodesAmount() && nodePos >= 0) return p.getNodeAt(nodePos).getLabel();
        return "";
    }
    
}
