package com.gdblab.algorithm.automata.tree.node;

import com.gdblab.algorithm.automata.automata.NFA;
import com.gdblab.algorithm.automata.stack.OperatingStack;
import com.gdblab.algorithm.automata.stack.ShuntingStack;

/**
 * Created on 2015/5/10.
 */
public class BConcat extends BranchNode {

    @Override
    public String toString() {
        return "[C]";
    }

    @Override
    public void accept(NFA nfa) {
        nfa.visit(this);
    }

    @Override

    public Node copy() {
        return new BConcat();
    }

    @Override
    public void accept(OperatingStack operatingStack) {
        operatingStack.visit(this);
    }

    @Override
    public void accept(ShuntingStack shuntingStack) {
        shuntingStack.visit(this);
    }

    @Override
    public int getPri() {
        return 1;
    }
}
