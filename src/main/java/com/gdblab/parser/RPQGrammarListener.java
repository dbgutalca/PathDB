// Generated from RPQGrammar.g4 by ANTLR 4.13.0
package com.gdblab.parser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link RPQGrammarParser}.
 */
public interface RPQGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link RPQGrammarParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(RPQGrammarParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link RPQGrammarParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(RPQGrammarParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link RPQGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(RPQGrammarParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RPQGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(RPQGrammarParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RPQGrammarParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(RPQGrammarParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link RPQGrammarParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(RPQGrammarParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link RPQGrammarParser#base}.
	 * @param ctx the parse tree
	 */
	void enterBase(RPQGrammarParser.BaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link RPQGrammarParser#base}.
	 * @param ctx the parse tree
	 */
	void exitBase(RPQGrammarParser.BaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link RPQGrammarParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(RPQGrammarParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RPQGrammarParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(RPQGrammarParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RPQGrammarParser#separator}.
	 * @param ctx the parse tree
	 */
	void enterSeparator(RPQGrammarParser.SeparatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RPQGrammarParser#separator}.
	 * @param ctx the parse tree
	 */
	void exitSeparator(RPQGrammarParser.SeparatorContext ctx);
}