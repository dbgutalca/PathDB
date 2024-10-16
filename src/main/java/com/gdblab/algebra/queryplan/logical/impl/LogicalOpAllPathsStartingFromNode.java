package com.gdblab.algebra.queryplan.logical.impl;

import com.gdblab.algebra.queryplan.logical.LogicalPlanVisitor;
import com.gdblab.algebra.queryplan.logical.NullaryLogicalOperator;
import com.gdblab.graph.impl.CSRVPMin;
import com.gdblab.graph.schema.Node;

public class LogicalOpAllPathsStartingFromNode implements NullaryLogicalOperator {

    private final CSRVPMin graph;
    private final Node start;

    public LogicalOpAllPathsStartingFromNode(final CSRVPMin graph, final Node start) {
        this.graph = graph;
        this.start = start;
    }

    @Override
    public void acceptVisitor(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }
}
