// Generated from RPQGrammar.g4 by ANTLR 4.13.0
package com.gdblab.parser;

import java.util.ArrayList;
import java.util.HashMap;

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
 * InnerRPQGrammarBaseListener
 */
class Eval{
	private String term;
	private ArrayList<Path> paths;

	public Eval(String term, ArrayList<Path> paths){
		this.term = term;
		this.paths = paths;
	}
}
/**
 * This class provides an empty implementation of {@link RPQGrammarListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
@SuppressWarnings("CheckReturnValue")
public class RPQGrammarBaseListener implements RPQGrammarListener {

	private Database database;
	private Integer MAX = 2;
	private HashMap<String, ArrayList<Path>> evals = new HashMap<>();
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterQuery(RPQGrammarParser.QueryContext ctx) {
		this.database = new Database();
		System.out.println("====================================================");
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitQuery(RPQGrammarParser.QueryContext ctx) { }
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
	@Override public void exitExpression(RPQGrammarParser.ExpressionContext ctx) { }
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
		
		ArrayList<Path> paths = new ArrayList<>();
		String term = ctx.getText();
		System.out.println("Incoming term: " + term);
		System.out.println("Saved terms: ");
		for (String key : this.evals.keySet()) {
			System.out.print(key + ", ");
		}
		System.out.println();

		if (term.contains("(") && term.contains(")")) {
			String operator = null; // este es el operador del conjunto
			String lastChar = Character.toString(term.charAt(term.length() - 1));
			if (lastChar.equals("?") || lastChar.equals("*") || lastChar.equals("+")) {
				operator = lastChar;
				term = term.substring(0, term.length() - 1);
			}

			if(term.chars().filter(c -> c == '(').count() == 1){
				ArrayList<Path> joinPaths = new ArrayList<>();

				term = term.substring(1, term.length() - 1);
				ArrayList<String> terms = new ArrayList<String>(java.util.Arrays.asList(term.split("\\.")));
				
				for (String t : terms) {
					System.out.println("T: " + t);
					if(joinPaths.isEmpty()){
						joinPaths = this.evals.get(t);
						printPath(joinPaths);
					}
					else{
						joinPaths = PathAlgebra.NodeJoin(joinPaths, this.evals.get(t));
						printPath(joinPaths);
					}
				}

				this.evals.put(term, joinPaths);
				
			}
			else{
				// ver que ****** hacer
			}

		}





		// esta parte esta lista
		else {
			if(term.contains("+")){
				String termReplaced = term.replaceAll("\\+", "");
				this.evals.put(term, this.evaluatePlus(termReplaced));
			}
			else if (term.contains("*")) {
				String termReplaced = term.replaceAll("\\*", "");
				this.evals.put(term, this.evaluateKleene(termReplaced));
			}
			else if (term.contains("?")) {
				String termReplaced = term.replaceAll("\\?", "");
				this.evals.put(term, this.evaluateOptional(termReplaced));
			}
			else {
				paths = Select.eval(this.database.graph.getPaths(), new Label(term, 1));
				this.evals.put(term, paths);
			}
		}

		// for (String key : this.evals.keySet()) {
		// 	System.out.println("Key: " + key);
		// 	printPath(this.evals.get(key));
		// }
		System.out.println("====================================================");

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

	
	
	private ArrayList<Path> evaluatePlus(String label){
		ArrayList<Path> paths = new ArrayList<>();
		paths = Select.eval(this.database.graph.getPaths(), new Label(label, 1));
		paths = Recursion.arbitrary(paths, this.MAX);
		return paths;
	}

	private ArrayList<Path> evaluateKleene(String label){
		ArrayList<Path> paths = new ArrayList<>();
		paths = Select.eval(this.database.graph.getPaths(), new Label(label, 1));
		paths = Recursion.arbitrary(paths, this.MAX);
		paths = PathAlgebra.Union(paths, this.database.getPathsWithoutEdges());
		return paths;
	}

	private ArrayList<Path> evaluateOptional(String label){
		ArrayList<Path> paths = new ArrayList<>();
		paths = Select.eval(this.database.graph.getPaths(), new Label(label, 1));
		paths = PathAlgebra.Union(paths, this.database.getPathsWithoutEdges());
		return paths;
	}
}