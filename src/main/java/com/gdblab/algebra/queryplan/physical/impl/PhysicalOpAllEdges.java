/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.algebra.queryplan.physical.impl;

import java.util.Iterator;

import com.gdblab.algebra.queryplan.logical.impl.LogicalOpAllEdges;
import com.gdblab.algebra.queryplan.physical.NullaryPhysicalOperator;
import com.gdblab.algebra.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.execution.Context;
import com.gdblab.graph.impl.Graph;
import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.Path;

/**
 *
 * @author vroja
 */
public class PhysicalOpAllEdges implements NullaryPhysicalOperator{
    
    protected final LogicalOpAllEdges lop;
    private final Iterator<Edge> edges;

    protected Path slot;

    public PhysicalOpAllEdges(LogicalOpAllEdges lop) {
        this.lop = lop;
        this.edges = Graph.getGraph().getEdgeIterator();
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
            Path p = new Path("", edge);

            if (Context.getInstance().getSemantic() == 3) {
                if (p.lenght() > 1 && !p.isSelfAcyclic()) {
                    continue;
                }
            }

            if (Context.getInstance().getSemantic() == 4) {
                if (p.lenght() > 1 && !p.isSelfSimplePath()) {
                    continue;
                }
            }

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
