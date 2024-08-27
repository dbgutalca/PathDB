package com.gdblab.queryplan.physical.impl;

import com.gdblab.queryplan.logical.impl.LogicalOpReverse;
import com.gdblab.queryplan.physical.PhysicalOperator;
import com.gdblab.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.schema.Path;
import java.util.Iterator;
import java.util.UUID;

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
            slot = new Path( UUID.randomUUID().toString(), true, getChild().next().getSequence() );
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
