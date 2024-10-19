package com.gdblab.algebra.queryplan.physical;

import java.util.Iterator;

import com.gdblab.graph.schema.Path;

/**
 * PhysicalIterators represent both the algorithm to be executed and the result iterator, which
 * lazily consume the iterator of the child physical operators and apply the corresponding algorithm
 */
public interface PhysicalOperator extends Iterator<Path> {

    void acceptVisitor(final PhysicalPlanVisitor visitor);

}
