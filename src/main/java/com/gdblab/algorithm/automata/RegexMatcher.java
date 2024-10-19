package com.gdblab.algorithm.automata;

import com.gdblab.algorithm.automata.automata.DFA;
import com.gdblab.algorithm.automata.automata.NFA;
import com.gdblab.algorithm.automata.tree.SyntaxTree;

/**
 * Created on 2015/5/11.
 */
public class RegexMatcher {
    private int[][] transitionTable;
    private int is;
    private int rs;
    private boolean[] fs;

    private int state;
    private int lastState;

    public RegexMatcher(String regex) {
        compile(regex);
    }

    private void compile(String regex) {
        SyntaxTree syntaxTree = new SyntaxTree(regex);
        NFA nfa = new NFA(syntaxTree.getRoot());
        DFA dfa = new DFA(nfa.asBitmapStateManager());
        transitionTable = dfa.getTransitionTable();
        is = dfa.getInitState();
        fs = dfa.getFinalStates();
        rs = dfa.getRejectedState();
        state = is;
        lastState = is;
    }

    public String match(String str) {
        int s = is;
        for (int i = 0, length = str.length(); i < length; i++) {
            char ch = str.charAt(i);
            s = transitionTable[s][ch];
            if (s == rs) {
                return "Rejected";
            }
        }
        return fs[s] ? "Accepted" : "Substring";
    }

    public int next(char c){
        lastState = state;
        state = transitionTable[state][c];
        return state;
    }
    
    public int back(){
        state = lastState;
        return state;
    }

    public void printTransitionTable() {
        System.out.println("Transition Table:");
        for (int i = 0, length = transitionTable.length; i < length; i++) {
            for (int j = 0; j < 128; j++) {
                System.out.print(transitionTable[i][j] + " ");
            }
            System.out.println();
        }
    }


    /**
     * La tabla de transicion al parecer trabaja con 3 estados, 0 es final, 1 es rechazado y 2 es aceptado
    */
}
