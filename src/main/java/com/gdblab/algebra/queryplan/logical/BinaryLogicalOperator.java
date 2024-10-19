package com.gdblab.algebra.queryplan.logical;

public interface BinaryLogicalOperator extends LogicalOperator {

    LogicalOperator getLeftChild();

    LogicalOperator getRightChild();

}
