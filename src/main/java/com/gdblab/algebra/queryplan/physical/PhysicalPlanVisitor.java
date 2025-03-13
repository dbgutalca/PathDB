package com.gdblab.algebra.queryplan.physical;

import com.gdblab.algebra.queryplan.physical.impl.*;

public interface PhysicalPlanVisitor {

    void visit(final PhysicalOpSequentialScan physicalOpSequentialScan);

    void visit(final PhysicalOpNestedLoopNodeJoin physicalOpNestedLoopNodeJoin);

    void visit(final PhysicalOpBFSAllPathsFromNode physicalOpBFSAllPathsFromNode);

    void visit(final PhysicalOpBinaryUnion physicalOpBinaryUnion);

    void visit(final PhysicalOpRecursive physicalOpRecursive);

    void visit(final PhysicalOperatorListWrapper physicalOperatorListWrapper);
    
    void visit(final PhysicalOpAllEdges physicalOperatorAllEdges);
    
    void visit(final PhysicalOpAllNodes physicalOperatorAllNodes);

    void visit(final PhysicalOpHashNodeJoin physicalOpHashNodeJoin);

    void visit(final PhysicalOpReverse physicalOpReverse);

    void visit(PhysicalOpHashNodeJoinRight physicalOpHashNodeJoinRight);

    void visit(final PhysicalOpSelectionByLabel physicalOpSelection);
}
