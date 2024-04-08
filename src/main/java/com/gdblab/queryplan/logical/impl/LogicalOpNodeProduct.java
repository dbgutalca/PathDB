package com.gdblab.queryplan.logical.impl;

import com.gdblab.queryplan.logical.LogicalPlanVisitor;

public class LogicalOpNodeProduct extends BinaryLogicalOp {

    public LogicalOpNodeProduct() {
        super(, );
    }

    @Override
    public void acceptVisitor(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }
}
