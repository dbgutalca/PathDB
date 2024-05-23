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

/**
 *
 * @author vroja
 */
public class PhysicalOpAllNodes implements NullaryPhysicalOperator{
    
    private final LogicalOpAllNodes lop;
    private final Iterator<Node> nodes;

    private Path slot;

    public PhysicalOpAllNodes(LogicalOpAllNodes lop) {
        this.lop = lop;
        this.nodes = this.lop.getGraph().getNodeIterator();
    }
    
    @Override
    public void acceptVisitor(PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean hasNext() {
        if (slot != null) return true;
        while (nodes.hasNext()){
            Node node = nodes.next();
            Path p = new Path(node.getId());
            p.insertNode(node);
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
