/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.main;

import com.gdblab.algorithms.BFS;
import com.gdblab.algorithms.DFSAutomata;
import com.gdblab.converter.Converter;
import com.gdblab.database.Database;
import com.gdblab.schema.GraphObject;
import com.gdblab.schema.Path;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import java.util.ArrayList;

/**
 *
 * @author ramhg
 */
public class Main {
                                                                                                                                                            
    private static Converter converter = new Converter();
    private static String regex = "";
    public static Database db;

    public static String algorithm = "";
    public static String dburl = "";
    public static String semantic = "";
    public static String rpq = "";
    public static String output = "";

    public static void main(String[] args){

        for (String string : args) {
            String[] arg = string.split("=");
            switch (arg[0]) {
                case "-algorithm":
                    algorithm = arg[1];
                    break;
                case "-database":
                    dburl = arg[1];
                    break;
                case "-semantic":
                    semantic = arg[1];
                    break;
                case "-rpq":
                    rpq = arg[1];
                    break;
                case "-output":
                    output = arg[1];
                    break;
            }
        }

        if (algorithm.equals("") || dburl.equals("") || semantic.equals("") || rpq.equals("") || output.equals("")) {
            System.out.println("Error: Missing arguments");
            System.out.println("Usage: java -jar rpq.jar -algorithm=[alg|dfs|bfs] -database=[dburl] -semantic=[sp|t|a] -rpq=[rpq] -output=[outputurl]");
            System.exit(0);
        }

        db = new Database(dburl);

        switch (algorithm) {
            case "alg":
                switch (algorithm) {
                    case "alg":
                        algorithm = "Algebra";
                        break;
                    case "dfs":
                        algorithm = "DFS";
                        break;
                    case "bfs":
                        algorithm = "BFS";
                        break;
                }
                switch (semantic) {
                    case "sp":
                        semantic = "Simple Path";
                        break;
                    case "t":
                        semantic = "Trail";
                        break;
                    case "a":
                        semantic = "Arbitrary";
                        break;
                }
                printConfiguration(algorithm, semantic, dburl, rpq);
                System.out.println(ANSI_BLUE + ANSI_BOLD + ANSI_ITALIC + "Cargando..." + ANSI_RESET);
                System.out.println();
                CharStream input = CharStreams.fromString(rpq);
                //RPQGrammarLexer lexer = new RPQGrammarLexer(input);
                //CommonTokenStream tokens = new CommonTokenStream(lexer);
                //RPQGrammarParser parser = new RPQGrammarParser(tokens);
                //parser.removeParseListeners();
                //parser.addParseListener(new RPQGrammarBaseListener());
                //RPQGrammarParser.QueryContext tree = parser.query();



                break;
            case "dfs":
                regex = converter.RPQtoER(rpq);
                switch (semantic) {
                    case "sp":
                        semantic = "Simple Path";
                        break;
                    case "t":
                        semantic = "Trail";
                        break;
                    case "a":
                        semantic = "Arbitrary";
                        break;
                }
                printConfiguration(algorithm, semantic, dburl, rpq, regex);
                System.out.println(ANSI_BLUE + ANSI_BOLD + ANSI_ITALIC + "Cargando..." + ANSI_RESET);
                System.out.println();
                DFSAutomata dfs = new DFSAutomata(db, semantic, regex);
                dfs.printPath(output);
                break;
            case "bfs":
                regex = converter.RPQtoER(rpq);
                switch (semantic) {
                    case "sp":
                        semantic = "Simple Path";
                        break;
                    case "t":
                        semantic = "Trail";
                        break;
                    case "a":
                        semantic = "Arbitrary";
                        break;
                }
                // printConfiguration(algorithm, semantic, dburl, rpq, regex);
                System.out.println(ANSI_BLUE + ANSI_BOLD + ANSI_ITALIC + "Cargando..." + ANSI_RESET);
                System.out.println();
                BFS bfs = new BFS(db, semantic, regex);
                bfs.printCompletePaths();
                break;
            default:
                System.out.println("Error: Invalid algorithm");
                System.exit(0);
        }
    }

    private static void printConfiguration(String algorithm, String semantic, String dburl, String rpq) {
        switch (algorithm) {
            case "alg":
                algorithm = "Algebra";
                break;
            case "dfs":
                algorithm = "DFS";
                break;
            case "bfs":
                algorithm = "BFS";
                break;
        }
        switch (semantic) {
            case "sp":
                semantic = "Simple Path";
                break;
            case "t":
                semantic = "Trail";
                break;
            case "a":
                semantic = "Arbitrary";
                break;
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println(ANSI_BLUE + ANSI_BOLD + "CONFIGURATION" + ANSI_RESET);
        System.out.println(ANSI_BLUE + ANSI_ITALIC + "Algorithm: " + algorithm + ANSI_RESET);
        System.out.println(ANSI_BLUE + ANSI_ITALIC + "Semantic: " + semantic + ANSI_RESET);
        System.out.println(ANSI_BLUE + ANSI_ITALIC + "Database: " + dburl + ANSI_RESET);
        System.out.println(ANSI_BLUE + ANSI_ITALIC + "RPQ: " + rpq + ANSI_RESET);
        System.out.println();
    }

    private static void printConfiguration(String algorithm, String semantic, String dburl, String rpq, String regex) {
        switch (algorithm) {
            case "alg":
                algorithm = "Algebra";
                break;
            case "dfs":
                algorithm = "DFS";
                break;
            case "bfs":
                algorithm = "BFS";
                break;
        }
        switch (semantic) {
            case "sp":
                semantic = "Simple Path";
                break;
            case "t":
                semantic = "Trail";
                break;
            case "a":
                semantic = "Arbitrary";
                break;
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println(ANSI_BLUE + ANSI_BOLD + "CONFIGURATION" + ANSI_RESET);
        System.out.println(ANSI_BLUE + ANSI_ITALIC + "Algorithm: " + algorithm + ANSI_RESET);
        System.out.println(ANSI_BLUE + ANSI_ITALIC + "Semantic: " + semantic + ANSI_RESET);
        System.out.println(ANSI_BLUE + ANSI_ITALIC + "Database: " + dburl + ANSI_RESET);
        System.out.println(ANSI_BLUE + ANSI_ITALIC + "RPQ: " + rpq + ANSI_RESET);
        System.out.println(ANSI_BLUE + ANSI_ITALIC + "Regex: " + regex + ANSI_RESET);
        System.out.println();
    }

    public static void printPath(ArrayList<Path> paths) {
		for (Path pp : paths) {
			System.out.print(pp.getId() + " : ");
			for (GraphObject go : pp.getSequence()){

				if(go.getId().startsWith("e"))
					System.out.print(go.getLabel() + " ");
				
				else
					System.out.print(go.getId() + " ");
				
			}
			System.out.println("");
		}
	}

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_ITALIC = "\u001B[3m";
}
