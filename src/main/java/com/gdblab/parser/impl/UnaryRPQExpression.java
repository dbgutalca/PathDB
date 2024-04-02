package com.gdblab.parser.impl;

import com.gdblab.parser.RPQExpression;

public abstract class UnaryRPQExpression implements RPQExpression {

    protected final RPQExpression child;

    protected UnaryRPQExpression(final RPQExpression child) {
        this.child = child;
    }

    public RPQExpression getChild() {
        return child;
    }
}
