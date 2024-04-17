package com.gdblab.queryplan.logical.impl;

import com.gdblab.queryplan.logical.LogicalOperator;
import com.gdblab.queryplan.logical.LogicalPlanVisitor;

import java.util.Objects;

public class LogicalOpNodeJoin extends BinaryLogicalOp {

    public LogicalOpNodeJoin(final LogicalOperator leftChild, final LogicalOperator rightChild) {
        super(leftChild, rightChild);
    }

    @Override
    public void acceptVisitor(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LogicalOpNodeJoin that) {
            return Objects.equals(this.leftChild, that.leftChild) &&
                    Objects.equals(this.rightChild, that.rightChild);
        }
        return false;
    }

    @Override
    public String toString() {
        return "NODE-JOIN("+leftChild+", "+rightChild+")";
    }
}
