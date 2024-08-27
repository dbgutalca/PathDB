package com.gdblab.queryplan.physical.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.gdblab.execution.Context;
import com.gdblab.queryplan.logical.impl.LogicalOpNodeJoin;
import com.gdblab.queryplan.logical.impl.LogicalOpRecursive;
import com.gdblab.queryplan.physical.PhysicalOperator;
import com.gdblab.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.queryplan.util.Utils;
import com.gdblab.schema.PathInterface;

public class PhysicalOpRecursive extends UnaryPhysicalOp {

    protected final LogicalOpRecursive lop;
    protected PathInterface slot = null;

    protected final List<PathInterface> originalChild;
    private final List<PathInterface> loopChild;

    private PhysicalOpHashNodeJoin join;

    private final Iterator<PathInterface> childIterator;

    private Integer counterFixPoint = 1;

    public PhysicalOpRecursive(final PhysicalOperator child, final LogicalOpRecursive lop) {
        super(child);
        this.lop = lop;
        
        originalChild = Utils.iterToList(child);
        
        childIterator = originalChild.iterator();
        
        this.loopChild = new LinkedList<>();

        this.join = new PhysicalOpHashNodeJoin(
            new PhysicalOperatorListWrapper(originalChild.iterator()),
            new PhysicalOperatorListWrapper(originalChild.iterator()));

    }

    @Override
    public void acceptVisitor(final PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean hasNext() {
        if ( slot == null ) {
            slot = getNextPathInterface();
            return slot != null;
        }
        return true;
    }

    @Override
    public PathInterface next() {
        final PathInterface r = slot;
        slot = null;
        return r;
    }

    protected PathInterface getNextPathInterface() {
        while (this.childIterator.hasNext()) {
            return this.childIterator.next();
        }

        for ( ;; ) {
            if (this.counterFixPoint >= Context.getInstance().getFixPoint()) {
                return null;
            }
            
            while (this.join.hasNext()) {
                final PathInterface PathInterface = this.join.next();
                if (PathInterface != null) {
                    this.loopChild.add(PathInterface);
                    return PathInterface;
                }
            }
            
            this.counterFixPoint++;
            this.join = new PhysicalOpHashNodeJoin(
                new PhysicalOperatorListWrapper(this.loopChild.iterator()), 
                new PhysicalOperatorListWrapper(this.originalChild.iterator()));
            this.loopChild.clear();
        }
    }
    
}
