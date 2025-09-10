package com.gdblab.algebra.returncontent;

import com.gdblab.graph.schema.Path;

public abstract class ReturnContent {

    public String returnName = null;
    
    public ReturnContent(String returnName) {
        this.returnName = returnName;
    }

    public abstract String getContent(Path p);

    public String getReturnName() {
        return returnName;
    }

    public void setReturnName(String returnName) {
        this.returnName = returnName;
    }
}
