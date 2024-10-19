package com.gdblab.algebra.parser;

import com.gdblab.algebra.parser.impl.*;

public interface RPQExpressionVisitor {

    void visit(final AlternativePathExpression alternativePathExpression);
    void visit(final ConcatenationExpression concatenationExpression);

    void visit(final ZeroOrMoreExpression zeroOrMoreExpression);
    void visit(final OneOrMoreExpression oneOrMoreExpression);
    void visit(final ZeroOrOneExpression zeroOrOneExpression);

    void visit(final LabelExpression labelExpression);
    void visit(final NegatedLabelExpression negatedLabelExpression);
    void visit(final ReverseLabelExpression reverseLabelExpression);

}
