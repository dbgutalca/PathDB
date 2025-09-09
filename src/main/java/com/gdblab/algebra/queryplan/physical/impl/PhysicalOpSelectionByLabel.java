package com.gdblab.algebra.queryplan.physical.impl;

import java.util.Iterator;

import com.gdblab.algebra.condition.Label;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpSelectionByLabel;
import com.gdblab.algebra.queryplan.physical.NullaryPhysicalOperator;
import com.gdblab.algebra.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.graph.impl.Graph;
import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.Path;

public class PhysicalOpSelectionByLabel implements NullaryPhysicalOperator{

    protected final LogicalOpSelectionByLabel lop;
    protected Path slot;

    private final Iterator<Edge> edges;

    public PhysicalOpSelectionByLabel(LogicalOpSelectionByLabel lop) {
        this.lop = lop;
        this.edges = Graph.getGraph().getEdgeIteratorByLabel(((Label) lop.getCondition()).getLabel());
    }

    @Override
    public void acceptVisitor(PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean hasNext() {
        
        if (slot != null) return true;

        while (edges.hasNext()) {
            slot = new Path("", edges.next());
            return true;
        }
        
        return false;
    }

    @Override
    public Path next() {
        if (hasNext()) {
            final Path p = slot;
            slot = null;
            return p;
        }

        return null;
    }
    
}
