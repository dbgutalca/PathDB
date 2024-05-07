package com.gdblab.queryplan.physical.impl;

import java.util.Iterator;

import com.gdblab.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.schema.Path;

public class PhysicalOperatorListWrapper extends PhysicalOp {

    protected final Iterator<Path> paths;

    public PhysicalOperatorListWrapper(final Iterator<Path> paths) {
        this.paths = paths;
    }

    @Override
    public void acceptVisitor(final PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean hasNext() {
        return this.paths.hasNext();
    }

    @Override
    public Path next() {
        return this.paths.next();
    }
}
