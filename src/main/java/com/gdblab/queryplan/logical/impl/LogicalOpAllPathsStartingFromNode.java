package com.gdblab.queryplan.logical.impl;

import com.gdblab.queryplan.logical.LogicalPlanVisitor;
import com.gdblab.queryplan.logical.NullaryLogicalOperator;
import com.gdblab.schema.Graph;
import com.gdblab.schema.Node;

public class LogicalOpAllPathsStartingFromNode implements NullaryLogicalOperator {

    private final Graph graph;
    private final Node start;

    public LogicalOpAllPathsStartingFromNode(final Graph graph, final Node start) {
        this.graph = graph;
        this.start = start;
    }

    @Override
    public void acceptVisitor(LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }
}
