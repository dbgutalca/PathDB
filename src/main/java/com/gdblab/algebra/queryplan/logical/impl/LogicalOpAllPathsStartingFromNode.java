package com.gdblab.algebra.queryplan.logical.impl;

import com.gdblab.schema.impl.MemoryGraph;
import com.gdblab.algebra.queryplan.logical.LogicalPlanVisitor;
import com.gdblab.algebra.queryplan.logical.NullaryLogicalOperator;
import com.gdblab.schema.Node;

public class LogicalOpAllPathsStartingFromNode implements NullaryLogicalOperator {

    private final MemoryGraph graph;
    private final Node start;

    public LogicalOpAllPathsStartingFromNode(final MemoryGraph graph, final Node start) {
        this.graph = graph;
        this.start = start;
    }

    @Override
    public void acceptVisitor(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }
}
