package com.gdblab.parser;

import com.gdblab.parser.impl.*;

public interface RPQExpressionVisitor {

    void visit(final AlternativePathExpression alternativePathExpression);
    void visit(final ConcatenationExpression concatenationExpression);

    void visit(final ZeroOrMoreExpression zeroOrMoreExpression);
    void visit(final OneOrMoreExpression oneOrMoreExpression);
    void visit(final ZeroOrOneExpression zeroOrOneExpression);

    void visit(final LabelExpression labelExpression);

}
