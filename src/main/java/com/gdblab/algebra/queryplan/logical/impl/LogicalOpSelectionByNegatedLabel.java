package com.gdblab.algebra.queryplan.logical.impl;

import com.gdblab.algebra.condition.Condition;
import com.gdblab.algebra.queryplan.logical.LogicalPlanVisitor;
import com.gdblab.algebra.queryplan.logical.NullaryLogicalOperator;

public class LogicalOpSelectionByNegatedLabel implements NullaryLogicalOperator {

    protected Condition c;

    public LogicalOpSelectionByNegatedLabel(final Condition c) {
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
        return obj instanceof LogicalOpSelection;
    }

    @Override
    public String toString() {
        return "!S1";
    }

}
