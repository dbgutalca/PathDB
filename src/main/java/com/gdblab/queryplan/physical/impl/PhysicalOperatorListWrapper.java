package com.gdblab.queryplan.physical.impl;

import java.util.Iterator;

import com.gdblab.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.schema.PathInterface;

public class PhysicalOperatorListWrapper extends PhysicalOp {

    protected final Iterator<PathInterface> PathInterfaces;

    public PhysicalOperatorListWrapper(final Iterator<PathInterface> PathInterfaces) {
        this.PathInterfaces = PathInterfaces;
    }

    @Override
    public void acceptVisitor(final PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean hasNext() {
        return this.PathInterfaces.hasNext();
    }

    @Override
    public PathInterface next() {
        return this.PathInterfaces.next();
    }
}
