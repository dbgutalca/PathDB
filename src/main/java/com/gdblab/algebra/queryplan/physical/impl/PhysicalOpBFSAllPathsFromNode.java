package com.gdblab.algebra.queryplan.physical.impl;

import com.gdblab.algebra.queryplan.logical.impl.LogicalOpAllPathsStartingFromNode;
import com.gdblab.algebra.queryplan.physical.NullaryPhysicalOperator;
import com.gdblab.algebra.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.schema.Path;

public class PhysicalOpBFSAllPathsFromNode implements NullaryPhysicalOperator {


    private final LogicalOpAllPathsStartingFromNode lop;

    public PhysicalOpBFSAllPathsFromNode(final LogicalOpAllPathsStartingFromNode lop) {
        this.lop = lop;
    }

    @Override
    public void acceptVisitor(final PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Path next() {
        return null;
    }
}
