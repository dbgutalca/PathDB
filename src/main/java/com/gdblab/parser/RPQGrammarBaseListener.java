// Generated from RPQGrammar.g4 by ANTLR 4.13.1

package com.gdblab.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.runtime.tree.Tree;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.gdblab.algebra.PathAlgebra;
import com.gdblab.algebra.Select;
import com.gdblab.algebra.condition.Label;
import com.gdblab.algebra.condition.Not;
import com.gdblab.database.Database;
import com.gdblab.main.Main;
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

	public String getLabel() {
		return label;
	}

	public void setPaths(ArrayList<Path> paths) {
		this.paths = paths;
	}

	public ArrayList<Path> getPaths() {
		return paths;
	}
}

/**
 * This class provides an empty implementation of {@link RPQGrammarListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
@SuppressWarnings("CheckReturnValue")
public class RPQGrammarBaseListener implements RPQGrammarListener {

	public Database database = Main.db;
	private Integer MAX = 2;
	private HashMap<String, ArrayList<Path>> evals = new HashMap<>();
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterQuery(RPQGrammarParser.QueryContext ctx) {
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitQuery(RPQGrammarParser.QueryContext ctx) {

		ParseTree pt = ctx.getChild(0);

		TreeNode tree = this.buildTree(pt);

		System.out.println("-------------------------------------------------");
		
		System.out.println("Query: " + ctx.getChild(0).getText());

		System.out.println("-------------------------------------------------");

		System.out.println("Tree:");
		this.printTree(tree, "  ", false);

		System.out.println("-------------------------------------------------");

		System.out.println("Paths:");
		ArrayList<Path> paths = this.evalTree(tree);
		this.printPath(paths);

		System.out.println("-------------------------------------------------");
		// this.printEvals();
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterExpression(RPQGrammarParser.ExpressionContext ctx) { }
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
	@Override public void enterLabel(RPQGrammarParser.LabelContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitLabel(RPQGrammarParser.LabelContext ctx) { }

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


	// Build the tree to evaluate the query
	// the tree its a 
	public TreeNode buildTree (ParseTree p) {
		TreeNode root = new TreeNode(p.getText());
		ArrayList<TreeNode> childs = new ArrayList<>();

		// si es que llegó a una hoja
		if (!p.getText().contains(".") && !p.getText().contains("|") && !p.getText().contains("(") && !p.getText().contains(")")) {
			this.evalLeaf(root);
			return root;
		}

		if(p.getText().startsWith("(") && (
			p.getText().endsWith(")?") ||
			p.getText().endsWith(")*") ||
			p.getText().endsWith(")+") )){
			TreeNode child = this.buildTree(p.getChild(0));
			childs.add(child);
		}
		else{
			for (int i = 0; i < p.getChildCount(); i++) {
				if(!p.getChild(i).getText().equals("(") && !p.getChild(i).getText().equals(")")){
					TreeNode child = this.buildTree(p.getChild(i));
					childs.add(child);
				}
			}
		}

		root.addChilds(childs);
		return root;
	}

	public ArrayList<Path> evalTree (TreeNode tree) {
		
		// This is a leaf
		if(tree.getChildsCount() == 0) {
			return tree.getPaths();
		}

		// This is a father node with only one child
		// in all cases this is generated by ANTLR4
		// because of the parenthesis
		else if(tree.getChildsCount() == 1) {
			// falta checkear si el nodo posee un operador ? * +
			if (tree.getLabel().startsWith("(") &&  tree.getLabel().endsWith(")?")) {
				ArrayList<Path> p = this.evalTree(tree.getChildByIndex(0));
				p = PathAlgebra.Union(p, this.database.getPathsWithoutEdges());
				return p;
			}
			else if (tree.getLabel().startsWith("(") &&  tree.getLabel().endsWith(")*")) {
				ArrayList<Path> p = this.evalTree(tree.getChildByIndex(0));
				p = Recursion.arbitrary(p, this.MAX);
				p = PathAlgebra.Union(p, this.database.getPathsWithoutEdges());
				return p;
			}
			else if (tree.getLabel().startsWith("(") &&  tree.getLabel().endsWith(")+")) {
				ArrayList<Path> p = this.evalTree(tree.getChildByIndex(0));
				p = Recursion.arbitrary(p, this.MAX);
				return p;
			}
			else {
				ArrayList<Path> p = this.evalTree(tree.getChildByIndex(0));
				return p;
			}
		}

		// Other cases (father node with two childs)
		// check if is a concatenation or union
		else {
			ArrayList<Path> result = new ArrayList<>();
			ArrayList<Path> pl = this.evalTree(tree.getChildByIndex(0));
			ArrayList<Path> pr = this.evalTree(tree.getChildByIndex(2));

			if (tree.getChildByIndex(1).getLabel().equals(".")) {
				result = PathAlgebra.NodeJoin(pl, pr);
			}
			else {
				result.addAll(pr);
				result.addAll(pl);
			}

			return result;
		}
	}

	private void evalLeaf(TreeNode node) {
		String term = node.getLabel();
		ArrayList<Path> paths = new ArrayList<>();

		if(term.startsWith("!")){
			String label = term.substring(1);
			if(label.endsWith("?")){
				label = label.substring(0, label.length() - 1);
				paths = Select.eval(this.database.graph.getPaths(), new Not(new Label(label, 1)));
				paths = PathAlgebra.Union(paths, this.database.getPathsWithoutEdges());
			}
			else if(label.endsWith("*")){
				label = label.substring(0, label.length() - 1);
				paths = Select.eval(this.database.graph.getPaths(), new Not(new Label(label, 1)));
				paths = Recursion.arbitrary(paths, this.MAX);
				paths = PathAlgebra.Union(paths, this.database.getPathsWithoutEdges());
			}
			else if(label.endsWith("+")){
				label = label.substring(0, label.length() - 1);
				paths = Select.eval(this.database.graph.getPaths(), new Not(new Label(label, 1)));
				paths = Recursion.arbitrary(paths, this.MAX);
			}
			else {
				paths = Select.eval(this.database.graph.getPaths(), new Not(new Label(label, 1)));
			}
		}
		else {
			String label = term;
			if(label.endsWith("?")){
				label = label.substring(0, label.length() - 1);
				paths = Select.eval(this.database.graph.getPaths(), new Label(label, 1));
				paths = PathAlgebra.Union(paths, this.database.getPathsWithoutEdges());
			}
			else if(label.endsWith("*")){
				label = label.substring(0, label.length() - 1);
				paths = Select.eval(this.database.graph.getPaths(), new Label(label, 1));
				paths = Recursion.arbitrary(paths, this.MAX);
				paths = PathAlgebra.Union(paths, this.database.getPathsWithoutEdges());
			}
			else if(label.endsWith("+")){
				label = label.substring(0, label.length() - 1);
				paths = Select.eval(this.database.graph.getPaths(), new Label(label, 1));
				paths = Recursion.arbitrary(paths, this.MAX);
			}
			else {
				paths = Select.eval(this.database.graph.getPaths(), new Label(label, 1));
			}
		}

		node.setPaths(paths);
		this.evals.put(node.getLabel(), paths);		
	}

	private void printTree(TreeNode node, String prefix, boolean isLast) {
		System.out.print(prefix);
		if (isLast) {
			System.out.println("└── " + node.label);
			prefix += "    ";
		} else {
			System.out.println("├── " + node.label);
			prefix += "|   ";
		}

		for (int i = 0; i < node.childs.size(); i++) {
			boolean lastChild = (i == node.childs.size() - 1);
			printTree(node.childs.get(i), prefix, lastChild);
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

	private void printPath(ArrayList<Path> paths) {
		for (Path pp : paths) {
			System.out.print(pp.getId() + " : ");
			for (GraphObject go : pp.getSequence()){

				if(go.getId().startsWith("e"))
					System.out.print(go.getLabel() + " ");
				
				else
					System.out.print(go.getId() + " ");
				
			}
			System.out.println("");
		}
	}

	private void printGraph() {
		System.out.println("    ┌─────B────┐\r\n" + //
				"    │          v\r\n" + //
				"  ┌─┴┐        ┌──┬───B──┐\r\n" + //
				"┌>│N1│<──C────┤N2│      │\r\n" + //
				"│ └┬─┘        ├──┴─A─┐  │\r\n" + //
				"│  C          │ ^    v  v\r\n" + //
				"└──┘          │ │    ┌──┐\r\n" + //
				"      ┌───A───┘ C    │N5│\r\n" + //
				"      v         │    └┬─┘\r\n" + //
				"    ┌──┐      ┌─┴┐    │\r\n" + //
				"  ┌>│N3├──D──>│N4│<─B─┘\r\n" + //
				"  │ └┬─┘      └──┘\r\n" + //
				"  B  │\r\n" + //
				"  └──┘");
	}

}