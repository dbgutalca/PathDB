package com.gdblab.parser.impl;

import com.gdblab.parser.RPQExpression;
import com.gdblab.parser.RPQExpressionVisitor;

public class AlternativePathExpression extends BinaryRPQExpression{

    public AlternativePathExpression(final RPQExpression left, final RPQExpression right) {
        super(left, right);
    }

    @Override
    public void acceptVisit(final RPQExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AlternativePathExpression that) {
            return this.left.equals(that.left) && this.right.equals(that.right);
        }
        return false;
    }

    @Override
    public String toString() {
        return left+" | "+right;
    }
}
