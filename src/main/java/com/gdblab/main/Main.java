package com.gdblab.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import org.jline.reader.*;
import org.jline.terminal.*;

import com.gdblab.execution.Context;
import com.gdblab.execution.Execute;
import com.gdblab.graph.DefaultGraph;
import com.gdblab.graph.Graph;
import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.Node;

public class Main {

    private static String prefix = "/";

    public static void init() {
        System.out.println();
        String[] logo = {
        "       ╔═════════════════════════════════════════════════════╗",
        "       ║    _____            _     _       _____    ____     ║",
        "       ║   |  __ \\          | |   | |     |  __ \\  |  _ \\    ║",
        "       ║   | |__) |   __ _  | |_  | |__   | |  | | | |_) |   ║",
        "       ║   |  ___/   / _` | | __| | '_ \\  | |  | | |  _ <    ║",
        "       ║   | |      | (_| | | |_  | | | | | |__| | | |_) |   ║",
        "       ║   |_|       \\__,_|  \\__| |_| |_| |_____/  |____/    ║",
        "       ║                                                v1.0 ║",
        "       ╚═════════════════════════════════════════════════════╝"
        };

        for (String u : logo) {
            System.out.println(u);
        }
        System.out.println("\n");
    }

    public static void usageNoArgs() {
        String[] usage = {
            "Welcome to PathDB! A tool to evaluate Regular Path Queries in Graphs.",
            "",
            "No graph loaded. Using default graph.",
            "If you want to use a custom graph, run the program with the following arguments: ",
            "java -jar PathDB.jar -nf=nodes_file.txt -ef=edges_file.txt",
            "",
            "For help, type /h."
        };

        for (String u : usage) {
            System.out.println(u);
        }
        System.out.println();
    }

    public static void usageArgs(String nf, String ef) {
        String[] usage = {
            "Welcome to PathDB! A tool to evaluate Regular Path Queries in Graphs.",
            "",
            "Graph loaded successfully. Using " + nf + " and " + ef + " files.",
            "If you want to use a custom graph, run the program with the following arguments: ",
            "java -jar PathDB.jar -nf=nodes_file.txt -ef=edges_file.txt",
            "",
            "For help, type /h."
        };

        for (String u : usage) {
            System.out.println(u);
        }
        System.out.println();
    }

    public static void help() {
        String[] help = {
            "List of available commands:",
            "   /h              Show this help.",
            "   /m <1-5>        Select evaluation method.",
            "                       1 - Algebra (Default).",
            "                       2 - Regex + DFS.",
            "                       3 - Regex + BFS.",
            "                       4 - Automaton + DFS.",
            "                       5 - Automaton + BFS.",
            "   /f #            Set the fix point of recursion loops (Default is 3).",
            "   (S, RPQ, E);    Query to evaluate.",
            "                       S = Start Node.",
            "                       RPQ = Regular Path Query.",
            "                       E = End Node.",
            "                       Example: (N1,(A?.B+)*,N2);",
            "   /i              Show the information of the graph.",
            "   /s              Show a sample of each label in the graph.",
            "   /q              Quit the program."
        };
        for (String u : help) {
            System.out.println(u);
        }
        System.out.println();
    }

    public static void showMethods() {
        String[] methods = {
            "List of available methods:",
            "   1 - Algebra",
            "   2 - Regex + DFS",
            "   3 - Regex + BFS",
            "   4 - Automaton + DFS",
            "   5 - Automaton + BFS"
        };
        for (String u : methods) {
            System.out.println(u);
        }
        System.out.println();
    }
    
    public static void showSelectedMethod(Integer m) {
        switch (m) {
            case 1:
                System.out.println("Selected method: Algebra\n");
                break;

            case 2:
                System.out.println("Selected method: Regex + DFS\n");
                break;

            case 3:
                System.out.println("Selected method: Regex + BFS\n");
                break;

            case 4:
                System.out.println("Selected method: Automaton + DFS\n");
                break;

            case 5:
                System.out.println("Selected method: Automaton + BFS\n");
                break;
        }
    }

    private static void loadCustomGraphFiles(String nodesFile, String edgesFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(nodesFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Node node = new Node(data[0], data[1]);
                Graph.getGraph().insertNode(node);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(edgesFile))) {
            int i = 1;
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Edge edge = new Edge(
                    "E" + i,
                    data[1],
                    Graph.getGraph().getNode(data[0]),
                    Graph.getGraph().getNode(data[2])
                );
                Graph.getGraph().insertEdge(edge);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        
        try {
            Terminal terminal = TerminalBuilder.terminal();
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

            clearConsole();
            if (args.length == 0) {
                DefaultGraph.loadGraph();
                usageNoArgs();
            }
            else {
                if (args.length != 2) {
                    System.out.println("Invalid arguments. Use -nf=nodes_file.txt -ef=edges_file.txt");
                    System.exit(1);
                }
                else {
                    if (args[0].startsWith("-nf=") && args[1].startsWith("-ef=")) {
                        loadCustomGraphFiles(args[0], args[1]);
                        usageArgs(args[0].split("=")[1], args[1].split("=")[1]);
                    }
                    else {
                        System.out.println("Invalid arguments. Use -nf=nodes_file.txt -ef=edges_file.txt");
                        System.exit(1);
                    }
                }
            }
            
            String prompt = "PathDB> ";

            while (true) {
                String line = reader.readLine(prompt);

                if (line == null || line.equalsIgnoreCase(prefix + "q") ) {
                    System.out.println("Terminated execution.");
                    break;
                }

                reader.getHistory().add(line);

                if (line.startsWith(prefix + "h")) {
                    // clearConsole();
                    help();
                    System.out.println();
                    continue;
                }

                if (line.startsWith(prefix + "m")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        // clearConsole();
                        System.out.println("Invalid command. Use /m # to select a method.\n");
                        continue;
                    }

                    try {
                        Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        // clearConsole();
                        System.out.println("Invalid command. # must be a positive number greater than 0.\n");
                        continue;
                    }
                    
                    if (Integer.parseInt(parts[1]) < 1 || Integer.parseInt(parts[1]) > 5) {
                        // clearConsole();
                        System.out.println("Invalid command. # must be a number between 1 and 5.\n");
                        continue;
                    }
                    
                    Context.getInstance().setMethod(Integer.parseInt(parts[1]));
                    showSelectedMethod(Integer.parseInt(parts[1]));
                    continue;
                }

                if (line.startsWith(prefix + "f")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        // clearConsole();
                        System.out.println("Invalid command. Use /f # to set a fix point.\n");
                        continue;
                    }

                    try {
                        Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        // clearConsole();
                        System.out.println("Invalid command. # must be a positive number greater than 0.\n");
                        continue;
                    }

                    Context.getInstance().setFixPoint(Integer.parseInt(parts[1]));
                    System.out.println("Fix point set to: " + parts[1] + " iteration(s).\n");
                    continue;
                }

                if (line.startsWith(prefix + "i")) {
                    // clearConsole();
                    System.out.println("Graph Information:");
                    System.out.println("Total nodes: " + Graph.getGraph().getNodesQuantity());
                    System.out.println("Total edges: " + Graph.getGraph().getEdgesQuantity());
                    System.out.println("Total label: " + Graph.getGraph().getDifferetEdgesQuantity());
                    System.out.println("Edges per label: " + Graph.getGraph().getEdgesByLabelQuantity().toString());
                    System.out.println("\n");
                    continue;
                }
                
                if (line.startsWith(prefix + "s")) {
                    System.out.println("Samples: ");
                    ArrayList<Edge> edges = Graph.getGraph().getSampleOfEachlabel();
                    for (Edge e : edges) {
                       System.out.println(e.getId() + ": " + e.getSource().getId() + "," + e.getLabel() + "," + e.getTarget().getId());
                    }
                    System.out.println("\n");
                    continue;
                }

                if (line.endsWith(";")) {

                    String[] parts = line.substring(0, line.length() - 1).split(",");

                    String sn = parts[0].substring(1);
                    String rpq = parts[1];
                    String en = parts[2].substring(0, parts[2].length() - 1);

                    if (rpq.isEmpty()) {
                        System.out.println("Invalid command. Query cannot be empty.\n");
                        continue;
                    }

                    if (!sn.isEmpty()) {
                        Context.getInstance().setStartNode(sn);
                    }
                    else {
                        Context.getInstance().setStartNode("");
                    }

                    if (!en.isEmpty()) {
                        Context.getInstance().setEndNode(en);
                    }
                    else {
                        Context.getInstance().setEndNode("");
                    }

                    // Switch del method
                    switch (Context.getInstance().getMethod()) {
                        case 1:
                            Execute.EvalRPQWithAlgebra(rpq);
                            break;
                        case 2:
                            System.out.println("Regex + DFS");
                            Execute.EvalRPQWithRegexDFS(rpq);
                            break;
                        case 3:
                            System.out.println("Regex + BFS");
                            Execute.EvalRPQWithRegexBFS(rpq);
                            break;
                        case 4:
                            System.out.println("Automaton + DFS");
                            Execute.EvalRPQWithAutomatonDFS(rpq);
                            break;
                        case 5:
                            System.out.println("Automaton + BFS");
                            Execute.EvalRPQWithAutomatonBFS(rpq);
                            break;
                    
                        default:
                            break;
                    }
                    
                    System.out.println();
                    continue;

                }

                System.out.println("Invalid command. For help, type /h.\n\n");

            }

        } catch (Exception e) {
            System.out.println("Terminated execution.");
        }
            
    }

    
    private static void clearConsole() {
        try {
            String os = System.getProperty("os.name");
    
            if (os.contains("Windows")) {
                // Ejecutar comando cls en Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Secuencia de escape ANSI para limpiar la consola en Linux/Unix/MacOS
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
            init();
        } catch (Exception e) {}
    }
    
}
