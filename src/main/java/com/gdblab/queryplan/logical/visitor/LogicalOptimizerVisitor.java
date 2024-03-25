package com.gdblab.queryplan.logical.visitor;

import com.gdblab.queryplan.logical.LogicalPlanVisitor;
import com.gdblab.queryplan.logical.impl.*;

public class LogicalOptimizerVisitor implements LogicalPlanVisitor {

    int height = 0;

    @Override
    public void visit(final LogicalOpSelection logicalOpSelection) {

    }

    @Override
    public void visit(LogicalOpProjection logicalOpProjection) {

    }

    @Override
    public void visit(LogicalOpNodeJoin logicalOpNodeJoin) {

    }

    @Override
    public void visit(LogicalOpEdgeJoin logicalOpEdgeJoin) {

    }

    @Override
    public void visit(LogicalOpNodeProduct logicalOpNodeProduct) {

    }

    @Override
    public void visit(LogicalOpEdgeProduct logicalOpEdgeProduct) {

    }

    @Override
    public void visit(LogicalOpUnion logicalOpUnion) {

    }

    @Override
    public void visit(LogicalOpIntersection logicalOpIntersection) {

    }

    @Override
    public void visit(LogicalOpDifference logicalOpDifference) {

    }

    @Override
    public void visit(LogicalOpAllPathsStartingFromNode logicalOpAllPathsStartingFromNode) {

    }

    public int getHeight() {
        return height;
    }

}
