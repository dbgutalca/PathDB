package com.gdblab.algebra.queryplan.logical;

public interface LogicalOperator {

    /**
     * Receives a Logical Plan visitor that traverses the logical plan tree
     */
    void acceptVisitor(final LogicalPlanVisitor visitor );

}
