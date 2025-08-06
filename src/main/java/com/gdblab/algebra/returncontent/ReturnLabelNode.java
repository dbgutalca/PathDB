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
        if (this.nodePos == -2)
            this.nodePos = p.getQuantityOfNodes() - 1;

        if (nodePos < p.getQuantityOfNodes() && nodePos >= 0)
            return p.getLabelOfNodeAtPosition(nodePos);

        return "";
    }

}
