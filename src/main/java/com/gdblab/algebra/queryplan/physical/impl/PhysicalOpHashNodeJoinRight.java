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

/**
 * Same as PhysicalOpHashNodeJoin, but does the join from right to left.
 * This class is required to optimize the recursive operator when it has
 * a last Filter
 */
public class PhysicalOpHashNodeJoinRight extends BinaryPhysicalOp {

    final HashMap<Node, List<Path>> hashTable = new HashMap<>();
    private Path slot;
    private Path nextLeft = null;
    private Iterator<Path> partialRight = null;

    public PhysicalOpHashNodeJoinRight(final PhysicalOperator leftChild, final PhysicalOperator rightChild) {
        super(leftChild, rightChild);
        // This implementation hashes the right input and probes the left
        // a smarter implementation would hash the smaller input, but we don't have an optimizer yet
        while (rightChild.hasNext()) {
            final Path current = rightChild.next();
            final Node key = current.first();
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
        for ( ;; ) {
            if ( nextLeft == null ) {
                if ( leftChild.hasNext() ) {
                    nextLeft = leftChild.next();
                    partialRight = null;
                } else
                    return null;
            }
            if (partialRight == null) {
                List<Path> right = hashTable.get(nextLeft.last());
                if (right == null) {
                    nextLeft = null;
                    continue;
                }
                partialRight = right.iterator();
            }
            if (partialRight.hasNext()) {
                return PathAlgebra.NodeLink(nextLeft, partialRight.next()); // no need to check if null
            }
            nextLeft = null;
        }
    }

    @Override
    public Path next() {
        final Path r = slot;
        slot = null;
        return r;
    }
}

