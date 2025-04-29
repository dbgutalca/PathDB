package com.gdblab.algebra.parser.error;

public class ReturnedVariableAlreadyExists extends RuntimeException{
    private final ErrorDetails details;

    public ReturnedVariableAlreadyExists(ErrorDetails details) {
        this.details = details;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Returned Variable Already Exists: ").append(details.getMsg()).append("\n")
            .append(" ".repeat(details.getPos())).append("^");
        return sb.toString();
    }
}
