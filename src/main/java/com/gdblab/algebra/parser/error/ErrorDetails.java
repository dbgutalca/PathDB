package com.gdblab.algebra.parser.error;

import com.gdblab.execution.Context;

public class ErrorDetails {
    private final int line;
    private final int pos;
    private final String msg;

    public ErrorDetails(int line, int pos, String msg) {
        this.line = line;
        this.pos = pos;
        this.msg = msg;
    }

    public ErrorDetails(int line, String var ,String msg) {
        this.line = line;
        this.pos = this.getPos(var);
        this.msg = msg;
    }

    private int getPos(String var) {
        String[] query = Context.getInstance().getCompleteQuery().split("");
        int pos = query[1].indexOf(var);
        if (pos == -1) {
            return 0;
        } else {
            return pos + query[0].length() + 5;
        }
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