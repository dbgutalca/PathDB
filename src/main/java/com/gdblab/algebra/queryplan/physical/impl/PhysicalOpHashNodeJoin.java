package com.gdblab.algebra.queryplan.physical.impl;

import com.gdblab.algebra.queryplan.logical.impl.LogicalOpNodeJoin;
import com.gdblab.algebra.queryplan.physical.PhysicalOperator;
import com.gdblab.algebra.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.algebra.queryplan.util.Utils;
import com.gdblab.graph.schema.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PhysicalOpHashNodeJoin extends BinaryPhysicalOp {

    final HashMap<String, List<Path>> hashTable = new HashMap<>();
    private Path slot;
    private Path nextRight = null;
    private Iterator<Path> partialLeft = null;

    public PhysicalOpHashNodeJoin(final PhysicalOperator leftChild, final PhysicalOperator rightChild, final LogicalOpNodeJoin logicalOpNodeJoin) {
        super(leftChild, rightChild);
        // This implementation hashes the left input and probes the right
        // a smarter implementation would hash the smaller input, but we don't have an optimizer yet
        while (leftChild.hasNext()) {
            final Path current = leftChild.next();
            final String key = current.getLast();
            
            if (!hashTable.containsKey(key)) hashTable.put(key, new ArrayList<>()); 
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
                    partialLeft = null;
                } else
                    return null;
            }
            if (partialLeft == null) {
                List<Path> left = hashTable.get(nextRight.getFirst());
                if (left == null) {
                    nextRight = null;
                    continue;
                }
                partialLeft = left.iterator();
            }
            if (partialLeft.hasNext()) {
                Path pl = partialLeft.next();
                Path result =  Utils.NodeLink(pl, nextRight);
                if (result == null) {
                    continue;
                }
                return result;
            }
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
