package com.gdblab.algebra.queryplan.physical.impl;

import java.util.ArrayList;
import java.util.List;

import com.gdblab.algebra.queryplan.logical.impl.LogicalOpRecursive;
import com.gdblab.algebra.queryplan.physical.PhysicalOperator;
import com.gdblab.algebra.queryplan.physical.PhysicalPlanVisitor;
import com.gdblab.algebra.queryplan.util.Utils;
import com.gdblab.execution.Context;
import com.gdblab.graph.schema.Path;

public class PhysicalOpRecursive extends BinaryPhysicalOp {

    protected final LogicalOpRecursive lop;
    protected Path slot = null;
    protected final List<Path> filteredChild = new ArrayList<Path>();
    protected List<Path> notFilteredChild = new ArrayList<Path>();
    protected final List<Path> loopChild = new ArrayList<Path>();

    protected final Integer maxRecursion;
    protected Integer currentRecursion;

    protected BinaryPhysicalOp join;

    public PhysicalOpRecursive(final PhysicalOperator leftChild, final PhysicalOperator rightChild , final LogicalOpRecursive lop) {
        super(leftChild, rightChild);
        this.maxRecursion = Context.getInstance().getMaxRecursion();
        this.lop = lop;
        this.join = null;
        this.currentRecursion = 1;
    }

    @Override
    public void acceptVisitor(final PhysicalPlanVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean hasNext() {
        if ( slot == null ) {
            slot = this.lop.hasLastFilter() ? getNextRightPath() : getNextLeftPath();
            return slot != null;
        }
        return true;
    }

    @Override
    public Path next() {
        if (this.maxRecursion == 0) return null;
        final Path r = slot;
        slot = null;
        return r;
    }

    protected Path getNextRightPath() {
        while (this.rightChild.hasNext()) {
            final Path path = rightChild.next();

            if (path.getEdgeLength() <= Context.getInstance().getFixPoint()) {
                this.filteredChild.add(path);
                return path;
            }
        }

        if (this.join == null) {
            if (this.lop.hasLastFilter()) { this.notFilteredChild = Utils.iterToList(leftChild); }
            else { this.notFilteredChild = Utils.iterToList(rightChild); }

            if (this.currentRecursion == this.maxRecursion) return null;
            this.currentRecursion++;
            
            this.join = new PhysicalOpHashNodeJoin(
                new PhysicalOperatorListWrapper(this.notFilteredChild.iterator()),
                new PhysicalOperatorListWrapper(this.filteredChild.iterator()));
        }

        for ( ;; ) {
            while (this.join.hasNext()) {
                final Path path = this.join.next();

                if (path != null && path.getEdgeLength() <= Context.getInstance().getFixPoint()) {
                    this.loopChild.add(path);
                    return path;
                }
            }

            if (this.loopChild.size() == 0) return null;

            if (this.currentRecursion == this.maxRecursion) return null;
            this.currentRecursion++;

            this.join = new PhysicalOpHashNodeJoin(
                new PhysicalOperatorListWrapper(this.notFilteredChild.iterator()),
                new PhysicalOperatorListWrapper(this.loopChild.iterator()));

            this.loopChild.clear();
        }
    }

    protected Path getNextLeftPath() {
        while (this.leftChild.hasNext()) {
            final Path path = leftChild.next();

            if (path.getEdgeLength() <= Context.getInstance().getFixPoint()) {
                this.filteredChild.add(path);
                return path;
            }
        }

        if (this.join == null) {
            if (this.lop.hasLastFilter()) { this.notFilteredChild = Utils.iterToList(leftChild); }
            else { this.notFilteredChild = Utils.iterToList(rightChild); }

            if (this.currentRecursion == this.maxRecursion) return null;
            this.currentRecursion++;
            
            this.join = new PhysicalOpHashNodeJoin(
                    new PhysicalOperatorListWrapper(this.filteredChild.iterator()),
                    new PhysicalOperatorListWrapper(this.notFilteredChild.iterator()));
        }

        for ( ;; ) {
            while (this.join.hasNext()) {
                final Path path = this.join.next();

                if (path != null && path.getEdgeLength() <= Context.getInstance().getFixPoint()) {
                    this.loopChild.add(path);
                    return path;
                }
            }

            if (this.loopChild.size() == 0) return null;

            if (this.currentRecursion == this.maxRecursion) return null;

            this.currentRecursion++;

            this.join = new PhysicalOpHashNodeJoin(
                    new PhysicalOperatorListWrapper(this.loopChild.iterator()),
                    new PhysicalOperatorListWrapper(this.notFilteredChild.iterator()));
            

            this.loopChild.clear();
        }
    }
    
}
