package com.gdblab.algebra.parser;

public interface RPQExpression {

    void acceptVisit(final RPQExpressionVisitor visitor);

}
