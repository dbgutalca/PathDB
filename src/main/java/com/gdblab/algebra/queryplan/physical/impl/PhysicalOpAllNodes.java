/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.algebra.queryplan.physical.impl;

import java.util.Iterator;

import com.gdblab.algebra.queryplan.logical.impl.LogicalOpAllNodes;
import com.gdblab.algebra.queryplan.physical.NullaryPhysicalOperator;
import com.gdblab.algebra.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.graph.Graph;
import com.gdblab.graph.schema.Path;

/**
 *
 * @author vroja
 */
public class PhysicalOpAllNodes implements NullaryPhysicalOperator{
    
    protected final LogicalOpAllNodes lop;
    private final Iterator<String> nodes;

    protected Path slot;

    public PhysicalOpAllNodes(final LogicalOpAllNodes lop) {
        this.lop = lop;
        this.nodes = Graph.getGraph().getNodeIterator();
    }
    
    @Override
    public void acceptVisitor(final PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean hasNext() {
        if (slot != null) return true;
        while (nodes.hasNext()){
            slot = new Path(nodes.next());
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
