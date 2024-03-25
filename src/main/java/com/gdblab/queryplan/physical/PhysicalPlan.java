package com.gdblab.queryplan.physical;

import com.gdblab.queryplan.logical.LogicalOperator;
import com.gdblab.queryplan.logical.LogicalPlan;

import java.util.NoSuchElementException;

public interface PhysicalPlan {
    /**
     * Returns the root operator of the plan tree
     */
    PhysicalOperator getRootOperator();
}
