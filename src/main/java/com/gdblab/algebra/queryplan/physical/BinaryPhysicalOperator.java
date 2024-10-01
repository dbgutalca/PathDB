package com.gdblab.algebra.queryplan.physical;

public interface BinaryPhysicalOperator extends PhysicalOperator{

    PhysicalOperator getLeftChild();
    PhysicalOperator getRightChild();

}
