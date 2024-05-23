package com.gdblab.queryplan.physical.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.gdblab.queryplan.logical.impl.LogicalOpNodeJoin;
import com.gdblab.queryplan.logical.impl.LogicalOpRecursive;
import com.gdblab.queryplan.physical.PhysicalOperator;
import com.gdblab.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.queryplan.util.Utils;
import com.gdblab.schema.Path;

public class PhysicalOpRecursive extends UnaryPhysicalOp {

    protected final LogicalOpRecursive lop;
    protected Path slot = null;

    protected final List<Path> originalChild;
    private final List<Path> loopChild;

    private PhysicalOpNestedLoopNodeJoin join;

    private final Iterator<Path> childIterator;

    private Integer counterFixPoint = 1;

    public PhysicalOpRecursive(final PhysicalOperator child, final LogicalOpRecursive lop) {
        super(child);
        this.lop = lop;
        
        originalChild = Utils.iterToList(child);
        
        LinkedList<Path> leftChilds = new LinkedList<>(originalChild);
        LinkedList<Path> rightChilds = new LinkedList<>(originalChild);
        
        childIterator = originalChild.iterator();
        
        this.loopChild = new LinkedList<>();

        this.join = new PhysicalOpNestedLoopNodeJoin(
            new PhysicalOperatorListWrapper(leftChilds.iterator()),
            new PhysicalOperatorListWrapper(rightChilds.iterator()),
            null);

    }

    @Override
    public void acceptVisitor(final PhysicalPlanVisitor visitor) {
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
            final Path p = this.childIterator.next();
            return p;
        }

        for ( ;; ) {
            if (this.counterFixPoint >= 3) {
                return null;
            }
            
            while (this.join.hasNext()) {
                final Path path = this.join.next();
                if (path != null) {
                    this.loopChild.add(path);
                    return path;
                }
            }
            
            this.counterFixPoint++;
            this.join = new PhysicalOpNestedLoopNodeJoin(
                new PhysicalOperatorListWrapper(this.loopChild.iterator()), 
                new PhysicalOperatorListWrapper(this.originalChild.iterator()), 
                null);
            this.loopChild.clear();
        }
    }
    
}
