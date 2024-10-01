package com.gdblab.algebra.queryplan.logical.impl;

import com.gdblab.algebra.queryplan.logical.LogicalOperator;
import com.gdblab.algebra.queryplan.logical.LogicalPlanVisitor;

public class LogicalOpRecursive extends UnaryLogicalOp{
    public LogicalOpRecursive(final LogicalOperator child) {
        super(child);
    }

    @Override
    public void acceptVisitor(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LogicalOpRecursive that) {
            return this.child.equals(that.child);
        }
        return false;
    }

    @Override
    public String toString() {
        return "RECURSIVE+("+child+")";
    }
}
