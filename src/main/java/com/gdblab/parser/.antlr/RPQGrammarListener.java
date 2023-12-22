// Generated from c:/Users/vroja/Documents/PathDatabase/src/main/java/com/gdblab/parser/RPQGrammar.g4 by ANTLR 4.13.1
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
	 * Enter a parse tree produced by {@link RPQGrammarParser#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(RPQGrammarParser.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link RPQGrammarParser#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(RPQGrammarParser.LabelContext ctx);
}