package com.gdblab.parser.impl;

import com.gdblab.parser.RPQExpressionVisitor;

public class NegatedLabelExpression extends NullaryRPQExpression{

    private final String label;

    public NegatedLabelExpression(final String label) {
        this.label = label;
    }

    @Override
    public void acceptVisit(final RPQExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NegatedLabelExpression that) {
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
