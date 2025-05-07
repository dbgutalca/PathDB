package com.gdblab.algebra.returncontent;

import com.gdblab.graph.schema.Path;

public class ReturnLabelNode extends ReturnContent {

    int nodePos;

    public ReturnLabelNode(int nodePos, String returnName) {
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
