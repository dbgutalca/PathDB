package com.gdblab.algebra.queryplan.physical;

public interface UnaryPhysicalOperator extends PhysicalOperator{

    PhysicalOperator getChild();

}
