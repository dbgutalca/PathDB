package com.gdblab.parser;

import com.gdblab.algebra.condition.Label;
import com.gdblab.parser.impl.AlternativePathExpression;
import com.gdblab.parser.impl.ConcatenationExpression;
import com.gdblab.parser.impl.LabelExpression;
import com.gdblab.parser.impl.RPQtoAlgebraVisitor;
import com.gdblab.queryplan.logical.LogicalOperator;
import com.gdblab.queryplan.logical.impl.LogicalOpNodeJoin;
import com.gdblab.queryplan.logical.impl.LogicalOpSelection;
import com.gdblab.queryplan.logical.impl.LogicalOpUnion;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RPQtoAlgebraTest {

    @Test
    public void unionTest() {
        final RPQExpression union = new AlternativePathExpression(new LabelExpression("x"), new LabelExpression("y"));
        final RPQtoAlgebraVisitor visitor = new RPQtoAlgebraVisitor();
        union.acceptVisit(visitor);
        final LogicalOperator lop = visitor.getRoot();
        assertEquals(new LogicalOpUnion(new LogicalOpSelection(null, new Label("x", 1)),
                new LogicalOpSelection(null, new Label("y", 1))), lop);
    }
    @Test
    public void joinTest() {
        final RPQExpression join = new ConcatenationExpression(new LabelExpression("x"), new LabelExpression("y"));
        final RPQtoAlgebraVisitor visitor = new RPQtoAlgebraVisitor();
        join.acceptVisit(visitor);
        final LogicalOperator lop = visitor.getRoot();
        assertEquals(new LogicalOpNodeJoin(new LogicalOpSelection(null, new Label("x", 1)),
                new LogicalOpSelection(null, new Label("y", 1))), lop);
    }

}
