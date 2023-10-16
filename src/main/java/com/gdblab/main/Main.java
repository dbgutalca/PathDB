/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import com.gdblab.parser.RPQGrammarBaseListener;
import com.gdblab.parser.RPQGrammarLexer;
import com.gdblab.parser.RPQGrammarParser;

/**
 *
 * @author ramhg
 */
public class Main {
    public static void main(String[] args){

        String rpq = "";

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Ingrese RPQ > ");
            rpq = br.readLine();
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception e) {
        }
        
        // CharStream input = CharStreams.fromString("!c.(a|b|c)*");
        CharStream input = CharStreams.fromString(rpq);
        RPQGrammarLexer lexer = new RPQGrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RPQGrammarParser parser = new RPQGrammarParser(tokens);
        parser.removeParseListeners();
        parser.addParseListener(new RPQGrammarBaseListener());

        RPQGrammarParser.QueryContext tree = parser.query();
        
        // new Database();
    }
    
}
