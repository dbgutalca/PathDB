package com.gdblab.queryplan.logical.impl;

import com.gdblab.queryplan.logical.LogicalPlanVisitor;
import com.gdblab.queryplan.logical.NullaryLogicalOperator;
import com.gdblab.schema.impl.MemoryGraph;

public class LogicalOpAllNodes implements NullaryLogicalOperator {
    
    private MemoryGraph graph;

    public LogicalOpAllNodes() {
        this.graph = MemoryGraph.getInstance();
    }
    
    @Override
    public void acceptVisitor(final LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LogicalOpAllNodes;
    }

    @Override
    public String toString() {
        return "S0";
    }

    public MemoryGraph getGraph() {
        return graph;
    }
}
