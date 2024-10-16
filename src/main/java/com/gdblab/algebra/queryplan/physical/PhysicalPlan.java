package com.gdblab.algebra.queryplan.physical;

public interface PhysicalPlan {
    /**
     * Returns the root operator of the plan tree
     */
    PhysicalOperator getRootOperator();
}
