package com.gdblab.queryplan.logical.impl;

import com.gdblab.queryplan.logical.LogicalPlanVisitor;

public class LogicalOpIntersection extends BinaryLogicalOp{
    public LogicalOpIntersection() {
        super(, );
    }

    @Override
    public void acceptVisitor(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }
}
