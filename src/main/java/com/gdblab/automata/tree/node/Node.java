package com.gdblab.automata.tree.node;

import com.gdblab.automata.automata.NFA;
import com.gdblab.automata.stack.OperatingStack;
import com.gdblab.automata.stack.ShuntingStack;

/**
 * Created on 5/5/15.
 */
public abstract class Node {

    private Node left;
    private Node right;

    public Node() {
        left = right = null;
    }

    public Node right() {
        return right;
    }

    public Node left() {
        return left;
    }

    public boolean hasLeft() {
        return left != null;
    }

    public boolean hasRight() {
        return right != null;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public abstract void accept(NFA nfa);

    public abstract Node copy();

    public abstract void accept(OperatingStack operatingStack);

    public abstract void accept(ShuntingStack shuntingStack);

    @Override
    public abstract String toString();
}
