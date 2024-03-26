// Generated from RPQGrammar.g4 by ANTLR 4.13.1

package com.gdblab.parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
import com.gdblab.schema.Edge;
import com.gdblab.schema.GraphObject;
import com.gdblab.schema.Path;

class TreeNode {
	String label;
	String operator;
	LinkedList<Path> paths;
	List<TreeNode> childs;
	boolean visited;

	public TreeNode(String label) {
		this.label = label;
		this.operator = "";
		this.paths = new LinkedList<>();
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

	public void setPaths(LinkedList<Path> paths) {
		this.paths = paths;
	}

	public LinkedList<Path> getPaths() {
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
	private Integer MAX = 3;
	private long totalTime = 0;

	
	
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

		long startTime = System.currentTimeMillis();

		LinkedList<Path> paths = this.evalTree(tree);

		long endTime = System.currentTimeMillis();

		this.totalTime = endTime - startTime;

		this.printPath(paths);

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

	public LinkedList<Path> evalTree (TreeNode tree) {
		
		if(tree.getChildsCount() == 0) {
			return tree.getPaths();
		}

		else if(tree.getChildsCount() == 1) {
			if (tree.getLabel().startsWith("(") &&  tree.getLabel().endsWith(")?")) {
				LinkedList<Path> p = this.evalTree(tree.getChildByIndex(0));
				p = PathAlgebra.Union(p, this.database.getPathsWithoutEdges());
				// checkPath(p);
				return p;
			}
			else if (tree.getLabel().startsWith("(") &&  tree.getLabel().endsWith(")*")) {
				LinkedList<Path> p = this.evalTree(tree.getChildByIndex(0));
				p = Recursion.arbitrary(p, this.MAX);
				p = PathAlgebra.Union(p, this.database.getPathsWithoutEdges());
				// checkPath(p);
				return p;
			}
			else if (tree.getLabel().startsWith("(") &&  tree.getLabel().endsWith(")+")) {
				LinkedList<Path> p = this.evalTree(tree.getChildByIndex(0));
				p = Recursion.arbitrary(p, this.MAX);
				
				// checkPath(p);
				return p;
			}
			else {
				LinkedList<Path> p = this.evalTree(tree.getChildByIndex(0));
				if (Main.semantic.equals("Simple Path")) p = Recursion.removeDuplicatedNodes(p);
				else if (Main.semantic.equals("Trail")) p = Recursion.removeDuplicatedEdges(p);
				
				// checkPath(p);
				return p;
			}
		}
		
		else {
			LinkedList<Path> result = new LinkedList<>();
			LinkedList<Path> pl = this.evalTree(tree.getChildByIndex(0));
			LinkedList<Path> pr = this.evalTree(tree.getChildByIndex(2));

			if (tree.getChildByIndex(1).getLabel().equals(".")) {
				result = PathAlgebra.NodeJoin(pl, pr);
			}
			else {
				result.addAll(pr);
				result.addAll(pl);
				
			}

			// checkPath(result);

			return result;
		}
	}

	private void evalLeaf(TreeNode node) {
		String term = node.getLabel();
		LinkedList<Path> paths = new LinkedList<>();
		HashSet<String> stringPaths = new HashSet<>();

		if(term.startsWith("!")){
			String label = term.substring(1);
			if(label.endsWith("?")){
				label = label.substring(0, label.length() - 1);

				// Este es para el algebra original
				paths = Select.eval(this.database.graph.getPaths(), new Not(new Label(label, 1)));
				paths = PathAlgebra.Union(paths, this.database.getPathsWithoutEdges());

				// Este es para la nueva algebra con Strings
				// stringPaths = NewPathAlgebra.eval(label);
				// stringPaths = NewPathAlgebra.pathsUnion(stringPaths, stringPaths);
			}
			else if(label.endsWith("*")){
				label = label.substring(0, label.length() - 1);

				// Este es para el algebra original
				paths = Select.eval(this.database.graph.getPaths(), new Not(new Label(label, 1)));
				paths = Recursion.arbitrary(paths, this.MAX);
				paths = PathAlgebra.Union(paths, this.database.getPathsWithoutEdges());

				// Este es para la nueva algebra con Strings
				// stringPaths = NewPathAlgebra.eval(label);
				// stringPaths = NewPathAlgebra.arbitrary(stringPaths);
			}
			else if(label.endsWith("+")){
				label = label.substring(0, label.length() - 1);
				paths = Select.eval(this.database.graph.getPaths(), new Not(new Label(label, 1)));
				paths = Recursion.arbitrary(paths, this.MAX);
				
			}
			else {
				paths = Select.eval(this.database.graph.getPaths(), new Not(new Label(label, 1)));
				if (Main.semantic.equals("Simple Path")) paths = Recursion.removeDuplicatedNodes(paths);
				else if (Main.semantic.equals("Trail")) paths = Recursion.removeDuplicatedEdges(paths);
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
				if (Main.semantic.equals("Simple Path")) paths = Recursion.removeDuplicatedNodes(paths);
				else if (Main.semantic.equals("Trail")) paths = Recursion.removeDuplicatedEdges(paths);
				
			}
		}

		node.setPaths(paths);
	}

	private void realPrintPath(LinkedList<Path> paths) {
		for (Path pp : paths) {
			for (GraphObject go : pp.getSequence()) {
				if (go.getId().startsWith("e"))
					System.out.print(go.getLabel() + " ");
				else
					System.out.print(go.getId() + " ");
			}
			System.out.println();
		}
	}

	private void printPath(LinkedList<Path> paths) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(Main.output))) {
			// print config
			writer.write("--Configuration--"); writer.newLine();
			writer.write("Algorithm: " + Main.algorithm); writer.newLine();
			writer.write("Database: " + Main.dburl); writer.newLine();
			writer.write("Semantic: " + Main.semantic); writer.newLine();
			writer.write("RPQ: " + Main.rpq); writer.newLine();
			writer.write("Time: " + this.totalTime + " ms"); writer.newLine();
			writer.write("--Configuration--"); writer.newLine();
			writer.write("--Paths--"); writer.newLine();
			for (Path pp : paths) {
				for (GraphObject go : pp.getSequence()) {
					if (go.getId().startsWith("e"))
						writer.write(go.getLabel() + " ");
					else
						writer.write(go.getId() + " ");
				}
				writer.newLine();
			}
			writer.write("--Paths--"); writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void checkPath(LinkedList<Path> paths){
        LinkedList<Path> pathsToRemove = new LinkedList<>();

        for (Path p : paths) {
            Map<String, Integer> c = new HashMap<>();
            
            for (Edge e : p.getEdgeSequence()) {
                if (c.containsKey(e.getId())) {
                    c.put(e.getId(), c.get(e.getId()) + 1);
                } else {
                    c.put(e.getId(), 1);
                }
            }

            for (Map.Entry<String, Integer> entry : c.entrySet()) {
                if (entry.getValue() >= 3) {
                    pathsToRemove.add(p);
                }
            }

        }

        paths.removeAll(pathsToRemove);
    }
}