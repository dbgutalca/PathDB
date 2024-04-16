package com.gdblab.queryplan.physical.impl;

import java.util.Iterator;
import java.util.List;

import com.gdblab.queryplan.util.Utils;
import com.gdblab.queryplan.logical.impl.LogicalOpNodeJoin;
import com.gdblab.queryplan.physical.PhysicalOperator;
import com.gdblab.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.schema.Path;

public class PhysicalOpBinaryUnion extends BinaryPhysicalOp {

    protected final LogicalOpNodeJoin lop;
    protected Path slot = null;
    protected final List<Path> leftRows;
    private Iterator<Path> left;
    private Path nextRight = null;

    public PhysicalOpBinaryUnion(final PhysicalOperator leftChild, final PhysicalOperator rightChild,
                                        final LogicalOpNodeJoin lop) {
        super(leftChild, rightChild);
        this.lop = lop;
        leftRows = Utils.iterToList(leftChild);
    }

    @Override
    public void acceptVisitor(PhysicalPlanVisitor visitor) {
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
        while (leftChild.hasNext()) {
            return leftChild.next();
        }
        
        while  (rightChild.hasNext() ) {
            nextRight = rightChild.next();
            
            // Cambiar al reves
            if (!leftRows.contains(nextRight)) {
                return nextRight;
            }
        }

        return null;
    }

}
