package com.gdblab.queryplan.physical;

public interface UnaryPhysicalOperator extends PhysicalOperator{

    PhysicalOperator getChild();

}
