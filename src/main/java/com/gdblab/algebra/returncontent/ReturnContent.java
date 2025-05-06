package com.gdblab.algebra.returncontent;

import com.gdblab.graph.schema.Path;

public abstract class ReturnContent {
    
    public ReturnContent() {
    }

    public abstract String getContent(Path p);
}
