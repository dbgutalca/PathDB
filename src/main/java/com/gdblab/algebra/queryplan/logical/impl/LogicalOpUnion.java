package com.gdblab.algebra.queryplan.logical.impl;

import com.gdblab.algebra.queryplan.logical.LogicalOperator;
import com.gdblab.algebra.queryplan.logical.LogicalPlanVisitor;

public class LogicalOpUnion extends BinaryLogicalOp{

    public LogicalOpUnion(final LogicalOperator leftChild, final LogicalOperator rightChild) {
        super(leftChild, rightChild);
    }

    @Override
    public void acceptVisitor(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LogicalOpUnion that) {
            return this.leftChild.equals(that.leftChild) &&
                    this.rightChild.equals(that.rightChild);
        }
        return false;
    }

    @Override
    public String toString() {
        return "UNION ("+leftChild+", "+rightChild+")";
    }
}
