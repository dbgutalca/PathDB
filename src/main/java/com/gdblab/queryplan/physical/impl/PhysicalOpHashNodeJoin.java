package com.gdblab.queryplan.physical.impl;

import com.gdblab.algebra.PathAlgebra;
import com.gdblab.queryplan.physical.PhysicalOperator;
import com.gdblab.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.schema.Node;
import com.gdblab.schema.Path;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PhysicalOpHashNodeJoin extends BinaryPhysicalOp {

    final HashMap<Node, List<Path>> hashTable = new HashMap<>();
    private Path slot;
    private Path nextRight = null;
    private Iterator<Path> partialLeft = null;

    public PhysicalOpHashNodeJoin(final PhysicalOperator leftChild, final PhysicalOperator rightChild) {
        super(leftChild, rightChild);
        // This implementation hashes the left input and probes the right
        // a smarter implementation would hash the smaller input, but we don't have an optimizer yet
        while (leftChild.hasNext()) {
            final Path current = leftChild.next();
            final Node key = current.last();
            if (!hashTable.containsKey(key)) {
                hashTable.put(key, new LinkedList<>());
            }
            hashTable.get(key).add(current);
        }
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

    private Path moveToNextPathOrNull() {
        for ( ;; ) { // For rows from the right.
            if ( nextRight == null ) {
                if ( rightChild.hasNext() ) {
                    nextRight = rightChild.next();
                } else
                    return null;
            }
            if (partialLeft == null) {
                List <Path> left = hashTable.get(nextRight.first());
                if (left == null) {
                    nextRight = null;
                    continue;
                }
                partialLeft = left.iterator();
            }

            // There is a rowRight
            if (partialLeft.hasNext()) {
                return PathAlgebra.NodeLink(partialLeft.next(), nextRight); // no need to check if null
            }
            // Nothing more for this rowRight.
            nextRight = null;
        }
    }

    @Override
    public Path next() {
        final Path r = slot;
        slot = null;
        return r;
    }
}
