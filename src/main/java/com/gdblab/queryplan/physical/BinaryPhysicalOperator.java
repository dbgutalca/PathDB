package com.gdblab.queryplan.physical;

public interface BinaryPhysicalOperator extends PhysicalOperator{

    PhysicalOperator getLeftChild();
    PhysicalOperator getRightChild();

}
