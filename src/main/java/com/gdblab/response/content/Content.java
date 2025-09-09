package com.gdblab.response.content;

import com.gdblab.graph.schema.Path;

public abstract class Content {

    public String returnName = null;
    
    public Content(String returnName) {
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
