package com.gdblab.queryplan.physical;

import com.gdblab.queryplan.physical.impl.PhysicalOpNestedLoopNodeJoin;
import com.gdblab.queryplan.physical.impl.PhysicalOpSequentialScan;

public interface PhysicalPlanVisitor {

    void visit(final PhysicalOpSequentialScan physicalOpSequentialScan);

    void visit(final PhysicalOpNestedLoopNodeJoin physicalOpNestedLoopNodeJoin);
}
