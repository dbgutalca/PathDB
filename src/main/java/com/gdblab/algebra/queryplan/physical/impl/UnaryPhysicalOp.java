package com.gdblab.algebra.queryplan.physical.impl;

import com.gdblab.algebra.queryplan.physical.PhysicalOperator;
import com.gdblab.algebra.queryplan.physical.UnaryPhysicalOperator;

public abstract class UnaryPhysicalOp extends PhysicalOp implements UnaryPhysicalOperator {

    private final PhysicalOperator child;

    public UnaryPhysicalOp(final PhysicalOperator child) {
        this.child = child;
    }

    @Override
    public PhysicalOperator getChild() {
        return child;
    }
}
