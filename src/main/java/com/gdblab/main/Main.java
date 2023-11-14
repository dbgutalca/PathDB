/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import com.gdblab.automata.RegexMatcher;
import com.gdblab.converter.RPQtoER;
import com.gdblab.database.Database;
import com.gdblab.parser.RPQGrammarBaseListener;
import com.gdblab.parser.RPQGrammarLexer;
import com.gdblab.parser.RPQGrammarParser;
import com.gdblab.schema.Graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author ramhg
 */
public class Main {

    public static Database db;
    public static void main(String[] args){
        String graphFileURL = null;
        String rpqValue = null;
        String algorithm = null;

        db = new Database("db.txt");

        RPQtoER converter = new RPQtoER("et_1.et_2*.et_3");
        String er = converter.Convert();
        System.out.println("ER: " + er);

        RegexMatcher rm = new RegexMatcher(er);
        System.out.println(rm.match("et_1et_2et_2et_2et_2et_3"));
        
        // CharStream input = CharStreams.fromString("(a.b*.c)");
        // RPQGrammarLexer lexer = new RPQGrammarLexer(input);
        // CommonTokenStream tokens = new CommonTokenStream(lexer);
        // RPQGrammarParser parser = new RPQGrammarParser(tokens);
        // parser.removeParseListeners();
        // parser.addParseListener(new RPQGrammarBaseListener());
        // RPQGrammarParser.QueryContext tree = parser.query();
    }
    
}
