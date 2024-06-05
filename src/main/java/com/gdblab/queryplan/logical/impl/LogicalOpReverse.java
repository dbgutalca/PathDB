package com.gdblab.queryplan.logical.impl;

import com.gdblab.queryplan.logical.LogicalOperator;
import com.gdblab.queryplan.logical.LogicalPlanVisitor;

import java.util.Objects;

public class LogicalOpReverse extends UnaryLogicalOp {

    public LogicalOpReverse(final LogicalOperator child) {
        super(child);
    }

    @Override
    public void acceptVisitor(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LogicalOpReverse that) {
            return Objects.equals(this.child, that.child);
        }
        return false;
    }

    @Override
    public String toString() {
        return "REVERSE("+child+")";
    }
}
