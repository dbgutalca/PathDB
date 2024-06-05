package com.gdblab.parser.impl;

import com.gdblab.algebra.condition.Label;
import com.gdblab.algebra.condition.Negated;
import com.gdblab.parser.RPQExpressionVisitor;
import com.gdblab.queryplan.logical.LogicalOperator;
import com.gdblab.queryplan.logical.impl.*;

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
        final LogicalOperator right = stack.pop();
        final LogicalOperator left = stack.pop();
        stack.push(new LogicalOpUnion(left, right));
    }

    @Override
    public void visit(final ConcatenationExpression concatenationExpression) {
        concatenationExpression.getLeft().acceptVisit(this);
        concatenationExpression.getRight().acceptVisit(this);
        final LogicalOperator right = stack.pop();
        final LogicalOperator left = stack.pop();
        stack.push(new LogicalOpNodeJoin(left, right));
    }

    @Override
    public void visit(final ZeroOrMoreExpression zeroOrMoreExpression) {
        zeroOrMoreExpression.getChild().acceptVisit(this);
        stack.push(new LogicalOpUnion(new LogicalOpRecursive(stack.pop()), new LogicalOpAllNodes()));
    }

    @Override
    public void visit(final OneOrMoreExpression oneOrMoreExpression) {
        oneOrMoreExpression.getChild().acceptVisit(this);
        stack.push(new LogicalOpRecursive(stack.pop()));
    }

    @Override
    public void visit(final ZeroOrOneExpression zeroOrOneExpression) {
        zeroOrOneExpression.getChild().acceptVisit(this);
        stack.push(new LogicalOpUnion(stack.pop(), new LogicalOpAllNodes()));
    }

    @Override
    public void visit(final LabelExpression labelExpression) {
        stack.push(new LogicalOpSelection(new LogicalOpAllEdges(), new Label( labelExpression.getLabel(), 1)));
    }
    
    @Override
    public void visit(final NegatedLabelExpression negatedLabelExpression) {
        stack.push(new LogicalOpSelection(new LogicalOpAllEdges(), new Negated(new Label(negatedLabelExpression.getLabel(), 1))));
    }
    
    @Override
    public void visit(final ReverseLabelExpression reverseLabelExpression) {
        stack.push(new LogicalOpReverse(new LogicalOpSelection(new LogicalOpAllEdges(), new Label( reverseLabelExpression.getLabel(), 1))));
    }

    public LogicalOperator getRoot() {
        return stack.pop();
    }

}
