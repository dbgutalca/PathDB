package com.gdblab.algebra.queryplan.logical.impl;

import com.gdblab.algebra.queryplan.logical.LogicalOperator;
import com.gdblab.algebra.queryplan.logical.LogicalPlanVisitor;

public class LogicalOpProjection extends UnaryLogicalOp {
    protected int start;
    protected int end;

    public LogicalOpProjection(final LogicalOperator child, final int start, final int end) {
        super(child);
        this.start = start;
        this.end = end;
    }

    @Override
    public void acceptVisitor(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }

}
