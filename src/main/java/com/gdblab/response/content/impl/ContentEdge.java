package com.gdblab.response.content.impl;

import com.gdblab.graph.schema.Path;
import com.gdblab.response.content.Content;

public class ContentEdge extends Content {

    int edgePos;

    public ContentEdge(int edgePos, String returnName) {
        super(returnName);
        this.edgePos = edgePos - 1;
    }

    @Override
    public String getContent(Path p) {
        if (this.edgePos < p.getEdgeLength() && this.edgePos >= 0) return p.getEdgeAt(edgePos).toString();
        return "";
    }
    
}
