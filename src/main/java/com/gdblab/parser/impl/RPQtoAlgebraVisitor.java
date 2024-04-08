package com.gdblab.parser.impl;

import com.gdblab.algebra.condition.Label;
import com.gdblab.parser.RPQExpressionVisitor;
import com.gdblab.queryplan.logical.LogicalOperator;
import com.gdblab.queryplan.logical.impl.LogicalOpNodeJoin;
import com.gdblab.queryplan.logical.impl.LogicalOpSelection;
import com.gdblab.queryplan.logical.impl.LogicalOpUnion;

import java.util.Stack;

/**
 * Traverses the results of the parsing of the RPQ and produces the tree of logical
 * operators that resolve the RPQ according to García et al.
 */
public class RPQtoAlgebraVisitor implements RPQExpressionVisitor {

    private final Stack<LogicalOperator> stack = new Stack<>();

    @Override
    public void visit(final AlternativePathExpression alternativePathExpression) {
        alternativePathExpression.getLeft().acceptVisit(this);
        alternativePathExpression.getRight().acceptVisit(this);
        stack.push(new LogicalOpUnion(stack.pop(), stack.pop()));
    }

    @Override
    public void visit(final ConcatenationExpression concatenationExpression) {
        concatenationExpression.getLeft().acceptVisit(this);
        concatenationExpression.getRight().acceptVisit(this);
        stack.push(new LogicalOpNodeJoin(stack.pop(), stack.pop()));
    }

    @Override
    public void visit(final ZeroOrMoreExpression zeroOrMoreExpression) {
        // WE NEED THE RECURSIVE LOGICAL OPERATOR
    }

    @Override
    public void visit(final OneOrMoreExpression oneOrMoreExpression) {
        // WE NEED THE RECURSIVE LOGICAL OPERATOR
    }

    @Override
    public void visit(final ZeroOrOneExpression zeroOrOneExpression) {
        // WE NEED AN OPERATOR TO GET S_0 and S_1
    }

    @Override
    public void visit(final LabelExpression labelExpression) {
        // NEED A GENERIC ALL GRAPH PATHS TO FEED INTO THIS ONE, AND REPLACE THE null
        stack.push(new LogicalOpSelection(null, new Label(labelExpression.getLabel(), 1)));
    }
}
