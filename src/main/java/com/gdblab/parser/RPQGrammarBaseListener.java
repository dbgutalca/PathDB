// Generated from RPQGrammar.g4 by ANTLR 4.13.0
package com.gdblab.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
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
	String operator;
	ArrayList<Path> paths;
	List<TreeNode> childs;
	boolean visited;

	public TreeNode(String label) {
		this.label = label;
		this.operator = "";
		this.paths = new ArrayList<>();
		this.childs = new ArrayList<>();
		this.visited = false;
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

	public void visitNode() {
		this.visited = true;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setPaths(ArrayList<Path> paths) {
		this.paths = paths;
	}

	public ArrayList<Path> getPaths() {
		return paths;
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
		System.out.println("Query: " + ctx.getText().replaceAll("<EOF>", ""));
		System.out.println("====================================================");
		ParseTree e = ctx.getChild(0);

		TreeNode tree = this.buildtree(e);
		this.printTree(tree, "");
		recorridoPostorden(tree);
	}

	private TreeNode buildtree(ParseTree p) {
		TreeNode root = new TreeNode(p.getText());
		ArrayList<TreeNode> childs = new ArrayList<>();

		// Este if es para detener la producción de hojas una vez que se ha llegado a un
		// nodo atómico
		if (!p.getText().contains(".") && !p.getText().equals("(") && !p.getText().equals(")")) {
			return root;
		}

		// Previene que se añadan nodos que solo contengan operadores
		// sobre nodos que tengan paréntesis
		if(p.getText().startsWith("(") && (
			p.getText().endsWith(")?") ||
			p.getText().endsWith(")*") ||
			p.getText().endsWith(")+") )){
			TreeNode child = this.buildtree(p.getChild(0));
			childs.add(child);
		}
		else{
			for (int i = 0; i < p.getChildCount(); i++) {
			if(!p.getChild(i).getText().equals("(") && !p.getChild(i).getText().equals(")") && !p.getChild(i).getText().equals(".")){
				TreeNode child = this.buildtree(p.getChild(i));
				childs.add(child);
			}
		}
		}

		root.addChilds(childs);

		return root;
	}

	private void printTree(TreeNode node, String prefix) {
		System.out.println(prefix + "└── " + node.label);

		for (int i = 0; i < node.childs.size(); i++) {
			if (i == node.childs.size() - 1) {
				printTree(node.childs.get(i), prefix + "|   ");
			} else {
				printTree(node.childs.get(i), prefix + "|   ");
			}
		}
	}

	private void recorridoPostorden(TreeNode node) {
		if (node.childs == null) {
			return;
		}

		if (node.getChildsCount() == 0) {
			// extraer la evaluacion del hashmap
			node.setPaths(this.evals.get(node.label));
		}
		else if ( node.getChildsCount() == 1 && node.getChildByIndex(0).label.equals(node.label)) {
			// si tiene un solo hijo y es igual a su padre, entonces se debe copiar los mismos paths
			node.setPaths(node.getChildByIndex(0).getPaths());
		}
		else if ( node.getChildsCount() == 1 && !node.getChildByIndex(0).label.equals(node.label)) {
			// si tiene un solo hijo y es distinto al padre significa que hay un operador extra, se debe copiar la evaluacion del hijo
			// y agregar según corresponda
			ArrayList<Path> paths = node.getChildByIndex(0).getPaths();
			if(node.label.endsWith("?")){
				paths = PathAlgebra.Union(paths, this.database.getPathsWithoutEdges());
			}
			else if(node.label.endsWith("*")){
				paths = PathAlgebra.Union(paths, this.database.getPathsWithoutEdges());
			}
			else if(node.label.endsWith("+")){
				paths = Recursion.arbitrary(paths, this.MAX);
			}
			node.setPaths(paths);
		}
		else if ( node.getChildsCount() > 1) {
			// Si tiene más de un hijo se debe verificar si ya fueron visitados todos
			// en caso de que todos fueron visitados se debe realizar la operación correspondiente
			// y agregar los paths al nodo padre
			if (this.checkIfChildsVisited(node)) {
				
			}
		}

		if (node.childs != null) {
			for (TreeNode hijo : node.childs) {
				recorridoPostorden(hijo);
			}
		}

		
	}

	private boolean checkIfChildsVisited(TreeNode node) {
		for (TreeNode child : node.childs) {
			if (!child.visited) {
				return false;
			}
		}

		return true;
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