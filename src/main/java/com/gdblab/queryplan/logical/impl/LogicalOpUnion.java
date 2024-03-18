package com.gdblab.queryplan.logical.impl;

import com.gdblab.queryplan.logical.LogicalPlanVisitor;

public class LogicalOpUnion extends BinaryLogicalOp{
    @Override
    public void visit(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }
}
