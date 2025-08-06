package com.gdblab.algebra.returncontent;

import com.gdblab.execution.Context;
import com.gdblab.graph.schema.Path;

public class ReturnVariable extends ReturnContent {

    String variableName;

    public ReturnVariable(String variableName, String returnName) {
        super(returnName);
        this.variableName = variableName;
    }

    @Override
    public String getContent(Path p) {
        String pathName = Context.getInstance().getPathsName();
        String leftVariableName = Context.getInstance().getLeftVarName();
        String rightVariableName = Context.getInstance().getRightVarName();

        if (!pathName.equals("") && pathName.equals(this.variableName)) {
            return p.getStringPath();
        } else if (!leftVariableName.equals("") && leftVariableName.equals(this.variableName)) {
            return p.getNode(0);
        } else if (!rightVariableName.equals("") && rightVariableName.equals(this.variableName)) {
            return p.getNode(-1);
        } else {
            return "";
        }
    }

}
