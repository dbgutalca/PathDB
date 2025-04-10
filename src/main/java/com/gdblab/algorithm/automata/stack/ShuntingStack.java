package com.gdblab.algorithm.automata.stack;

import com.gdblab.algorithm.automata.tree.node.BranchNode;
import com.gdblab.algorithm.automata.tree.node.LeafNode;
import com.gdblab.algorithm.automata.tree.node.Node;
import com.gdblab.algorithm.automata.tree.node.bracket.LeftBracket;
import com.gdblab.algorithm.automata.tree.node.bracket.RightBracket;
import com.gdblab.algorithm.automata.util.InvalidSyntaxException;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Created on 2015/5/9.
 */
public class ShuntingStack { // powered by Shunting Yard Algorithm.
    private Stack<Node> finalStack;
    private Stack<BranchNode> branchStack;

    public ShuntingStack() {
        finalStack = new Stack<>();
        branchStack = new Stack<>();
    }

    public void visit(LeftBracket leftBracket) {
        branchStack.push(leftBracket);
    }

    public void visit(RightBracket rightBracket) {
        try {
            for (Node node = branchStack.pop(); !(node instanceof LeftBracket); node = branchStack.pop()) {
                finalStack.push(node);
            }
        } catch (EmptyStackException e) {
            throw new InvalidSyntaxException(e);
        }
    }

    public void visit(LeafNode leafNode) {
        finalStack.push(leafNode);
    }

    public void visit(BranchNode branchNode) {
        while (!branchStack.isEmpty() && branchNode.getPri() != -1 && branchNode.getPri() <= branchStack.peek().getPri()) {
            finalStack.push(branchStack.pop());
        }
        branchStack.push(branchNode);
    }

    public Stack<Node> finish() {
        while (!branchStack.isEmpty()) {
            finalStack.push(branchStack.pop());
        }
        Stack<Node> reversedStack = new Stack<>();
        while (!finalStack.isEmpty()) {
            reversedStack.push(finalStack.pop());
        }
        return reversedStack;
    }

    @Override
    public String toString() {
        return "ShuntingStack{" +
                "finalStack=" + finalStack +
                '}';
    }
}
