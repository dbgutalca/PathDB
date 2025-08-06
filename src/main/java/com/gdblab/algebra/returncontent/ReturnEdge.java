package com.gdblab.algebra.returncontent;

import com.gdblab.graph.schema.Path;

public class ReturnEdge extends ReturnContent {

    int edgePos;

    public ReturnEdge(int edgePos, String returnName) {
        super(returnName);
        this.edgePos = edgePos - 1;
    }

    @Override
    public String getContent(Path p) {
        if (this.edgePos < p.getQuantityOfEdges() && this.edgePos >= 0)
            return p.getEdge(edgePos);
        return "";
    }

}
