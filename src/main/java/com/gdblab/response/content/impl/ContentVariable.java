package com.gdblab.response.content.impl;

import com.gdblab.execution.Context;
import com.gdblab.graph.schema.Path;
import com.gdblab.response.content.Content;

public class ContentVariable extends Content {

    String variableName;

    public ContentVariable(String variableName, String returnName) {
        super(returnName);
        this.variableName = variableName;
    }

    @Override
    public String getContent(Path p) {
        String pathName = Context.getInstance().getPathsName();
        String leftVariableName = Context.getInstance().getLeftVarName();
        String rightVariableName = Context.getInstance().getRightVarName();

        if (!pathName.equals("") && pathName.equals(this.variableName)) {
            return p.toString();
        }
        else if (!leftVariableName.equals("") && leftVariableName.equals(this.variableName)) {
            return p.first().toString();
        }
        else if (!rightVariableName.equals("") && rightVariableName.equals(this.variableName)) {
            return p.last().toString();
        }
        else {
            return "";
        }
    }
    
}
