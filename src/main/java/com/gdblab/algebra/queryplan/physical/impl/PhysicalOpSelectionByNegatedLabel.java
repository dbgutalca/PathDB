package com.gdblab.algebra.queryplan.physical.impl;

import java.util.Iterator;

import com.gdblab.algebra.condition.Label;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpSelectionByNegatedLabel;
import com.gdblab.algebra.queryplan.physical.NullaryPhysicalOperator;
import com.gdblab.algebra.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.graph.Graph;
import com.gdblab.graph.schema.Path;

public class PhysicalOpSelectionByNegatedLabel implements NullaryPhysicalOperator {

    protected final LogicalOpSelectionByNegatedLabel lop;
    protected Path slot;

    private final Iterator<String> edges;

    public PhysicalOpSelectionByNegatedLabel(LogicalOpSelectionByNegatedLabel lop) {
        this.lop = lop;
        this.edges = Graph.getGraph().getEdgeIteratorByNegatedLabel(((Label) lop.getCondition()).getLabel());
    }

    @Override
    public void acceptVisitor(PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean hasNext() {

        if (slot != null)
            return true;

        while (edges.hasNext()) {
            String[] edgeData = edges.next().split("\\|");
            slot = new Path(edgeData[0], edgeData[1], edgeData[2]);
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