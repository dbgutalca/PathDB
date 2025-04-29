package com.gdblab.algebra.parser;

import com.gdblab.algebra.condition.And;
import com.gdblab.algebra.condition.Condition;
import com.gdblab.algebra.condition.First;
import com.gdblab.algebra.condition.Last;
import com.gdblab.algebra.condition.Or;
import com.gdblab.algebra.parser.error.ErrorDetails;
import com.gdblab.algebra.parser.error.VariableNotFoundException;
import com.gdblab.algebra.parser.impl.*;
import com.gdblab.execution.Context;
import com.gdblab.execution.Tools;
import com.gdlab.parser.RPQGrammarBaseListener;
import com.gdlab.parser.RPQGrammarParser;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

public class RPQGrammarListener extends RPQGrammarBaseListener {

    RPQExpression regularExpressionRoot = null;
    final Stack<RPQExpression> regularExpressionStack = new Stack<>();

    Condition conditionRoot = null;
    final Stack<Condition> conditionalStack = new Stack<>();
    
    boolean finished = false;

    @Override public void exitQuery(final RPQGrammarParser.QueryContext ctx) {
        finished = true;
    }



    
    @Override public void exitRegularExpressionRule(final RPQGrammarParser.RegularExpressionRuleContext ctx) {
        try { this.regularExpressionRoot = this.regularExpressionStack.pop(); }
        catch (EmptyStackException ese) { this.regularExpressionRoot = null; }
        
        if (this.regularExpressionRoot == null) { /* Throw an exception of empty regular expression stack */ }
        else { Context.getInstance().setRegularExpression(this.regularExpressionRoot); }
    }

    @Override public void exitEdge(final RPQGrammarParser.EdgeContext ctx) {
        regularExpressionStack.push(new LabelExpression(ctx.getText()));
    }

    @Override public void exitNegatedEdge(final RPQGrammarParser.NegatedEdgeContext ctx) {
        regularExpressionStack.push(new NegatedLabelExpression(ctx.getText().substring(1)));
    }

    @Override public void exitReverseEdge(final RPQGrammarParser.ReverseEdgeContext ctx) {
        regularExpressionStack.push(new ReverseLabelExpression(ctx.getText().substring(0, ctx.getText().length() -1)));
    }
    
    @Override public void exitConcatenation(final RPQGrammarParser.ConcatenationContext ctx) {
        final RPQExpression right = regularExpressionStack.pop();
        final RPQExpression left = regularExpressionStack.pop();
        regularExpressionStack.push(new ConcatenationExpression(left, right));
    }

    @Override public void exitStar(final RPQGrammarParser.StarContext ctx) {
        regularExpressionStack.push(new ZeroOrMoreExpression(regularExpressionStack.pop()));
    }

    @Override public void exitAlternative(final RPQGrammarParser.AlternativeContext ctx) {
        final RPQExpression right = regularExpressionStack.pop();
        final RPQExpression left = regularExpressionStack.pop();
        regularExpressionStack.push(new AlternativePathExpression(left, right));
    }

    @Override public void exitOptional(final RPQGrammarParser.OptionalContext ctx) {
        regularExpressionStack.push(new ZeroOrOneExpression(regularExpressionStack.pop()));
    }

    @Override public void exitPlus(final RPQGrammarParser.PlusContext ctx) {
        regularExpressionStack.push(new OneOrMoreExpression(regularExpressionStack.pop()));
    }



    @Override public void exitRangeRecursive(final RPQGrammarParser.RangeRecursiveContext ctx) {
        String range = ctx.getText().substring(3, ctx.getText().length() - 1);
        int rangeValue = Integer.parseInt(range);
        Context.getInstance().setMaxRecursion(rangeValue);
    }

    @Override public void exitLimitStatement(final RPQGrammarParser.LimitStatementContext ctx) {
        String limit = ctx.getText().replace("LIMIT", "");
        int limitValue = Integer.parseInt(limit);
        Context.getInstance().setLimit(limitValue);
    }
    


    @Override public void exitReturnStatement(final RPQGrammarParser.ReturnStatementContext ctx) {
        String returnStatement = ctx.getText().replace("RETURN", "").trim();
        String[] data = returnStatement.split(",");
        ArrayList<String> variables = new ArrayList<>();
        variables.add(Context.getInstance().getPathsName());
        variables.add(Context.getInstance().getLeftVarName());
        variables.add(Context.getInstance().getRightVarName());
        for (String s : data) {
            String variable = s.trim();

            if (variables.contains(variable)) {

                if (!Context.getInstance().getReturnedVariables().contains(variable)){
                    Context.getInstance().addReturnedVariable(variable);
                }
                else {

                }
            }
            else {
                ErrorDetails ed = new ErrorDetails(0, variable, "Variable " + variable +" not found.");
                throw new VariableNotFoundException(ed);
            }
        }
    }



    @Override public void exitConditionalExpression(final RPQGrammarParser.ConditionalExpressionContext ctx) {
        try { this.conditionRoot = this.conditionalStack.pop(); }
        catch (EmptyStackException ese) { this.conditionRoot = null; }
        
        if (this.conditionRoot == null) { /* Throw an exception of empty condition stack */ }
        else { Context.getInstance().setCondition(this.conditionRoot); }
    }

    @Override public void exitAndConditionals(final RPQGrammarParser.AndConditionalsContext ctx) {
        Condition left = conditionalStack.pop();
        Condition right = conditionalStack.pop();
        conditionalStack.push(new And(left, right));
    }

    @Override public void exitOrConditionals(final RPQGrammarParser.OrConditionalsContext ctx) {
        Condition left = conditionalStack.pop();
        Condition right = conditionalStack.pop();
        conditionalStack.push(new Or(left, right));
    }

    @Override public void exitConditionalsEvaluation(final RPQGrammarParser.ConditionalsEvaluationContext ctx) {
        
        String condition = Tools.getConditional(ctx.getText());

        String[] data = ctx.getText().split(condition);
        String valueToFind = data[1].replaceAll("\"", "");

        String[] data2 = data[0].split("\\.");
        String variableToFind = data2[0];
        String propertyToFind = data2[1];

        if (variableToFind.equals(Context.getInstance().getLeftVarName())) {
            First first = new First(propertyToFind, condition, valueToFind);
            this.conditionalStack.push(first);
        }
        else if (variableToFind.equals(Context.getInstance().getRightVarName())) {
            Last last = new Last(propertyToFind,condition , valueToFind);
            this.conditionalStack.push(last);
        }
        else {
            ErrorDetails ed = new ErrorDetails(0, variableToFind, "Variable " + variableToFind +" not found.");
            throw new VariableNotFoundException(ed);
        }
    }



    @Override public void exitRestrictorsStatement(final RPQGrammarParser.RestrictorsStatementContext ctx) {
        switch (ctx.getText()) {
            case "WALK":
                Context.getInstance().setSemantic(1);
                break;
            case "TRAIL":
                Context.getInstance().setSemantic(2);
                break;
            case "ACYCLIC":
                Context.getInstance().setSemantic(3);
                break;
            case "SIMPLE":
                Context.getInstance().setSemantic(4);
                break;
        }
    }

    @Override public void exitPathName(final RPQGrammarParser.PathNameContext ctx) {
        Context.getInstance().setPathsName(ctx.getText());
    }

    @Override public void exitNodePatternLeft(final RPQGrammarParser.NodePatternLeftContext ctx) {
        Context.getInstance().setLeftVarName(ctx.getText().substring(1, ctx.getText().length() - 1));
    }

    @Override public void exitNodePatternRight(final RPQGrammarParser.NodePatternRightContext ctx) {
        Context.getInstance().setRightVarName(ctx.getText().substring(1, ctx.getText().length() - 1));
    }
    
}
