package com.gdblab.parser.impl;

import com.gdblab.parser.RPQExpressionVisitor;

public class ReverseLabelExpression extends NullaryRPQExpression{

    private final String label;

    public ReverseLabelExpression(final String label) {
        this.label = label;
    }

    @Override
    public void acceptVisit(final RPQExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ReverseLabelExpression that) {
            return this.label.equals(that.label);
        }
        return false;
    }

    @Override
    public String toString() {
        return label;
    }

    public String getLabel() {
        return label;
    }
}
