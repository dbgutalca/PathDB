package com.gdblab.parser.impl;

import com.gdblab.parser.RPQExpression;

public abstract class BinaryRPQExpression implements RPQExpression {

    protected final RPQExpression left;
    protected final RPQExpression right;

    public BinaryRPQExpression(final RPQExpression left, final RPQExpression right) {
        this.left = left;
        this.right = right;
    }

    public RPQExpression getLeft(){
        return left;
    }

    public RPQExpression getRight(){
        return right;
    }

}
