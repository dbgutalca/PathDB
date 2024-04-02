package com.gdblab.parser;

import com.gdblab.parser.impl.*;
import com.gdlab.parser.RPQGrammarBaseListener;
import com.gdlab.parser.RPQGrammarParser;

import java.text.ParseException;
import java.util.Stack;

public class RPQGrammarListener extends RPQGrammarBaseListener {

    RPQExpression root = null;
    final Stack<RPQExpression> stack = new Stack<>();
    boolean finished = false;

    @Override public void exitQuery(final RPQGrammarParser.QueryContext ctx) {
        root = stack.pop();
        finished = true;
    }

    @Override public void exitEdge(final RPQGrammarParser.EdgeContext ctx) {
        stack.push(new LabelExpression(ctx.getText()));
    }

    @Override public void exitConcatenation(RPQGrammarParser.ConcatenationContext ctx) {
        final RPQExpression right = stack.pop();
        final RPQExpression left = stack.pop();
        stack.push(new ConcatenationExpression(left, right));
    }

    @Override public void exitStar(final RPQGrammarParser.StarContext ctx) {
        stack.push(new ZeroOrMoreExpression(stack.pop()));
    }

    @Override public void exitAlternative(final RPQGrammarParser.AlternativeContext ctx) {
        final RPQExpression right = stack.pop();
        final RPQExpression left = stack.pop();
        stack.push(new AlternativePathExpression(left, right));
    }

    @Override public void exitOptional(final RPQGrammarParser.OptionalContext ctx) {
        stack.push(new ZeroOrOneExpression(stack.pop()));
    }

    //@Override public void exitParenthesis(RPQGrammarParser.ParenthesisContext ctx) { }

    @Override public void exitPlus(RPQGrammarParser.PlusContext ctx) {
        stack.push(new OneOrMoreExpression(stack.pop()));
    }

    public RPQExpression getRoot() {
        if (finished) {
            return root;
        }
        throw new RuntimeException("Parsing is not finished.");
    }

}
