package com.gdblab.queryplan.logical;

public interface BinaryLogicalOperator extends LogicalOperator {

    LogicalOperator getLeftChild();

    LogicalOperator getRightChild();

}
