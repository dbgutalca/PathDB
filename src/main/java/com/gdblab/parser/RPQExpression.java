package com.gdblab.parser;

public interface RPQExpression {

    void acceptVisit(final RPQExpressionVisitor visitor);

}
