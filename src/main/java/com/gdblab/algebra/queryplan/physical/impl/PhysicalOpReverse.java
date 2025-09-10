package com.gdblab.algebra.queryplan.physical.impl;

import com.gdblab.algebra.queryplan.logical.impl.LogicalOpReverse;
import com.gdblab.algebra.queryplan.physical.PhysicalOperator;
import com.gdblab.algebra.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.graph.schema.Path;

public class PhysicalOpReverse extends UnaryPhysicalOp {

    protected final LogicalOpReverse lop;
    protected Path slot = null;
    
    public PhysicalOpReverse(final PhysicalOperator child, final LogicalOpReverse lop) {
        super(child);
        
        this.lop = lop;
    }

    @Override
    public void acceptVisitor(PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean hasNext() {
        if ( slot != null ) return true;
        
        while ( getChild().hasNext() ){
            Path next = getChild().next();
            slot = new Path("", true, next.getSequence(), next.getLength());
            return true;
        }
        
        return false;
    }

    @Override
    public Path next() {
        final Path r = slot;
        slot = null;
        return r;
    }

}
