package com.gdblab.execution;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import com.gdblab.algebra.condition.First;
import com.gdblab.algebra.condition.Last;
import com.gdblab.algebra.parser.RPQExpression;
import com.gdblab.algebra.parser.RPQGrammarListener;
import com.gdblab.algebra.parser.impl.RPQtoAlgebraVisitor;
import com.gdblab.algebra.queryplan.logical.LogicalOperator;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpSelection;
import com.gdblab.algebra.queryplan.logical.visitor.LogicalToBFPhysicalVisitor;
import com.gdblab.algebra.queryplan.physical.PhysicalOperator;
import com.gdblab.algebra.queryplan.util.Utils;
import com.gdblab.algorithm.translator.RPQtoER;
import com.gdblab.algorithm.versions.v1.BFSRegex;
import com.gdblab.algorithm.versions.v1.DFSRegex;
import com.gdblab.algorithm.versions.v2.BFSAutomaton;
import com.gdblab.algorithm.versions.v2.DFSAutomaton;
import com.gdblab.graph.Graph;
import com.gdblab.graph.schema.Edge;
import com.gdlab.parser.RPQGrammarLexer;
import com.gdlab.parser.RPQGrammarParser;

public final class Execute {

    private static final String prefix = "/";

    public static void EvalRPQWithAlgebra(){

        boolean isExperimental = Context.getInstance().isExperimental();

        long start = System.nanoTime();

        int counter = 1;

        RPQGrammarLexer lexer = new RPQGrammarLexer(CharStreams.fromString(Context.getInstance().getRPQ()));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RPQGrammarParser parser = new RPQGrammarParser(tokens);
        ParseTreeWalker walker = new ParseTreeWalker();
        RPQGrammarListener listener = new RPQGrammarListener();
        walker.walk(listener, parser.query());

        RPQExpression rpqExp = listener.getRoot();
        RPQtoAlgebraVisitor visitor = new RPQtoAlgebraVisitor();
        rpqExp.acceptVisit(visitor);

        LogicalOperator lo = visitor.getRoot();

        // Adding filter on top of the logical operator
        if ( !Context.getInstance().getStartNode().equalsIgnoreCase("") ) {
            lo = filterOnTopLeft(lo);
        }

        if ( !Context.getInstance().getEndNode().equalsIgnoreCase("") ) {
            lo = filterOnTopRight(lo);
        }

        LogicalToBFPhysicalVisitor visitor2 = new LogicalToBFPhysicalVisitor();
        lo.acceptVisitor(visitor2);
        PhysicalOperator po = visitor2.getPhysicalPlan().getRootOperator();

        if (isExperimental) {
            Context.getInstance().setResultFilename("results_" + Context.getInstance().getRPQFileName() + "_" + Context.getInstance().getNumber() + ".txt");
            Utils.writeAndCountPaths(po);
            long end = System.nanoTime();
            Context.getInstance().setTime(Utils.getTime(start, end));
            Utils.writeTotalAndTime();
        }
        else {
            counter = Utils.printAndCountPaths(po);
            long end = System.nanoTime();
            System.out.println("\nTotal paths: " + (counter - 1) + " paths");
            System.out.println("Execution time: " + Utils.getTime(start, end) + " seconds");
            System.out.println("");
        }
    }

    public static void EvalRPQWithRegexDFS() {
        long start = System.nanoTime();

        String er = RPQtoER.Translate(Context.getInstance().getRPQ());

        DFSRegex dfsRegex = new DFSRegex(er);
        if (Context.getInstance().isExperimental()) {
            String filename = "results_" + Context.getInstance().getRPQFileName() + "_" + Context.getInstance().getNumber() + ".txt";
            Context.getInstance().setResultFilename(filename);

            Writer writer = null;
            
            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Context.getInstance().getResultFilename()), "utf-8"));
                Utils.writeConfig(writer);
            } catch (Exception e) {
            } finally { try { writer.close(); } catch (Exception e) {} }

            dfsRegex.Trail();

            long end = System.nanoTime();
            Context.getInstance().setTime(Utils.getTime(start, end));
            Utils.writeTotalAndTime();
        }
        else {
            dfsRegex.Trail();
            long end = System.nanoTime();
            System.out.println("\nTotal paths: " + dfsRegex.getTotalPaths() + " paths");
            System.out.println("Execution time: " + Utils.getTime(start, end) + " seconds");
            System.out.println("");
        }
    }

    public static void EvalRPQWithRegexBFS() {
        long start = System.nanoTime();

        String er = RPQtoER.Translate(Context.getInstance().getRPQ());

        BFSRegex bfsRegex = new BFSRegex(er);

        if (Context.getInstance().isExperimental()) {
            String filename = "results_" + Context.getInstance().getRPQFileName() + "_" + Context.getInstance().getNumber() + ".txt";
            Context.getInstance().setResultFilename(filename);

            Writer writer = null;

            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Context.getInstance().getResultFilename()), "utf-8"));
                Utils.writeConfig(writer);
            } catch (Exception e) {
            } finally {
                try { writer.close(); } catch (Exception e) {}
            }

            bfsRegex.Trail();

            long end = System.nanoTime();
            Context.getInstance().setTime(Utils.getTime(start, end));
            Utils.writeTotalAndTime();
        }
        else {
            bfsRegex.Trail();
            long end = System.nanoTime();
            System.out.println("\nTotal paths: " + bfsRegex.getTotalPaths() + " paths");
            System.out.println("Execution time: " + Utils.getTime(start, end) + " seconds");
            System.out.println("");
        }
    }

    public static void EvalRPQWithAutomatonDFS() {
        long start = System.nanoTime();

        String er = RPQtoER.Translate(Context.getInstance().getRPQ());

        DFSAutomaton dfsAutomaton = new DFSAutomaton(er);

        if (Context.getInstance().isExperimental()) {
            String filename = "results_" + Context.getInstance().getRPQFileName() + "_" + Context.getInstance().getNumber() + ".txt";
            Context.getInstance().setResultFilename(filename);

            Writer writer = null;

            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Context.getInstance().getResultFilename()), "utf-8"));
                Utils.writeConfig(writer);
            } catch (Exception e) {
            } finally {
                try { writer.close(); } catch (Exception e) {}
            }

            dfsAutomaton.Trail();

            long end = System.nanoTime();
            Context.getInstance().setTime(Utils.getTime(start, end));
            Utils.writeTotalAndTime();

        } else {
            dfsAutomaton.Trail();
            long end = System.nanoTime();
            System.out.println("\nTotal paths: " + dfsAutomaton.getTotalPaths() + " paths");
            System.out.println("Execution time: " + Utils.getTime(start, end) + " seconds");
            System.out.println("");
        }
    }

    public static void EvalRPQWithAutomatonBFS() {
        long start = System.nanoTime();

        String er = RPQtoER.Translate(Context.getInstance().getRPQ());

        BFSAutomaton bfsAutomaton = new BFSAutomaton(er);

        if (Context.getInstance().isExperimental()) {
            String filename = "results_" + Context.getInstance().getRPQFileName() + "_" + Context.getInstance().getNumber() + ".txt";
            Context.getInstance().setResultFilename(filename);

            Writer writer = null;

            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Context.getInstance().getResultFilename()), "utf-8"));
                Utils.writeConfig(writer);
            } catch (Exception e) {
            } finally {
                try { writer.close(); } catch (Exception e) {}
            }

            bfsAutomaton.Trail();

            long end = System.nanoTime();
            Context.getInstance().setTime(Utils.getTime(start, end));
            Utils.writeTotalAndTime();

        } else {
            bfsAutomaton.Trail();
            long end = System.nanoTime();
            System.out.println("\nTotal paths: " + bfsAutomaton.getTotalPaths() + " paths");
            System.out.println("Execution time: " + Utils.getTime(start, end) + " seconds");
            System.out.println("");
        }
    }

    private static LogicalOperator filterOnTopLeft(LogicalOperator lo) {
        return new LogicalOpSelection(lo, new First(Context.getInstance().getStartNode()));
    }

    private static LogicalOperator filterOnTopRight(LogicalOperator lo) {
        return new LogicalOpSelection(lo, new Last(Context.getInstance().getEndNode()));
    }

    public static void interactive(String[] args) {
        try {
            Terminal terminal = TerminalBuilder.terminal();
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

            Tools.clearConsole();

            if (args.length == 0) {
                Tools.showUsageNoArgs();
                Tools.loadDefaultGraph();
            }
            else {
                Tools.showUsageArgs(args[0], args[1]);
                Tools.loadCustomGraphFiles(args[0], args[1]);
            }
            
            
            String prompt = "PathDB> ";

            while (true) {
                String line = reader.readLine(prompt);

                if (line == null || line.equalsIgnoreCase(prefix + "q") ) {
                    System.out.println("Terminated execution.");
                    break;
                }

                reader.getHistory().add(line);

                if (line.startsWith(prefix + "h ")) {
                    // clearConsole();
                    Tools.showHelp();
                    System.out.println();
                    continue;
                }

                if (line.startsWith(prefix + "m ")) {
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
                    Tools.showSelectedMethod(Integer.parseInt(parts[1]));
                    continue;
                }

                if (line.startsWith(prefix + "f ")) {
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
                    else {
                        Context.getInstance().setRPQ(rpq);
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
                            Execute.EvalRPQWithAlgebra();
                            break;
                        case 2:
                            Execute.EvalRPQWithRegexDFS();
                            break;
                        case 3:
                            Execute.EvalRPQWithRegexBFS();
                            break;
                        case 4:
                            Execute.EvalRPQWithAutomatonDFS();
                            break;
                        case 5:
                            Execute.EvalRPQWithAutomatonBFS();
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

    public static void experimental(String[] args) {
        Context.getInstance().setExperimental(true);
        
        String nodes_file = args[0];
        String edges_file = args[1];
        String rpqs_file = args[2];

        int lineNumber;     // 1st data in line
        int method;         // 2nd data in line
        int fixpoint;       // 3rd data in line
        String sn;          // 4th data in line
        String rpq;         // 5th data in line
        String en;          // 6th data in line

        Tools.loadCustomGraphFiles(nodes_file, edges_file);

        ArrayList<String> rpqs = Tools.readRPQsFromFile(rpqs_file);

        Context.getInstance().setRPQFileName(rpqs_file);

        for (String line : rpqs) {
            String[] data = line.split(",");

            try {
                lineNumber = Integer.parseInt(data[0]);
                Context.getInstance().setNumber(lineNumber);
            } catch (Exception e) {
                continue;
            }

            try {
                method = Integer.parseInt(data[1]);
                Context.getInstance().setMethod(method);
                if (method < 1 || method > 5) continue;
            } catch (Exception e) {
                continue;
            }

            try {
                fixpoint = Integer.parseInt(data[2]);
                Context.getInstance().setFixPoint(fixpoint);
                if (fixpoint < 0) continue;
            } catch (Exception e) {
                continue;
            }

            sn = data[3];
            rpq = data[4].trim();
            en = data[5].replaceAll(";", "");

            
            if (!rpq.isEmpty()) Context.getInstance().setRPQ(rpq);
            else continue;

            if (!sn.isEmpty()) Context.getInstance().setStartNode(sn);
            else Context.getInstance().setStartNode("");

            if (!en.isEmpty()) Context.getInstance().setEndNode(en);
            else Context.getInstance().setEndNode("");

            switch (method) {
                case 1:
                    Execute.EvalRPQWithAlgebra();
                    break;
                case 2:
                    Execute.EvalRPQWithRegexDFS();
                    break;
                case 3:
                    Execute.EvalRPQWithRegexBFS();
                    break;
                case 4:
                    Execute.EvalRPQWithAutomatonDFS();
                    break;
                case 5:
                    Execute.EvalRPQWithAutomatonBFS();
                    break;
            }
        }
    }
}
