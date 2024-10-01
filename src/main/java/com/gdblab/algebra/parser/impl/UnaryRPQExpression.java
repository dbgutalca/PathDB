package com.gdblab.algebra.parser.impl;

import com.gdblab.algebra.parser.RPQExpression;

public abstract class UnaryRPQExpression implements RPQExpression {

    protected final RPQExpression child;

    protected UnaryRPQExpression(final RPQExpression child) {
        this.child = child;
    }

    public RPQExpression getChild() {
        return child;
    }
}
