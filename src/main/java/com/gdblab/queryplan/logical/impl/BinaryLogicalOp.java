package com.gdblab.queryplan.logical.impl;

import com.gdblab.queryplan.logical.BinaryLogicalOperator;
import com.gdblab.queryplan.logical.LogicalOperator;

public abstract class BinaryLogicalOp implements BinaryLogicalOperator {
    protected LogicalOperator leftChild;
    protected LogicalOperator rightChild;

    @Override
    public LogicalOperator getLeftChild() {
        return leftChild;
    }

    @Override
    public LogicalOperator getRightChild() {
        return rightChild;
    }
}
