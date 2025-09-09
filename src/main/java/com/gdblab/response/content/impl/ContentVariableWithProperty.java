package com.gdblab.response.content.impl;

import com.gdblab.execution.Context;
import com.gdblab.graph.schema.Path;
import com.gdblab.response.content.Content;

public class ContentVariableWithProperty extends Content {

    String variableName;
    String propertyName;

    public ContentVariableWithProperty(String variableName, String propertyName, String returnName) {
        super(returnName);
        this.variableName = variableName;
        this.propertyName = propertyName;
    }

    @Override
    public String getContent(Path p) {
        String pathName = Context.getInstance().getPathsName();
        String leftVariableName = Context.getInstance().getLeftVarName();
        String rightVariableName = Context.getInstance().getRightVarName();

        if (!pathName.equals("") && pathName.equals(this.variableName)) {
            if (this.propertyName.equals("LENGTH")) {
                return String.valueOf(p.getEdgeLength());
            }
            // MAYBE THROW AN EXCEPTION OF PROPERTY NOT EXISTS
            return "";
        }
        else if (!leftVariableName.equals("") && leftVariableName.equals(this.variableName)) {
            if (p.first().getProperties().containsKey(this.propertyName)) {
                return p.first().getProperty(this.propertyName);
            }
            // MAYBE THROW AN EXCEPTION OF PROPERTY NOT EXISTS
            return "";
        }
        else if (!rightVariableName.equals("") && rightVariableName.equals(this.variableName)) {
            if (p.last().getProperties().containsKey(this.propertyName)) {
                return p.last().getProperty(this.propertyName);
            }
            // MAYBE THROW AN EXCEPTION OF PROPERTY NOT EXISTS
            return "";
        }
        else {
            // MAYBE THROW AN EXCEPTION OF VARIABLE NOT EXISTS
            return "";
        }
    }
    
}
