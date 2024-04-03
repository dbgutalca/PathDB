package com.gdblab.queryplan.physical.impl;

import java.util.Iterator;
import java.util.List;

import com.gdblab.queryplan.util.Utils;
import com.gdblab.algebra.PathAlgebra;
import com.gdblab.queryplan.logical.impl.LogicalOpNodeJoin;
import com.gdblab.queryplan.physical.PhysicalOperator;
import com.gdblab.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.schema.Path;

public class PhysicalOpBinaryUnion extends BinaryPhysicalOp {

    protected final LogicalOpNodeJoin lop;
    protected Path slot = null;
    protected final List<Path> rightRows;
    protected final List<Path> leftRows;
    private Iterator<Path> right;
    private Iterator<Path> left;
    private Path nextLeft = null;

    public PhysicalOpBinaryUnion(final PhysicalOperator leftChild, final PhysicalOperator rightChild,
                                        final LogicalOpNodeJoin lop) {
        super(leftChild, rightChild);
        this.lop = lop;
        rightRows = Utils.iterToList(rightChild);
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

        // Create a iterator objecto from the right child
        // to return only right children first
        right = rightRows.iterator();

        // Set a flag to know when all right children returns
        boolean rightReady = false;

        for ( ;; ) { 
            // First return all paths from right child
            // Note: when right doesn't has another child
            // activate the ready flag to avoid to enter again to
            // this conditional.
            if ( right.hasNext() && !rightReady) return right.next();
            else rightReady = true;

            // Start to iterate over left child
            if ( nextLeft != null ) {
                // if leftChild has next get the next path
                // and start to iterate over the right child
                if ( leftChild.hasNext() ) {
                    nextLeft = leftChild.next();
                    right = rightRows.iterator();
                }
            }

            // Set a flag to know if a left path exist on
            // the right children
            boolean leftInRight = false;

            // Start to iterate over the right children
            while ( right.hasNext() ) {
                final Path rowRight = right.next();

                // Check if the right row is equal to the left row
                // activating the flag if it's true
                if ( rowRight.equals(nextLeft) ) leftInRight = true;
            }

            // Finally if the current left child doesn't exist on the right children
            // return the path, otherwise return null
            if ( !leftInRight ) return nextLeft;
            else return null;
        }
    }
    
}
