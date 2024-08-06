package com.gdblab.queryplan.physical.impl;

import com.gdblab.algebra.PathAlgebra;
import com.gdblab.queryplan.logical.impl.LogicalOpNodeJoin;
import com.gdblab.queryplan.physical.PhysicalOperator;
import com.gdblab.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.queryplan.util.Utils;
import com.gdblab.schema.Node;
import com.gdblab.schema.Path;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class PhysicalOpNestedLoopNodeJoin extends BinaryPhysicalOp{

    protected final LogicalOpNodeJoin lop;
    protected Path slot = null;
    protected final List<Path> leftRows;
    private Iterator<Path> left;
    private Path nextRight = null;
    private boolean firstMatch = false;
    

    public PhysicalOpNestedLoopNodeJoin(final PhysicalOperator leftChild, final PhysicalOperator rightChild,
                                        final LogicalOpNodeJoin lop) {
        super(leftChild, rightChild);
        this.lop = lop;
        leftRows = Utils.iterToList(leftChild);
        
        Collections.sort(leftRows, new Comparator<Path>() {
            @Override
            public int compare(Path o1, Path o2) {
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
            slot = moveToNextPathOrNull2();
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


    // Poda el bloque final que no corresponde una vez ordenado el leftRows
    protected Path moveToNextPathOrNull2() {
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
                final Path rowLeft = left.next();
                final Path r = PathAlgebra.NodeLink(rowLeft, nextRight); //Algebra.merge(rowLeft, rowRight);
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
    protected Path moveToNextPathOrNull() {
        for ( ;; ) { // For rows from the right.
            if ( nextRight == null ) {
                if ( rightChild.hasNext() ) {
                    nextRight = rightChild.next();

                    List<Path> filteredLeftRows = filterLeftRowsByRightNode(nextRight.first());
                    if (filteredLeftRows.size() > 0) {
                        left = filteredLeftRows.iterator();
                    }
                    
                } else
                    return null;
            }

            // There is a rowRight
            while (left.hasNext()) {
                final Path rowLeft = left.next();
                final Path r = PathAlgebra.NodeLink(rowLeft, nextRight); //Algebra.merge(rowLeft, rowRight);
                if ( r != null ) {
                    return r;
                }
            }
            // Nothing more for this rowRight.
            nextRight = null;
        }
    }

    private List<Path> filterLeftRowsByRightNode(Node n) {
        return leftRows.stream().filter( path -> { return path.last().equals(n); })
        .collect(Collectors.toList());
    }

}
