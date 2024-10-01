package com.gdblab.algebra.queryplan.logical.impl;

import com.gdblab.algebra.condition.Condition;
import com.gdblab.algebra.queryplan.logical.LogicalOperator;
import com.gdblab.algebra.queryplan.logical.LogicalPlanVisitor;
import com.gdblab.algebra.queryplan.logical.UnaryLogicalOperator;

import java.util.Objects;

public class LogicalOpSelection extends UnaryLogicalOp {

    protected Condition c;

    public LogicalOpSelection(final LogicalOperator child, final Condition c) {
        super(child);
        this.c = c;
    }

    @Override
    public void acceptVisitor(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    public Condition getCondition() {
        return c;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LogicalOpSelection that) {
            return Objects.equals(this.child, that.child) &&
                    this.c.equals(that.c);
        }
        return false;
    }

    @Override
    public String toString() {
        return "SELECT<"+c+">("+child+")";
    }
}
