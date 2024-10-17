package com.gdblab.visitor;

import com.gdblab.algebra.condition.First;
import com.gdblab.algebra.condition.Label;
import com.gdblab.algebra.condition.Last;
import com.gdblab.queryplan.logical.LogicalOperator;
import com.gdblab.queryplan.logical.impl.*;
import com.gdblab.queryplan.logical.visitor.PredicatePushdownLogicalPlanVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PredicatePushdownTest {

    @Test
    public void doesNothingTest() {

        LogicalOperator lop = new LogicalOpUnion(
                new LogicalOpNodeJoin(
                        new LogicalOpSelection(new LogicalOpAllEdges(), new Label("a",1)),
                        new LogicalOpAllNodes()),
                new LogicalOpRecursive(new LogicalOpSelection(new LogicalOpAllEdges(), new Label("b", 1))));
        PredicatePushdownLogicalPlanVisitor visitor = new PredicatePushdownLogicalPlanVisitor();
        lop.acceptVisitor(visitor);
        assertEquals(lop, visitor.getRoot());
    }

    @Test
    public void pushFirstTest() {

        LogicalOperator lop = new LogicalOpSelection(
                    new LogicalOpUnion(
                    new LogicalOpNodeJoin(
                            new LogicalOpSelection(new LogicalOpAllEdges(), new Label("a",1)),
                            new LogicalOpAllNodes()),
                    new LogicalOpRecursive(new LogicalOpSelection(new LogicalOpAllEdges(), new Label("b", 1))))
                , new First("X"));
        LogicalOperator expected = new LogicalOpUnion(
                new LogicalOpNodeJoin(
                        new LogicalOpSelection(
                                new LogicalOpSelection(
                                        new LogicalOpAllEdges(), new First("X")
                                ), new Label("a", 1)
                        ),
                        new LogicalOpAllNodes()
                ),
                new LogicalOpRecursive(
                        new LogicalOpSelection(
                                new LogicalOpSelection(
                                        new LogicalOpAllEdges(), new First("X")
                                ), new Label("b", 1)
                        )
                )
        );
        PredicatePushdownLogicalPlanVisitor visitor = new PredicatePushdownLogicalPlanVisitor();
        lop.acceptVisitor(visitor);
        assertEquals(expected, visitor.getRoot());
    }

    @Test
    public void pushLastTest() {

        LogicalOperator lop = new LogicalOpSelection(
                new LogicalOpUnion(
                        new LogicalOpNodeJoin(
                                new LogicalOpSelection(new LogicalOpAllEdges(), new Label("a",1)),
                                new LogicalOpAllNodes()),
                        new LogicalOpRecursive(new LogicalOpSelection(new LogicalOpAllEdges(), new Label("b", 1))))
                , new Last("X"));
        LogicalOperator expected = new LogicalOpUnion(
                new LogicalOpNodeJoin(
                        new LogicalOpSelection(
                                new LogicalOpAllEdges(),  new Label("a", 1)
                        ),
                        new LogicalOpSelection(
                                new LogicalOpAllNodes(), new Last("X")
                        )),
                new LogicalOpRecursive(
                        new LogicalOpSelection(
                                new LogicalOpSelection(
                                        new LogicalOpAllEdges(), new Last("X")
                                ), new Label("b", 1)
                        )
                )
        );
        PredicatePushdownLogicalPlanVisitor visitor = new PredicatePushdownLogicalPlanVisitor();
        lop.acceptVisitor(visitor);
        assertEquals(expected, visitor.getRoot());
    }

}
