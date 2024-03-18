package com.gdblab.queryplan.logical;

import com.gdblab.queryplan.logical.impl.*;

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
}
