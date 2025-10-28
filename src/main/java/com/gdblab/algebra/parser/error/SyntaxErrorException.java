package com.gdblab.algebra.parser.error;

public class SyntaxErrorException extends RuntimeException {

    private final ErrorDetails details;

    public SyntaxErrorException(ErrorDetails details) {
        this.details = details;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" ".repeat(details.getPos())).append("^").append("\n")
                .append("Syntax Error: ").append(details.getMsg()).append("\n");
        return sb.toString();
    }
}
