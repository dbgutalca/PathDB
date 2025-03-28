package com.gdblab.algebra.queryplan.logical;

import com.gdblab.algebra.queryplan.logical.impl.*;

/**
 * Visits all possible types of {@link LogicalOperator}
 */
public interface LogicalPlanVisitor {

    void visit(final LogicalOpSelection logicalOpSelection);

    void visit(final LogicalOpProjection logicalOpProjection);

    void visit(final LogicalOpNodeJoin logicalOpNodeJoin);

    void visit(final LogicalOpEdgeJoin logicalOpEdgeJoin);

    void visit(final LogicalOpNodeProduct logicalOpNodeProduct);

    void visit(final LogicalOpEdgeProduct logicalOpEdgeProduct);

    void visit(final LogicalOpUnion logicalOpUnion);

    void visit(final LogicalOpIntersection logicalOpIntersection);

    void visit(final LogicalOpDifference logicalOpDifference);

    void visit(final LogicalOpAllPathsStartingFromNode logicalOpAllPathsStartingFromNode);

    void visit(final LogicalOpRecursive logicalOpRecursive);

    void visit(final LogicalOpAllNodes logicalOpAllNodes);

    void visit(final LogicalOpAllEdges logicalOpAllEdges);

    void visit(final LogicalOpReverse logicalOpReverse);

    void visit(final LogicalOpSelectionByLabel logicalOpSelectionByLabel);

}
