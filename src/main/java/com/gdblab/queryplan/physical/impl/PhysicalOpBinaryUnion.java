package com.gdblab.queryplan.physical.impl;

import java.util.Iterator;
import java.util.List;

import com.gdblab.queryplan.util.Utils;
import com.gdblab.queryplan.logical.impl.LogicalOpNodeJoin;
import com.gdblab.queryplan.logical.impl.LogicalOpUnion;
import com.gdblab.queryplan.physical.PhysicalOperator;
import com.gdblab.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.schema.Path;
import java.util.LinkedList;

public class PhysicalOpBinaryUnion extends BinaryPhysicalOp {

    protected final LogicalOpUnion lop;
    protected Path slot = null;
    protected final List<Path> leftRows = new LinkedList<>();
    private Iterator<Path> left;
    private Path nextRight = null;

    public PhysicalOpBinaryUnion(final PhysicalOperator leftChild, final PhysicalOperator rightChild,
                                        final LogicalOpUnion lop) {
        super(leftChild, rightChild);
        this.lop = lop;
    }

    @Override
    public void acceptVisitor(final PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean hasNext() {
        if ( slot == null ) {
            slot = moveToNextPathOrNull();
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

    protected Path moveToNextPathOrNull() {
        while (this.leftChild.hasNext()) {
            final Path p = this.leftChild.next();
            this.leftRows.add(p);
            return p;
        }
        
        while (this.rightChild.hasNext() ) {
            this.nextRight = this.rightChild.next();
            
            if (!this.leftRows.contains(this.nextRight)) {
                return this.nextRight;
            }
            
            return null;
        }

        return null;
    }

}
