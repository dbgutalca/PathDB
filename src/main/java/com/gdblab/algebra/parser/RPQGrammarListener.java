package com.gdblab.algebra.parser;

import com.gdblab.algebra.condition.And;
import com.gdblab.algebra.condition.Condition;
import com.gdblab.algebra.condition.First;
import com.gdblab.algebra.condition.Label;
import com.gdblab.algebra.condition.Last;
import com.gdblab.algebra.condition.Length;
import com.gdblab.algebra.condition.Node;
import com.gdblab.algebra.condition.Or;
import com.gdblab.algebra.parser.error.ErrorDetails;
import com.gdblab.algebra.parser.error.PropertyDoesntExist;
import com.gdblab.algebra.parser.error.VariableNotFoundException;
import com.gdblab.algebra.parser.impl.*;
import com.gdblab.algebra.returncontent.ReturnContent;
import com.gdblab.algebra.returncontent.ReturnVariable;
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

    final ArrayList<ReturnContent> returnContent = new ArrayList<>();
    
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
        Context.getInstance().setReturnedVariables(this.returnContent);
    }

    @Override public void exitReturnVariable(final RPQGrammarParser.ReturnVariableContext ctx) {
        this.returnContent.add(new ReturnVariable(ctx.getText()));
    }
    
    @Override public void exitReturnVariableWithProperty(final RPQGrammarParser.ReturnVariableWithPropertyContext ctx) {}
    @Override public void exitReturnFirst(final RPQGrammarParser.ReturnFirstContext ctx) {}
    @Override public void exitReturnFirstWithProperty(final RPQGrammarParser.ReturnFirstWithPropertyContext ctx) {}
    @Override public void exitReturnLast(final RPQGrammarParser.ReturnLastContext ctx) {}
    @Override public void exitReturnLastWithProperty(final RPQGrammarParser.ReturnLastWithPropertyContext ctx) {}
    @Override public void exitReturnNode(final RPQGrammarParser.ReturnNodeContext ctx) {}
    @Override public void exitReturnNodeWithProperty(final RPQGrammarParser.ReturnNodeWithPropertyContext ctx) {}
    @Override public void exitReturnEdge(final RPQGrammarParser.ReturnEdgeContext ctx) {}
    @Override public void exitReturnEdgeWithProperty(final RPQGrammarParser.ReturnEdgeWithPropertyContext ctx) {}
    @Override public void exitReturnLabelNode(final RPQGrammarParser.ReturnLabelNodeContext ctx) {}
    @Override public void exitReturnLabelEdge(final RPQGrammarParser.ReturnLabelEdgeContext ctx) {}



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

        if (variableToFind.equals(Context.getInstance().getPathsName())) {
            switch (propertyToFind) {
                case "length":
                    Length length = new Length(Integer.parseInt(valueToFind), condition);
                    this.conditionalStack.push(length);
                    break;
            
                default:
                    ErrorDetails ed = new ErrorDetails(0, propertyToFind, "Property " + propertyToFind +" not found.");
                    throw new PropertyDoesntExist(ed);
            }
        }
        else if (variableToFind.equals(Context.getInstance().getLeftVarName())) {
            First first = new First(propertyToFind, condition, valueToFind);
            this.conditionalStack.push(first);
        }
        else if (variableToFind.equals(Context.getInstance().getRightVarName())) {
            Last last = new Last(propertyToFind, condition , valueToFind);
            this.conditionalStack.push(last);
        }
        else {
            ErrorDetails ed = new ErrorDetails(0, variableToFind, "Variable " + variableToFind +" not found.");
            throw new VariableNotFoundException(ed);
        }
    }

    @Override public void exitConditionalsEvalFunction(final RPQGrammarParser.ConditionalsEvalFunctionContext ctx) {
        String condition = Tools.getConditional(ctx.getText());

        String data[] = ctx.getText().split(condition);
        String valueToFind = data[1].replaceAll("\"", "");

        String data2[] = data[0].split("\\.");
        String function = data2[0];
        String propertyToFind = data2[1];

        System.out.println("Value: " + valueToFind);
        System.out.println("Function: " + function);
        System.out.println("Property: " + propertyToFind);

        if (function.startsWith("FIRST()")) {
            First first = new First(propertyToFind, condition, valueToFind);
            this.conditionalStack.push(first);
        }
        else if (function.startsWith("LAST()")) {
            Last last = new Last(propertyToFind, condition , valueToFind);
            this.conditionalStack.push(last);
        }
        else if (function.startsWith("NODE(")) {
            int pos = Integer.parseInt(function.substring(5, function.length() - 1));
            Node node = new Node(propertyToFind, condition, valueToFind, pos);
            this.conditionalStack.push(node);
        }
        else if (function.startsWith("LABEL(NODE(")) {
            int pos = Integer.parseInt(function.substring(11, function.length() - 2));
            Label label = new Label(valueToFind, "node", pos);
            this.conditionalStack.push(label);
        }
        else if (function.startsWith("LABEL(EDGE(")) {
            int pos = Integer.parseInt(function.substring(11, function.length() - 2));
            Label label = new Label(valueToFind, "edge", pos);
            this.conditionalStack.push(label);
        }
        else {
            ErrorDetails ed = new ErrorDetails(0, function, "Function " + function +" not found.");
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
        if (ctx.getText().equals("()")) {
            Context.getInstance().setLeftVarName(ctx.getText().substring(1, ctx.getText().length() - 1));
        }
    }

    @Override public void exitNodePatternRight(final RPQGrammarParser.NodePatternRightContext ctx) {
        if (ctx.getText().equals("()")) {
            Context.getInstance().setRightVarName(ctx.getText().substring(1, ctx.getText().length() - 1));
        }
    }
    
}
