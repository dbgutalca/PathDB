package com.gdblab.algebra.parser.impl;

import com.gdblab.algebra.parser.RPQExpression;
import com.gdblab.algebra.parser.RPQExpressionVisitor;

public class ZeroOrOneExpression extends UnaryRPQExpression{

    public ZeroOrOneExpression(final RPQExpression child) {
        super(child);
    }

    @Override
    public void acceptVisit(final RPQExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ZeroOrOneExpression that) {
            return this.child.equals(that.child);
        }
        return false;
    }

    @Override
    public String toString() {
        return child+"?";
    }
}
