package com.gdblab.parser;

import com.gdblab.algebra.condition.Label;
import com.gdblab.algebra.parser.RPQExpression;
import com.gdblab.algebra.parser.impl.*;
import com.gdblab.algebra.queryplan.logical.LogicalOperator;
import com.gdblab.algebra.queryplan.logical.impl.*;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RPQtoAlgebraTest {

    @Test
    public void unionTest() {
        final RPQExpression union = new AlternativePathExpression(new LabelExpression("x"), new LabelExpression("y"));
        final RPQtoAlgebraVisitor visitor = new RPQtoAlgebraVisitor();
        union.acceptVisit(visitor);
        final LogicalOperator lop = visitor.getRoot();
        assertEquals(new LogicalOpUnion(new LogicalOpSelection(new LogicalOpAllEdges(), new Label("x", 1)),
                new LogicalOpSelection(new LogicalOpAllEdges(), new Label("y", 1))), lop);
    }

    @Test
    public void joinTest() {
        final RPQExpression join = new ConcatenationExpression(new LabelExpression("x"), new LabelExpression("y"));
        final RPQtoAlgebraVisitor visitor = new RPQtoAlgebraVisitor();
        join.acceptVisit(visitor);
        final LogicalOperator lop = visitor.getRoot();
        assertEquals(new LogicalOpNodeJoin(new LogicalOpSelection(new LogicalOpAllEdges(), new Label("x", 1)),
                new LogicalOpSelection(new LogicalOpAllEdges(), new Label("y", 1))), lop);
    }

    @Test
    public void zeroOrMoreTest() {
        final RPQExpression zeroOrMore = new ZeroOrMoreExpression(new LabelExpression("x"));
        final RPQtoAlgebraVisitor visitor = new RPQtoAlgebraVisitor();
        zeroOrMore.acceptVisit(visitor);
        final LogicalOperator lop = visitor.getRoot();
        assertEquals(new LogicalOpUnion(new LogicalOpRecursive(new LogicalOpSelection(new LogicalOpAllEdges(), new Label("x", 1))),
                new LogicalOpAllNodes()), lop);
    }

    @Test
    public void oneOrMoreTest() {
        final RPQExpression oneOrMore = new OneOrMoreExpression(new LabelExpression("x"));
        final RPQtoAlgebraVisitor visitor = new RPQtoAlgebraVisitor();
        oneOrMore.acceptVisit(visitor);
        final LogicalOperator lop = visitor.getRoot();
        assertEquals(new LogicalOpRecursive(new LogicalOpSelection(new LogicalOpAllEdges(), new Label("x", 1))), lop);
    }

    @Test
    public void zeroOrOneTest() {
        final RPQExpression zeroOrOne = new ZeroOrOneExpression(new LabelExpression("x"));
        final RPQtoAlgebraVisitor visitor = new RPQtoAlgebraVisitor();
        zeroOrOne.acceptVisit(visitor);
        final LogicalOperator lop = visitor.getRoot();
        assertEquals(new LogicalOpUnion(new LogicalOpSelection(new LogicalOpAllEdges(), new Label("x", 1)),
                new LogicalOpAllNodes()), lop);
    }

}
