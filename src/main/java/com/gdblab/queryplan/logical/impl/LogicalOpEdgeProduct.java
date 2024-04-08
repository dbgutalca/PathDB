package com.gdblab.queryplan.logical.impl;

import com.gdblab.queryplan.logical.LogicalPlanVisitor;

public class LogicalOpEdgeProduct extends BinaryLogicalOp{
    public LogicalOpEdgeProduct() {
        super(, );
    }

    @Override
    public void acceptVisitor(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }
}
