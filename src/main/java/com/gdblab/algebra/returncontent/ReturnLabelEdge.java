package com.gdblab.algebra.returncontent;

import com.gdblab.graph.schema.Path;

public class ReturnLabelEdge extends ReturnContent {

    int edgePos;

    public ReturnLabelEdge(int edgePos, String returnName) {
        super(returnName);
        this.edgePos = edgePos - 1;
    }

    @Override
    public String getContent(Path p) {
        if (edgePos < p.getQuantityOfEdges() && edgePos >= 0)
            return p.getLabelOfEdgeAtPosition(edgePos);
        return "";
    }

}
