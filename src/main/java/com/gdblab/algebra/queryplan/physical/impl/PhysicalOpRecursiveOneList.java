package com.gdblab.algebra.queryplan.physical.impl;

import java.util.ArrayList;
import java.util.Stack;

import com.gdblab.algebra.queryplan.logical.impl.LogicalOpRecursive;
import com.gdblab.algebra.queryplan.physical.PhysicalOperator;
import com.gdblab.algebra.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.execution.Context;
import com.gdblab.graph.schema.Path;

public class PhysicalOpRecursiveOneList extends UnaryPhysicalOp {

    protected final LogicalOpRecursive lop;

    protected Path slot = null;
    protected Path candidate = null;

    protected ArrayList<Path> childList = new ArrayList<>();

    protected Stack<Integer> indexStack = new Stack<>();

    protected final Integer maxRecursion;
    protected Integer currentRecursion;

    public PhysicalOpRecursiveOneList(final PhysicalOperator child, final LogicalOpRecursive lop) {
        super(child);
        this.maxRecursion = Context.getInstance().getMaxRecursion();
        this.lop = lop;
        this.currentRecursion = 0;
        this.indexStack.push(0);
        saveChildList(child);
    }

    @Override
    public void acceptVisitor(final PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean hasNext() {
        if (this.slot == null) {
            if (this.maxRecursion == 0) {
                return this.calculateNextSlotZero();
            } else {
                return this.calculateNextSlotMore();
            }
        }
        return true;
    }

    @Override
    public Path next() {
        final Path r = slot;
        slot = null;
        return r;
    }

    private void saveChildList(final PhysicalOperator child) {
        while (child.hasNext()) {
            Path p = child.next();
            this.childList.add(p);
        }
    }

    private boolean calculateNextSlotZero() {
        Integer idx = indexStack.pop();
        if (idx >= this.childList.size()) {
            return false;
        }
        this.slot = this.childList.get(idx);
        this.indexStack.push(idx + 1);
        return this.slot != null;
    }

    private boolean calculateNextSlotMore() {
        /**
         * Inicializa solamente la primera vez el candidato. Este candidato es
         * mutable segun se van agregando y quitando aristas. Sirve de base para
         * generar todos los caminos.
         */
        if (this.candidate == null) {
            Integer idx = indexStack.pop();
            this.candidate = this.childList.get(idx);
            this.indexStack.push(idx + 1);
        }

        while (true) {

            if (this.candidate.getEdgeLength() < this.maxRecursion) {

                continue;
            }

            return true;
        }
    }
}
