package com.gdblab.queryplan.physical.impl;

import com.gdblab.queryplan.physical.PhysicalOperator;
import com.gdblab.queryplan.physical.UnaryPhysicalOperator;

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
