package com.gdblab.algebra.returncontent;

import com.gdblab.execution.Context;
import com.gdblab.graph.schema.Path;

public class ReturnVariableWithProperty extends ReturnContent {

    String variableName;
    String propertyName;

    public ReturnVariableWithProperty(String variableName, String propertyName, String returnName) {
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
            switch (this.propertyName) {
                case "LENGTH":
                    return String.valueOf(p.getQuantityOfEdges());

                default:
                    return "";
            }
        }

        else if (!leftVariableName.equals("") && leftVariableName.equals(this.variableName)) {
            return p.getPropertyValueOfNodeAtPosition(0, this.propertyName);
        }

        else if (!rightVariableName.equals("") && rightVariableName.equals(this.variableName)) {
            return p.getPropertyValueOfNodeAtPosition(-1, this.propertyName);
        }

        return "";

    }

}
