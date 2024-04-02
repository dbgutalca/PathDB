package com.gdblab.queryplan.logical;

import java.util.NoSuchElementException;

public interface LogicalPlan {

    /**
     * Returns the root operator of the plan tree
     */
    LogicalOperator getRootOperator();

    /**
     * Returns the i-th sub-plan of this plan, where i starts at
     * index 0 (zero).
     *
     * If the plan has fewer sub-plans (or no sub-plans at all),
     * then a {@link NoSuchElementException} will be thrown.
     */
    LogicalPlan getSubPlan(final int i) throws NoSuchElementException;
}
