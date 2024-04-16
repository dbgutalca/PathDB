package com.gdblab.queryplan.logical.visitor;

import com.gdblab.queryplan.logical.LogicalPlanVisitor;
import com.gdblab.queryplan.logical.impl.*;

public class LogicalOptimizerVisitor implements LogicalPlanVisitor {

    int height = 0;

    @Override
    public void visit(final LogicalOpSelection logicalOpSelection) {

    }

    @Override
    public void visit(final LogicalOpProjection logicalOpProjection) {

    }

    @Override
    public void visit(final LogicalOpNodeJoin logicalOpNodeJoin) {

    }

    @Override
    public void visit(final LogicalOpEdgeJoin logicalOpEdgeJoin) {

    }

    @Override
    public void visit(final LogicalOpNodeProduct logicalOpNodeProduct) {

    }

    @Override
    public void visit(final LogicalOpEdgeProduct logicalOpEdgeProduct) {

    }

    @Override
    public void visit(final LogicalOpUnion logicalOpUnion) {

    }

    @Override
    public void visit(final LogicalOpIntersection logicalOpIntersection) {

    }

    @Override
    public void visit(final LogicalOpDifference logicalOpDifference) {

    }

    @Override
    public void visit(final LogicalOpAllPathsStartingFromNode logicalOpAllPathsStartingFromNode) {

    }

    @Override
    public void visit(final LogicalOpRecursive logicalOpRecursive) {

    }

    public int getHeight() {
        return height;
    }

}
