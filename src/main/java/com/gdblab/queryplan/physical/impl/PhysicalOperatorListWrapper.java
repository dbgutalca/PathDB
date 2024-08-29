package com.gdblab.queryplan.physical.impl;

import java.util.Iterator;

import com.gdblab.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.schema.Path;

public class PhysicalOperatorListWrapper extends PhysicalOp {

    protected final Iterator<Path> Paths;

    public PhysicalOperatorListWrapper(final Iterator<Path> Paths) {
        this.Paths = Paths;
    }

    @Override
    public void acceptVisitor(final PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean hasNext() {
        return this.Paths.hasNext();
    }

    @Override
    public Path next() {
        return this.Paths.next();
    }
}
