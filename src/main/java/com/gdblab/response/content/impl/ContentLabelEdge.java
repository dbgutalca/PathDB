package com.gdblab.response.content.impl;

import com.gdblab.graph.schema.Path;
import com.gdblab.response.content.Content;

public class ContentLabelEdge extends Content {

    int edgePos;

    public ContentLabelEdge(int edgePos, String returnName) {
        super(returnName);
        this.edgePos = edgePos - 1;
    }

    @Override
    public String getContent(Path p) {
        if (edgePos < p.getEdgeLength() && edgePos >= 0) return p.getEdgeAt(edgePos).getLabel();
        return "";
    }
    
}
