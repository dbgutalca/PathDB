package com.gdblab.algebra.queryplan.logical.impl;

import com.gdblab.algebra.queryplan.logical.LogicalOperator;
import com.gdblab.algebra.queryplan.logical.LogicalPlanVisitor;

public class LogicalOpDifference extends BinaryLogicalOp{

    public LogicalOpDifference(final LogicalOperator leftChild, final LogicalOperator rightChild) {
        super(leftChild, rightChild);
    }

    @Override
    public void acceptVisitor(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }
}
