package com.gdblab.response.content.impl;

import com.gdblab.graph.schema.Path;
import com.gdblab.response.content.Content;

public class ContentEdgeWithProperty extends Content {

    int edgePos;
    String propertyName;

    public ContentEdgeWithProperty(int edgePos, String propertyName, String returnName) {
        super(returnName);
        this.edgePos = edgePos - 1;
        this.propertyName = propertyName;
    }

    @Override
    public String getContent(Path p) {
        if (this.edgePos < p.getEdgeLength() && this.edgePos >= 0) {
            return p.getEdgeAt(edgePos).getProperty(propertyName);
        }
        return "";
    }
    
}
