package com.gdblab.automata.stack;

import com.gdblab.automata.tree.node.BranchNode;
import com.gdblab.automata.tree.node.LeafNode;
import com.gdblab.automata.tree.node.Node;

import java.util.Stack;

/**
 * Created on 2015/5/9.
 */
public class OperatingStack {

    private Stack<Node> stack;

    public OperatingStack() {
        this.stack = new Stack<>();
    }

    public void visit(LeafNode leafNode) {
        stack.push(leafNode);
    }

    public void visit(BranchNode branchNode) {
        Node right = stack.pop();
        Node left = stack.pop();
        branchNode.operate(left, right);
        stack.push(branchNode);
    }

    public Node pop() {
        return stack.pop();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public String toString() {
        return "OperatingStack{" +
                "stack=" + stack +
                '}';
    }
}
