package com.gdblab.queryplan.physical;

import com.gdblab.schema.Path;

import java.util.Iterator;

/**
 * PhysicalIterators represent both the algorithm to be executed and the result iterator, which
 * lazily consume the iterator of the child physical operators and apply the corresponding algorithm
 */
public interface PhysicalOperator extends Iterator<Path> {

    void acceptVisitor(final PhysicalPlanVisitor visitor);

}
