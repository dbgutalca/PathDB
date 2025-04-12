package com.gdblab.algebra.parser.error;

public class SyntaxErrorException extends RuntimeException {
    private final SyntaxErrorDetails details;

    public SyntaxErrorException(SyntaxErrorDetails details) {
        super(details.toString());
        this.details = details;
    }

    public SyntaxErrorDetails getDetails() {
        return details;
    }
}
