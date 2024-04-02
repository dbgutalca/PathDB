package com.gdblab.parser.impl;

import com.gdblab.parser.RPQExpression;
import com.gdblab.parser.RPQExpressionVisitor;

public class ZeroOrMoreExpression extends UnaryRPQExpression{

    public ZeroOrMoreExpression(final RPQExpression child) {
        super(child);
    }

    @Override
    public void acceptVisit(final RPQExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ZeroOrMoreExpression that) {
            return this.child.equals(that.child);
        }
        return false;
    }

    @Override
    public String toString() {
        return child+"*";
    }
}
