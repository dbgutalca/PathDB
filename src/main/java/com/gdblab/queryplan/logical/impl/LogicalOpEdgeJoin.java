package com.gdblab.queryplan.logical.impl;

import com.gdblab.queryplan.logical.LogicalPlanVisitor;

public class LogicalOpEdgeJoin extends BinaryLogicalOp {
    public LogicalOpEdgeJoin() {
        super(, );
    }

    @Override
    public void acceptVisitor(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }
}
