package com.gdblab.queryplan.logical.impl;

import com.gdblab.queryplan.logical.LogicalOperator;
import com.gdblab.queryplan.logical.UnaryLogicalOperator;

public abstract class UnaryLogicalOp implements UnaryLogicalOperator {

    protected LogicalOperator child;

    public UnaryLogicalOp(final LogicalOperator child) {
        this.child = child;
    }

    @Override
    public LogicalOperator getChild() {
        return child;
    }
}
