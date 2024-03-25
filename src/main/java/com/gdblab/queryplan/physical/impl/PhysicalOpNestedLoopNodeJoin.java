package com.gdblab.queryplan.physical.impl;

import com.gdblab.algebra.PathAlgebra;
import com.gdblab.queryplan.logical.impl.LogicalOpNodeJoin;
import com.gdblab.queryplan.physical.PhysicalOperator;
import com.gdblab.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.queryplan.util.Utils;
import com.gdblab.schema.Path;

import java.util.Iterator;
import java.util.List;

public class PhysicalOpNestedLoopNodeJoin extends BinaryPhysicalOp{

    protected final LogicalOpNodeJoin lop;
    protected Path slot = null;
    protected final List<Path> leftRows;
    private Iterator<Path> left;
    private Path nextRight = null;

    public PhysicalOpNestedLoopNodeJoin(final PhysicalOperator leftChild, final PhysicalOperator rightChild,
                                        final LogicalOpNodeJoin lop) {
        super(leftChild, rightChild);
        this.lop = lop;
        leftRows = Utils.iterToList(leftChild);
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

    /**
     * Code taken from Apache Jena. It gets the next match, or null if there isn't one.
     */
    protected Path moveToNextPathOrNull() {
        for ( ;; ) { // For rows from the right.
            if ( nextRight == null ) {
                if ( rightChild.hasNext() ) {
                    nextRight = rightChild.next();
                    left = leftRows.iterator();
                } else
                    return null;
            }

            // There is a rowRight
            while (left.hasNext()) {
                final Path rowLeft = left.next();
                final Path r = PathAlgebra.NodeLink(rowLeft, nextRight);//Algebra.merge(rowLeft, rowRight);
                if ( r != null ) {
                    return r;
                }
            }
            // Nothing more for this rowRight.
            nextRight = null;
        }
    }

}
