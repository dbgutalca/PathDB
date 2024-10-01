package com.gdblab.algebra.queryplan.physical;

import java.util.NoSuchElementException;

import com.gdblab.algebra.queryplan.logical.LogicalOperator;
import com.gdblab.algebra.queryplan.logical.LogicalPlan;

public interface PhysicalPlan {
    /**
     * Returns the root operator of the plan tree
     */
    PhysicalOperator getRootOperator();
}
