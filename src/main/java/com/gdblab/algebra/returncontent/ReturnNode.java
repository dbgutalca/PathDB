package com.gdblab.algebra.returncontent;

import com.gdblab.graph.schema.Path;

public class ReturnNode extends ReturnContent {

    int nodePos;

    public ReturnNode(int nodePos, String returnName) {
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
