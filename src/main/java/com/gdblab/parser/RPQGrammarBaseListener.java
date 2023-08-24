// Generated from RPQGrammar.g4 by ANTLR 4.13.0
package com.gdblab.parser;

import java.util.ArrayList;
import java.util.Arrays;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.gdblab.algebra.Select;
import com.gdblab.algebra.condition.Label;
import com.gdblab.algebra.PathAlgebra;
import com.gdblab.database.Database;
import com.gdblab.recursion.Recursion;
import com.gdblab.schema.GraphObject;
import com.gdblab.schema.Path;

/**
 * This class provides an empty implementation of {@link RPQGrammarListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
@SuppressWarnings("CheckReturnValue")
public class RPQGrammarBaseListener implements RPQGrammarListener {

	private Database database;
	private Integer MAX = 2;

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterQuery(RPQGrammarParser.QueryContext ctx) {
		this.database = new Database();
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitQuery(RPQGrammarParser.QueryContext ctx) {
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterExpression(RPQGrammarParser.ExpressionContext ctx) {
		
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitExpression(RPQGrammarParser.ExpressionContext ctx) {
		// System.out.println("Expression: " + ctx.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterTerm(RPQGrammarParser.TermContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitTerm(RPQGrammarParser.TermContext ctx) {
		ArrayList<Path> join =  null;
		
		String term = ctx.getText();

		// This first version support only a sequence of terms without parenthesis adn operators
		if(term.contains("(") && term.contains(")")){
			term = term.substring(1, term.length()-1);
			ArrayList<String> terms = new ArrayList<String>(Arrays.asList(term.split("\\.")));

			if(terms.size() == 1){
				join = Select.eval(this.database.graph.getPaths(), new Label(terms.get(0), 1));
			}

			else{
				for(String t : terms){
					if(join == null){
						if(t.contains("+")){
							t = t.replaceAll("\\+", "");
							join = Select.eval(this.database.graph.getPaths(), new Label(t, 1));
							join = Recursion.arbitrary(join, this.MAX);
						}

						else if (t.contains("*")) {
							t = t.replaceAll("\\*", "");
							join = Select.eval(this.database.graph.getPaths(), new Label(t, 1));
							join = Recursion.arbitrary(join, this.MAX);
							join = PathAlgebra.Union(join, this.database.getPathsWithoutEdges());
						}

						else if (t.contains("?")) {
							t = t.replaceAll("\\?", "");
							join = Select.eval(this.database.graph.getPaths(), new Label(t, 1));
							join = PathAlgebra.Union(join, this.database.getPathsWithoutEdges());
						}

						else {
							join = Select.eval(this.database.graph.getPaths(), new Label(t, 1));
						}
					}
					else{
						ArrayList<Path> paths = new ArrayList<>();
						if(t.contains("+")){
							t = t.replaceAll("\\+", "");
							paths = Select.eval(this.database.graph.getPaths(), new Label(t, 1));
							paths = Recursion.arbitrary(paths, this.MAX);
						}

						else if (t.contains("*")) {
							t = t.replaceAll("\\*", "");
							paths = Select.eval(this.database.graph.getPaths(), new Label(t, 1));
							paths = Recursion.arbitrary(paths, this.MAX);
							paths = PathAlgebra.Union(paths, this.database.getPathsWithoutEdges());
						}

						else if (t.contains("?")) {
							t = t.replaceAll("\\?", "");
							paths = Select.eval(this.database.graph.getPaths(), new Label(t, 1));
							paths = PathAlgebra.Union(paths, this.database.getPathsWithoutEdges());
						}

						else {
							paths = Select.eval(this.database.graph.getPaths(), new Label(t, 1));
						}

						join = PathAlgebra.NodeJoin(join, paths);
					}
				}
			}
			
			// System.out.println("Arbitrary");
			// printPath(Recursion.arbitrary(join, 10));
			// System.out.println(".................................................");
			// System.out.println("No repeated nodes");
			// printPath(Recursion.noRepeatedNodes(join, 10));
			// System.out.println(".................................................");
			// System.out.println("No repeated edges");
			// printPath(Recursion.noRepeatedEdges(join, 10));
			// System.out.println(".................................................");
			// System.out.println("Shortest Paths");
			// printPath(Recursion.shortestPath(join, 10));

			printPath(join);
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterBase(RPQGrammarParser.BaseContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitBase(RPQGrammarParser.BaseContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterOperator(RPQGrammarParser.OperatorContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitOperator(RPQGrammarParser.OperatorContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSeparator(RPQGrammarParser.SeparatorContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSeparator(RPQGrammarParser.SeparatorContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterEveryRule(ParserRuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitEveryRule(ParserRuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitTerminal(TerminalNode node) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitErrorNode(ErrorNode node) { }

	
	private void printPath(ArrayList<Path> paths) {
		for (Path pp : paths) {
			System.out.print(pp.getId() + " : ");
			for (GraphObject go : pp.getSequence())
				System.out.print(go.getId() + " ");
			System.out.println("");
		}
	}
}