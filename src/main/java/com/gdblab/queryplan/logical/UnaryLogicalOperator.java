package com.gdblab.queryplan.logical;

public interface UnaryLogicalOperator extends LogicalOperator {

    LogicalOperator getChild();

}
