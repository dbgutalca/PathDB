package com.gdblab.queryplan.logical.impl;

import com.gdblab.algebra.condition.Condition;
import com.gdblab.queryplan.logical.LogicalOperator;
import com.gdblab.queryplan.logical.LogicalPlanVisitor;
import com.gdblab.queryplan.logical.UnaryLogicalOperator;

public class LogicalOpSelection extends UnaryLogicalOp {

    protected Condition c;

    public LogicalOpSelection(final LogicalOperator child, final Condition c) {
        this.child = child;
        this.c = c;
    }

    @Override
    public void acceptVisitor(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    public Condition getCondition() {
        return c;
    }
}
