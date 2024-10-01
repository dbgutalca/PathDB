package com.gdblab.algebra.queryplan.logical.impl;

import com.gdblab.algebra.queryplan.logical.LogicalPlanVisitor;
import com.gdblab.algebra.queryplan.logical.NullaryLogicalOperator;

public class LogicalOpAllEdges implements NullaryLogicalOperator {
    
    @Override
    public void acceptVisitor(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LogicalOpAllEdges;
    }

    @Override
    public String toString() {
        return "S1";
    }
}
