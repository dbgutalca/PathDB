package com.gdblab.queryplan.logical.impl;

import com.gdblab.queryplan.logical.LogicalPlanVisitor;

public class LogicalOpOneOrMore extends UnaryLogicalOp {

    @Override
    public void acceptVisitor(LogicalPlanVisitor visitor) {
        visitor.visit(this);
    }
    
}
