package com.gdblab.queryplan.logical.impl;

import com.gdblab.queryplan.logical.LogicalOperator;
import com.gdblab.queryplan.logical.LogicalPlanVisitor;

public class LogicalOpProjection extends UnaryLogicalOp {
    protected int start;
    protected int end;

    public LogicalOpProjection(final LogicalOperator child, final int start, final int end) {
        this.child = child;
        this.start = start;
        this.end = end;
    }

    @Override
    public void acceptVisitor(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }

}
