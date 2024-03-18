package com.gdblab.queryplan.logical.impl;

import com.gdblab.queryplan.logical.LogicalPlanVisitor;

public class LogicalOpNodeProduct extends BinaryLogicalOp {

    @Override
    public void visit(LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }
}
