package com.gdblab.algebra.parser.error;

public class PropertyDoesntExist extends RuntimeException {

    private final ErrorDetails details;

    public PropertyDoesntExist(ErrorDetails details) {
        this.details = details;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Property does not exists: ").append(details.getMsg()).append("\n")
            .append(" ".repeat(details.getPos())).append("^");
        return sb.toString();
    }
}
