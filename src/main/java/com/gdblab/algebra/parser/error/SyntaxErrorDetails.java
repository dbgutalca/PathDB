package com.gdblab.algebra.parser.error;

public class SyntaxErrorDetails {
    private final int line;
    private final int pos;
    private final String msg;

    public SyntaxErrorDetails(int line, int pos, String msg) {
        this.line = line;
        this.pos = pos;
        this.msg = msg;
    }

    public int getLine() {
        return line;
    }

    public int getPos() {
        return pos;
    }

    public String getMsg() {
        return msg;
    }
}