/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.main;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import com.gdblab.database.Database;
import com.gdblab.parser.RPQGrammarBaseListener;
import com.gdblab.parser.RPQGrammarLexer;
import com.gdblab.parser.RPQGrammarParser;

/**
 *
 * @author ramhg
 */
public class Main {
    public static void main(String[] args){

        CharStream input = CharStreams.fromString("(a+.(c+.b?)).b*");
        RPQGrammarLexer lexer = new RPQGrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RPQGrammarParser parser = new RPQGrammarParser(tokens);
        parser.removeParseListeners();
        parser.addParseListener(new RPQGrammarBaseListener());

        RPQGrammarParser.QueryContext tree = parser.query();
        
        // new Database();
    }
    
}
