package com.gdblab.queryplan.physical.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.gdblab.algebra.PathAlgebra;
import com.gdblab.queryplan.logical.impl.LogicalOpNodeJoin;
import com.gdblab.queryplan.logical.impl.LogicalOpRecursive;
import com.gdblab.queryplan.physical.PhysicalOperator;
import com.gdblab.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.queryplan.util.Utils;
import com.gdblab.schema.Path;

public class PhysicalOpRecursive extends UnaryPhysicalOp {

    protected final LogicalOpRecursive lop;
    protected final LogicalOpNodeJoin lon;
    protected Path slot = null;

    protected final List<Path> originalChild;
    private List<Path> loopChild;

    private PhysicalOpNestedLoopNodeJoin join;

    private Iterator<Path> childIterator;

    private Integer counterFixPoint = 1;

    public PhysicalOpRecursive(final PhysicalOperator child, final LogicalOpRecursive lop, final LogicalOpNodeJoin lon) {
        super(child);
        this.lop = lop;
        this.lon = lon;

        this.loopChild = new LinkedList<Path>();

        join = new PhysicalOpNestedLoopNodeJoin(
            new PhysicalOperatorListWrapper(child),
            new PhysicalOperatorListWrapper(child),
            lon);

        originalChild = Utils.iterToList(child);
        childIterator = originalChild.iterator();
    }

    @Override
    public void acceptVisitor(PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean hasNext() {
        if ( slot == null ) {
            slot = getNextPath();
            return slot != null;
        }
        return true;
    }

    @Override
    public Path next() {
        final Path r = slot;
        slot = null;
        return r;
    }

    protected Path getNextPath() {
        while (this.childIterator.hasNext()) {
            Path path = this.childIterator.next();
            return path;
        }

        for ( ;; ) {
            if (this.counterFixPoint >= 3) {
                return null;
            }

            while (this.join.hasNext()) {
                Path path = this.join.next();
                if (path != null) {
                    this.loopChild.add(path);
                    return path;
                }
            }

            this.counterFixPoint++;
            this.join = new PhysicalOpNestedLoopNodeJoin(
                new PhysicalOperatorListWrapper(this.loopChild.iterator()), 
                new PhysicalOperatorListWrapper(this.originalChild.iterator()), 
                lon);
            this.loopChild.clear();
        }
    }
    
}
