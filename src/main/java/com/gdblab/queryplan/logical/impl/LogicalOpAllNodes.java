package com.gdblab.queryplan.logical.impl;

import com.gdblab.queryplan.logical.LogicalPlanVisitor;
import com.gdblab.queryplan.logical.NullaryLogicalOperator;

public class LogicalOpAllNodes implements NullaryLogicalOperator {
    
    @Override
    public void acceptVisitor(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LogicalOpAllNodes;
    }

    @Override
    public String toString() {
        return "S0";
    }
}
