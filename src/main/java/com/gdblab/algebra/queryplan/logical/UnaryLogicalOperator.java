package com.gdblab.algebra.queryplan.logical;

public interface UnaryLogicalOperator extends LogicalOperator {

    LogicalOperator getChild();

}
