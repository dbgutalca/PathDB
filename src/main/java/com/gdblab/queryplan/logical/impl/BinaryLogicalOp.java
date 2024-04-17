package com.gdblab.queryplan.logical.impl;

import com.gdblab.queryplan.logical.BinaryLogicalOperator;
import com.gdblab.queryplan.logical.LogicalOperator;

public abstract class BinaryLogicalOp implements BinaryLogicalOperator {
    protected final LogicalOperator leftChild;
    protected final LogicalOperator rightChild;

    public BinaryLogicalOp(final LogicalOperator leftChild, final LogicalOperator rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    @Override
    public LogicalOperator getLeftChild() {
        return leftChild;
    }

    @Override
    public LogicalOperator getRightChild() {
        return rightChild;
    }
}
