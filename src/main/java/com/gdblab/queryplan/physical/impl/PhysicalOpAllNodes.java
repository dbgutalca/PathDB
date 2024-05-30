/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gdblab.queryplan.physical.impl;

import java.util.Iterator;

import com.gdblab.queryplan.logical.impl.LogicalOpAllNodes;
import com.gdblab.queryplan.physical.NullaryPhysicalOperator;
import com.gdblab.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.schema.Node;
import com.gdblab.schema.Path;
import com.gdblab.schema.impl.MemoryGraph;

/**
 *
 * @author vroja
 */
public class PhysicalOpAllNodes implements NullaryPhysicalOperator{
    
    private final LogicalOpAllNodes lop;
    private final Iterator<Node> nodes;

    private Path slot;

    public PhysicalOpAllNodes(final LogicalOpAllNodes lop) {
        this.lop = lop;
        this.nodes = MemoryGraph.getInstance().getNodeIterator();
    }
    
    @Override
    public void acceptVisitor(final PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean hasNext() {
        if (slot != null) return true;
        while (nodes.hasNext()){
            final Node node = nodes.next();
            final Path p = new Path(node.getId(), node);
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
