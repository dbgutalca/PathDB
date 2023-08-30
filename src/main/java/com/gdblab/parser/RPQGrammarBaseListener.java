// Generated from RPQGrammar.g4 by ANTLR 4.13.0
package com.gdblab.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.tree.ParseTree;
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

class TreeNode {
	String label;
	List<TreeNode> childs;

	public TreeNode(String label) {
		this.label = label;
		this.childs = new ArrayList<>();
	}

	public void addChild(TreeNode child) {
		childs.add(child);
	}

	public void addChilds(List<TreeNode> childs) {
		this.childs.addAll(childs);
	}

	public TreeNode getChildByIndex(int index) {
		return this.childs.get(index);
	}

	public int getChildsCount() {
		return this.childs.size();
	}
}

/**
 * InnerRPQGrammarBaseListener
 */
class Eval {
	private String term;
	private ArrayList<Path> paths;

	public Eval(String term, ArrayList<Path> paths) {
		this.term = term;
		this.paths = paths;
	}
}

/**
 * This class provides an empty implementation of {@link RPQGrammarListener},
 * which can be extended to create a listener which only needs to handle a
 * subset
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
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	@Override
	public void enterQuery(RPQGrammarParser.QueryContext ctx) {
		this.database = new Database();
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	@Override
	public void exitQuery(RPQGrammarParser.QueryContext ctx) {
		System.out.println("====================================================");
		System.out.println("Query: " + ctx.getText());
		System.out.println("====================================================");
		TreeNode tree = this.buildtree(ctx.getChild(0));
		this.printTree(tree, "");
	}

	private TreeNode buildtree(ParseTree p) {
		try {
			TreeNode root = new TreeNode(p.getText());
			List<TreeNode> childs = new ArrayList<>();
			
			for (int i = 0; i < p.getChildCount(); i++) {
				if (!p.getChild(i).getText().equals(".")) {
					childs.add(new TreeNode(p.getChild(i).getText()));
				}
			}

			for (int i = 0; i < childs.size(); i++) {
				childs.add(buildtree(p.getChild(i)));
			}

			root.addChilds(childs);

			return root;
		} catch (Exception e) {
			return null;
		}
		
	}

	private void printTree(TreeNode node, String prefix) {
		System.out.println(prefix + "└── " + node.label);

		for (int i = 0; i < node.childs.size(); i++) {
			if (i == node.childs.size() - 1) {
				printTree(node.childs.get(i), prefix + "    ");
			} else {
				printTree(node.childs.get(i), prefix + "│   ");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	@Override
	public void enterExpression(RPQGrammarParser.ExpressionContext ctx) {

	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	@Override
	public void exitExpression(RPQGrammarParser.ExpressionContext ctx) {
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	@Override
	public void enterTerm(RPQGrammarParser.TermContext ctx) {
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	@Override
	public void exitTerm(RPQGrammarParser.TermContext ctx) {

		ArrayList<Path> paths = new ArrayList<>();
		String term = ctx.getText();

		if (!term.contains("(") && !term.contains(")")) {
			if (term.contains("+")) {
				String termReplaced = term.replaceAll("\\+", "");
				this.evals.put(term, this.evaluatePlus(termReplaced));
			} else if (term.contains("*")) {
				String termReplaced = term.replaceAll("\\*", "");
				this.evals.put(term, this.evaluateKleene(termReplaced));
			} else if (term.contains("?")) {
				String termReplaced = term.replaceAll("\\?", "");
				this.evals.put(term, this.evaluateOptional(termReplaced));
			} else {
				paths = Select.eval(this.database.graph.getPaths(), new Label(term, 1));
				this.evals.put(term, paths);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	@Override
	public void enterBase(RPQGrammarParser.BaseContext ctx) {
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	@Override
	public void exitBase(RPQGrammarParser.BaseContext ctx) {
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	@Override
	public void enterOperator(RPQGrammarParser.OperatorContext ctx) {
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	@Override
	public void exitOperator(RPQGrammarParser.OperatorContext ctx) {
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	@Override
	public void enterSeparator(RPQGrammarParser.SeparatorContext ctx) {
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	@Override
	public void exitSeparator(RPQGrammarParser.SeparatorContext ctx) {
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	@Override
	public void enterEveryRule(ParserRuleContext ctx) {
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	@Override
	public void exitEveryRule(ParserRuleContext ctx) {
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	@Override
	public void visitTerminal(TerminalNode node) {
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation does nothing.
	 * </p>
	 */
	@Override
	public void visitErrorNode(ErrorNode node) {
	}

	private void printPath(ArrayList<Path> paths) {
		for (Path pp : paths) {
			System.out.print(pp.getId() + " : ");
			for (GraphObject go : pp.getSequence())
				System.out.print(go.getId() + " ");
			System.out.println("");
		}
	}

	private void printEvals() {
		System.out.println("====================================================");
		System.out.println("Evaluations: ");
		for (String key : this.evals.keySet()) {
			System.out.println(key + " : ");
			this.printPath(this.evals.get(key));
		}
		System.out.println("====================================================");
	}

	private ArrayList<Path> evaluatePlus(String label) {
		ArrayList<Path> paths = new ArrayList<>();
		paths = Select.eval(this.database.graph.getPaths(), new Label(label, 1));
		paths = Recursion.arbitrary(paths, this.MAX);
		return paths;
	}

	private ArrayList<Path> evaluateKleene(String label) {
		ArrayList<Path> paths = new ArrayList<>();
		paths = Select.eval(this.database.graph.getPaths(), new Label(label, 1));
		paths = Recursion.arbitrary(paths, this.MAX);
		paths = PathAlgebra.Union(paths, this.database.getPathsWithoutEdges());
		return paths;
	}

	private ArrayList<Path> evaluateOptional(String label) {
		ArrayList<Path> paths = new ArrayList<>();
		paths = Select.eval(this.database.graph.getPaths(), new Label(label, 1));
		paths = PathAlgebra.Union(paths, this.database.getPathsWithoutEdges());
		return paths;
	}
}