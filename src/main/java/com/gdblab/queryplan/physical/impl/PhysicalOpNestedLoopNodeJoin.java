package com.gdblab.queryplan.physical.impl;

import com.gdblab.algebra.PathAlgebra;
import com.gdblab.queryplan.logical.impl.LogicalOpNodeJoin;
import com.gdblab.queryplan.physical.PhysicalOperator;
import com.gdblab.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.queryplan.util.Utils;
import com.gdblab.schema.Node;
import com.gdblab.schema.PathInterface;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class PhysicalOpNestedLoopNodeJoin extends BinaryPhysicalOp{

    protected final LogicalOpNodeJoin lop;
    protected PathInterface slot = null;
    protected final List<PathInterface> leftRows;
    private Iterator<PathInterface> left;
    private PathInterface nextRight = null;
    private boolean firstMatch = false;
    

    public PhysicalOpNestedLoopNodeJoin(final PhysicalOperator leftChild, final PhysicalOperator rightChild,
                                        final LogicalOpNodeJoin lop) {
        super(leftChild, rightChild);
        this.lop = lop;
        leftRows = Utils.iterToList(leftChild);
        
        Collections.sort(leftRows, new Comparator<PathInterface>() {
            @Override
            public int compare(PathInterface o1, PathInterface o2) {
                Node lastP1 = o1.last();
                Node lastP2 = o2.last();
                return lastP1.getId().compareTo(lastP2.getId());
            }
        });


        
    }

    @Override
    public void acceptVisitor(final PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean hasNext() {
        if ( slot == null ) {
            slot = moveToNextPathInterfaceOrNull2();
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


    // Poda el bloque final que no corresponde una vez ordenado el leftRows
    protected PathInterface moveToNextPathInterfaceOrNull2() {
        for ( ;; ) { // For rows from the right.
            if ( nextRight == null ) {
                if ( rightChild.hasNext() ) {
                    nextRight = rightChild.next();
                    left = leftRows.iterator();
                    firstMatch = false;
                } else
                    return null;
            }

            // There is a rowRight
            while (left.hasNext()) {
                final PathInterface rowLeft = left.next();
                final PathInterface r = rowLeft.join(nextRight); //Algebra.merge(rowLeft, rowRight);
                if ( r != null ) {
                    if (!firstMatch) firstMatch = true;
                    return r;
                }
                else if( r == null && firstMatch) break;
            }
            // Nothing more for this rowRight.
            nextRight = null;
        }
    }

    /**
     * Code taken from Apache Jena. It gets the next match, or null if there isn't one.
     */
    protected PathInterface moveToNextPathInterfaceOrNull() {
        for ( ;; ) { // For rows from the right.
            if ( nextRight == null ) {
                if ( rightChild.hasNext() ) {
                    nextRight = rightChild.next();

                    List<PathInterface> filteredLeftRows = filterLeftRowsByRightNode(nextRight.first());
                    if (filteredLeftRows.size() > 0) {
                        left = filteredLeftRows.iterator();
                    }
                    
                } else
                    return null;
            }

            // There is a rowRight
            while (left.hasNext()) {
                final PathInterface rowLeft = left.next();
                final PathInterface r = rowLeft.join(nextRight); //Algebra.merge(rowLeft, rowRight);
                if ( r != null ) {
                    return r;
                }
            }
            // Nothing more for this rowRight.
            nextRight = null;
        }
    }

    private List<PathInterface> filterLeftRowsByRightNode(Node n) {
        return leftRows.stream().filter( PathInterface -> { return PathInterface.last().equals(n); })
        .collect(Collectors.toList());
    }

}
