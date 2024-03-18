package com.gdblab.queryplan.physical.impl;

import com.gdblab.queryplan.exception.IteratorAlreadyConsumedException;
import com.gdblab.queryplan.logical.impl.LogicalOpSelection;
import com.gdblab.queryplan.physical.PhysicalOperator;
import com.gdblab.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.schema.Path;

/**
 * Sequential Scan resolves a brute-force Selection
 */
public class PhysicalOpSequentialScan extends UnaryPhysicalOp {

    protected final LogicalOpSelection lop;
    private Path slot = null;

    public PhysicalOpSequentialScan(final PhysicalOperator child, final LogicalOpSelection lop) {
        super(child);
        this.lop = lop;
    }

    @Override
    public void acceptVisitor(final PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * This method not only checks if there are more results, but also "saves"
     * the next path in the slot variable
     * @return true if there is a new path, or if there is already one stored. False otherwise
     */
    @Override
    public boolean hasNext() {
        if (slot != null) return true;
        if (getChild().hasNext()){
            Path candidate = getChild().next();
            if (lop.getCondition().eval(candidate)){
                slot = candidate;
                return true;
            }
        }
        return false;
    }

    /**
     * Always calls hasNext(). If there's something in the slot after the call,
     * this method returns the slot and restores its value to 0.
     *
     * It must throw an exception if the child does not have more paths. This
     * exception is avoided by manually calling hasNext outside.
     * @return the next path
     */
    @Override
    public Path next() {
        if (hasNext()){
            Path p = slot;
            slot = null;
            return p;
        }
        throw new IteratorAlreadyConsumedException("zzzzzzzz");
    }
}