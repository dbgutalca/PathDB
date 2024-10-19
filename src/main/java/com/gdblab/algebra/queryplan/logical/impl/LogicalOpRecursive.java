package com.gdblab.algebra.queryplan.logical.impl;

import com.gdblab.algebra.queryplan.logical.LogicalOperator;
import com.gdblab.algebra.queryplan.logical.LogicalPlanVisitor;

public class LogicalOpRecursive extends UnaryLogicalOp{
    private boolean lastFilter = false;
    private boolean firstFilter = false;

    public LogicalOpRecursive(final LogicalOperator child) {
        super(child);
    }

    public LogicalOpRecursive(final LogicalOperator child, final boolean lastFilter, final boolean firstFilter) {
        super(child);
        this.lastFilter = lastFilter;
        this.firstFilter = firstFilter;
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

    public boolean hasFirstFilter() {
        return this.firstFilter;
    }

    public boolean hasLastFilter() {
        return this.lastFilter;
    }

    public void setFilters(boolean lastFilter, boolean firstFilter) {
        this.lastFilter = lastFilter;
        this.firstFilter = firstFilter;
    }
}
