package com.gdblab.queryplan.physical;

import com.gdblab.queryplan.physical.impl.PhysicalOpBFSAllPathsFromNode;
import com.gdblab.queryplan.physical.impl.PhysicalOpBinaryUnion;
import com.gdblab.queryplan.physical.impl.PhysicalOpNestedLoopNodeJoin;
import com.gdblab.queryplan.physical.impl.PhysicalOpOneOrMore;
import com.gdblab.queryplan.physical.impl.PhysicalOpSequentialScan;

public interface PhysicalPlanVisitor {

    void visit(final PhysicalOpSequentialScan physicalOpSequentialScan);

    void visit(final PhysicalOpNestedLoopNodeJoin physicalOpNestedLoopNodeJoin);

    void visit(final PhysicalOpBFSAllPathsFromNode physicalOpBFSAllPathsFromNode);

    void visit(final PhysicalOpBinaryUnion physicalOpBinaryUnion);

    void visit(final PhysicalOpOneOrMore physicalOpOneOrMore);
}
