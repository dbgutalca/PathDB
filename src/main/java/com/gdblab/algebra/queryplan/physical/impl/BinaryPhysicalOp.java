package com.gdblab.algebra.queryplan.physical.impl;

import com.gdblab.algebra.queryplan.logical.LogicalOperator;
import com.gdblab.algebra.queryplan.physical.BinaryPhysicalOperator;
import com.gdblab.algebra.queryplan.physical.PhysicalOperator;

public abstract class BinaryPhysicalOp extends PhysicalOp implements BinaryPhysicalOperator {

    protected final PhysicalOperator leftChild;
    protected final PhysicalOperator rightChild;

    public BinaryPhysicalOp(final PhysicalOperator leftChild, final PhysicalOperator rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    @Override
    public PhysicalOperator getLeftChild() {
        return leftChild;
    }

    @Override
    public PhysicalOperator getRightChild() {
        return rightChild;
    }

}
