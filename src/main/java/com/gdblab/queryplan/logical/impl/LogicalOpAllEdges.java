package com.gdblab.queryplan.logical.impl;

import com.gdblab.queryplan.logical.LogicalPlanVisitor;
import com.gdblab.queryplan.logical.NullaryLogicalOperator;
import com.gdblab.schema.impl.MemoryGraph;

public class LogicalOpAllEdges implements NullaryLogicalOperator {
    
    private MemoryGraph graph;

    public LogicalOpAllEdges() {
        this.graph = MemoryGraph.getInstance();
    }
    
    @Override
    public void acceptVisitor(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LogicalOpAllEdges;
    }

    @Override
    public String toString() {
        return "S1";
    }

    public MemoryGraph getGraph() {
        return graph;
    }

    public void setGraph(MemoryGraph graph) {
        this.graph = graph;
    }
}
