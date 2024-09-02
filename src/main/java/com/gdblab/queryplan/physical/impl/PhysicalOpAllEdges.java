/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.queryplan.physical.impl;

import java.util.Iterator;

import com.gdblab.execution.Context;
import com.gdblab.queryplan.logical.impl.LogicalOpAllEdges;
import com.gdblab.queryplan.physical.NullaryPhysicalOperator;
import com.gdblab.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.schema.Edge;
import com.gdblab.schema.Path;
import java.util.UUID;

/**
 *
 * @author vroja
 */
public class PhysicalOpAllEdges implements NullaryPhysicalOperator{
    
    private final LogicalOpAllEdges lop;
    private final Iterator<Edge> edges;

    private Path slot;

    public PhysicalOpAllEdges(LogicalOpAllEdges lop) {
        this.lop = lop;
        this.edges = Context.getInstance().getGraph().getEdgeIterator();
    }

    @Override
    public void acceptVisitor(PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean hasNext() {
        if (slot != null) return true;
        while (edges.hasNext()){
            Edge edge = edges.next();
            Path p = new Path(edge);
            slot = p;
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
