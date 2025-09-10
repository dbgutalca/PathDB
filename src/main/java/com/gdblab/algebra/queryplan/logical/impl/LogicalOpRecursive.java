package com.gdblab.algebra.queryplan.logical.impl;

import com.gdblab.algebra.queryplan.logical.LogicalOperator;
import com.gdblab.algebra.queryplan.logical.LogicalPlanVisitor;

public class LogicalOpRecursive extends BinaryLogicalOp{
    private boolean lastFilter = false;
    private boolean firstFilter = false;

    public LogicalOpRecursive(final LogicalOperator leftChild, final LogicalOperator rightChild) {
        super(leftChild, rightChild);
    }

    public LogicalOpRecursive(final LogicalOperator leftChild, final LogicalOperator rightChild, final boolean lastFilter, final boolean firstFilter) {
        super(leftChild, rightChild);
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
            return this.leftChild.equals(that.leftChild) && this.rightChild.equals(that.rightChild);
        }
        return false;
    }

    @Override
    public String toString() {
        return "RECURSIVE("+leftChild+", "+rightChild+")";
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
