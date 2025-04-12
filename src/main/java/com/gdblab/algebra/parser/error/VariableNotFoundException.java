package com.gdblab.algebra.parser.error;

import com.gdblab.execution.Context;

public class VariableNotFoundException extends RuntimeException {
    private final ErrorDetails details;

    public VariableNotFoundException(ErrorDetails details) {
        this.details = details;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Variable Not Found: ").append(details.getMsg()).append("\n")
            .append(Context.getInstance().getCompleteQuery()).append("\n")
            .append(" ".repeat(details.getPos())).append("^");
        return sb.toString();
    }
    
}
