package com.gdblab.algebra.queryplan.physical;

import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpAllEdges;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpAllNodes;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpBFSAllPathsFromNode;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpBinaryUnion;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpHashNodeJoin;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpHashNodeJoinRight;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpNestedLoopNodeJoin;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpRecursive;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpRecursiveOneList;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpReverse;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpSelectionByLabel;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOpSequentialScan;
import com.gdblab.algebra.queryplan.physical.impl.PhysicalOperatorListWrapper;

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

    void visit(final PhysicalOpHashNodeJoinRight physicalOpHashNodeJoinRight);

    void visit(final PhysicalOpSelectionByLabel physicalOpSelection);

    void visit(final PhysicalOpRecursiveOneList physicalOpRecursiveOneList);
}
