package com.gdblab.automata.tree.node;

import com.gdblab.automata.automata.NFA;
import com.gdblab.automata.stack.OperatingStack;
import com.gdblab.automata.stack.ShuntingStack;

/**
 * Created on 2015/5/10.
 */
public class LClosure extends LeafNode {
    @Override
    public void accept(NFA nfa) {
        nfa.visit(this);
    }

    @Override
    public Node copy() {
        return new LClosure();
    }

    @Override
    public String toString() {
        return "{ε}";
    }

    @Override
    public void accept(OperatingStack operatingStack) {
        operatingStack.visit(this);
    }

    @Override
    public void accept(ShuntingStack shuntingStack) {
        shuntingStack.visit(this);
    }
}
