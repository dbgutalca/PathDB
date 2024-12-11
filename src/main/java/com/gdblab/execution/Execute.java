package com.gdblab.execution;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
import com.gdblab.algebra.queryplan.logical.visitor.PredicatePushdownLogicalPlanVisitor;
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

            if (Context.getInstance().isOptimize()) {
                PredicatePushdownLogicalPlanVisitor v = new PredicatePushdownLogicalPlanVisitor();
                lo.acceptVisitor(v);
                lo = v.getRoot();
            }
        }

        if ( !Context.getInstance().getEndNode().equalsIgnoreCase("") ) {
            lo = filterOnTopRight(lo);

            if (Context.getInstance().isOptimize()) {
                PredicatePushdownLogicalPlanVisitor v = new PredicatePushdownLogicalPlanVisitor();
                lo.acceptVisitor(v);
                lo = v.getRoot();
            }
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

        if (Context.getInstance().isExperimental()) {
            String filename = "results_" + Context.getInstance().getRPQFileName() + "_" + Context.getInstance().getNumber() + ".txt";
            Context.getInstance().setResultFilename(filename);

            Writer writer = null;
            
            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Context.getInstance().getResultFilename()), "utf-8"));
                Utils.writeConfig(writer);

                DFSRegex dfsRegex = new DFSRegex(er, writer);
                dfsRegex.Trail();
                Context.getInstance().setTotalPaths(dfsRegex.getTotalPaths());

                writer.close();

                long end = System.nanoTime();
                Context.getInstance().setTime(Utils.getTime(start, end));
                Utils.writeTotalAndTime();
            } catch (Exception e) {
            }
        }
        else {
            DFSRegex dfsRegex = new DFSRegex(er);
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

        if (Context.getInstance().isExperimental()) {
            String filename = "results_" + Context.getInstance().getRPQFileName() + "_" + Context.getInstance().getNumber() + ".txt";
            Context.getInstance().setResultFilename(filename);

            Writer writer = null;

            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Context.getInstance().getResultFilename()), "utf-8"));
                Utils.writeConfig(writer);

                BFSRegex bfsRegex = new BFSRegex(er, writer);
                bfsRegex.Trail();
                Context.getInstance().setTotalPaths(bfsRegex.getTotalPaths());

                writer.close();

                long end = System.nanoTime();
                Context.getInstance().setTime(Utils.getTime(start, end));
                Utils.writeTotalAndTime();
            } catch (Exception e) {}

            
        }
        else {
            BFSRegex bfsRegex = new BFSRegex(er);
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

        if (Context.getInstance().isExperimental()) {
            String filename = "results_" + Context.getInstance().getRPQFileName() + "_" + Context.getInstance().getNumber() + ".txt";
            Context.getInstance().setResultFilename(filename);

            Writer writer = null;

            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Context.getInstance().getResultFilename()), "utf-8"));
                Utils.writeConfig(writer);

                DFSAutomaton dfsAutomaton = new DFSAutomaton(er, writer);
                dfsAutomaton.Trail();
                Context.getInstance().setTotalPaths(dfsAutomaton.getTotalPaths());

                writer.close();

                long end = System.nanoTime();
                Context.getInstance().setTime(Utils.getTime(start, end));
                Utils.writeTotalAndTime();
            } catch (Exception e) {}

        } else {
            DFSAutomaton dfsAutomaton = new DFSAutomaton(er);
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

        if (Context.getInstance().isExperimental()) {
            String filename = "results_" + Context.getInstance().getRPQFileName() + "_" + Context.getInstance().getNumber() + ".txt";
            Context.getInstance().setResultFilename(filename);

            Writer writer = null;

            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Context.getInstance().getResultFilename()), "utf-8"));
                Utils.writeConfig(writer);

                BFSAutomaton bfsAutomaton = new BFSAutomaton(er, writer);
                bfsAutomaton.Trail();
                Context.getInstance().setTotalPaths(bfsAutomaton.getTotalPaths());

                writer.close();

                long end = System.nanoTime();
                Context.getInstance().setTime(Utils.getTime(start, end));
                Utils.writeTotalAndTime();
            } catch (Exception e) {}

           

        } else {
            BFSAutomaton bfsAutomaton = new BFSAutomaton(er);
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

                if (line.equals(prefix + "h")) {
                    // clearConsole();
                    Tools.showHelp();
                    System.out.println();
                    continue;
                }

                else if (line.startsWith(prefix + "m ")) {
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

                else if (line.startsWith(prefix + "f ")) {
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

                else if (line.startsWith(prefix + "s ")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        System.out.println("Invalid command. Use /e <1-3> to set a semantic.\n");
                        continue;
                    }

                    try {
                        Integer.parseInt(parts[1]);
                    } catch (Exception e) {
                        System.out.println("Invalid command. Use /e <1-3> to set a semantic.\n");
                        continue;
                    }

                    if (Integer.parseInt(parts[1]) < 1 || Integer.parseInt(parts[1]) > 3) {
                        System.out.println("Invalid command. Use /e <1-3> to set a semantic.\n");
                        continue;
                    }

                    Context.getInstance().setSemantic(Integer.parseInt(parts[1]));
                    Tools.showSelectedSemantic(Integer.parseInt(parts[1]));
                    continue;
                }

                else if (line.endsWith(";")) {

                    String[] parts = line.substring(0, line.length() - 1).split(",");

                    if (parts.length != 3) {
                        System.out.println("Invalid command. Use '(S,RQP,E);' to execute a RPQ.\n");
                        continue;
                    }

                    String sn = parts[0].replaceAll("\\(", "");
                    if (sn.isEmpty()) Context.getInstance().setStartNode("");
                    else Context.getInstance().setStartNode(sn);

                    String rpq = parts[1];
                    if (rpq.isEmpty()) {
                        System.out.println("Invalid command. Must include the RPQ. Use '(S,RQP,E);' to execute a RPQ.\n");
                        continue;
                    }
                    Context.getInstance().setRPQ(rpq);

                    String en = parts[2].replaceAll("\\)", "");
                    if (en.isEmpty()) Context.getInstance().setEndNode("");
                    else Context.getInstance().setEndNode(en);

                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    Future<Void> future = executor.submit(new runExecution());

                    try {
                        future.get(Context.getInstance().getTimeoutDuration(), Context.getInstance().getTimeoutTimeUnit());
                    }
                    catch (Exception e) {
                        System.out.println("Execution timed out.\n");
                        future.cancel(true);
                    }

                    executor.shutdownNow();
                }

                else if (line.startsWith(prefix + "t ")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        System.out.println("Invalid command. Use /t <#><U> to set a timeout.\n");
                        continue;
                    }

                    if (parts[1].equals("0")) {
                        Context.getInstance().setTimeoutDuration(Integer.MAX_VALUE);
                        Context.getInstance().setTimeoutTimeUnit(TimeUnit.DAYS);
                        System.out.println("Timeout disabled.\n");
                        continue;
                    }

                    String number = parts[1].substring(0, parts[1].length() - 1);
                    String unit = parts[1].substring(parts[1].length() - 1);

                    try { Integer.parseInt(number); }
                    catch (Exception e) {
                        System.out.println("Invalid command. Use /t <#><U> to set a timeout. # must be a number.\n");
                        continue;
                    }

                    if ( Integer.parseInt(number) < 1)  {
                        System.out.println("Invalid command. Use /t <#><U> to set a timeout. # must be greater than 0.\n");
                        continue;
                    }

                    switch (unit) {
                        case "S": 
                            Context.getInstance().setTimeoutTimeUnit(TimeUnit.SECONDS);
                            System.out.println("Timeout set to: " + number + " seconds.\n");
                            break;
                        case "M": 
                            Context.getInstance().setTimeoutTimeUnit(TimeUnit.MINUTES);
                            System.out.println("Timeout set to: " + number + " minutes.\n");
                            break;
                        case "H": 
                            Context.getInstance().setTimeoutTimeUnit(TimeUnit.HOURS);
                            System.out.println("Timeout set to: " + number + " hours.\n");
                            break;
                    
                        default:
                            System.out.println("Invalid command. Use /t " + number + "<U> to set a timeout. U must be S, M or H.\n");
                            continue;
                    }
                }

                else if (line.equals(prefix + "i")) {
                    // clearConsole();
                    System.out.println("Graph Information:");
                    System.out.println("Total nodes: " + Graph.getGraph().getNodesQuantity());
                    System.out.println("Total edges: " + Graph.getGraph().getEdgesQuantity());
                    System.out.println("Total label: " + Graph.getGraph().getDifferetEdgesQuantity());
                    System.out.println("Edges per label: " + Graph.getGraph().getEdgesByLabelQuantity().toString());
                    System.out.println("\n");
                    continue;
                }
                
                else if (line.equals(prefix + "l")) {
                    System.out.println("Samples: ");
                    ArrayList<Edge> edges = Graph.getGraph().getSampleOfEachlabel();
                    for (Edge e : edges) {
                    System.out.println(e.getId() + ": " + e.getSource().getId() + "," + e.getLabel() + "," + e.getTarget().getId());
                    }
                    System.out.println("\n");
                    continue;
                }

                else if (line.startsWith(prefix + "o ")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        // clearConsole();
                        System.out.println("Invalid command. Use /o true or /o false to set optimization.\n");
                        continue;
                    }

                    if (parts[1].equalsIgnoreCase("true")) {
                        Context.getInstance().setOptimize(true);
                        System.out.println("Optimization set to: true.\n");
                    }
                    else if (parts[1].equalsIgnoreCase("false")) {
                        Context.getInstance().setOptimize(false);
                        System.out.println("Optimization set to: false.\n");
                    }
                    else {
                        // clearConsole();
                        System.out.println("Invalid command. Use /o true or /o false to set optimization.\n");
                    }
                    continue;
                }

                else {
                    System.out.println("Invalid command. For help, type /h.\n\n");
                }
            }

        }
        catch (Exception e) {}
    }

    public static void experimental(String[] args) {
        Context.getInstance().setExperimental(true);
        
        String nodes_file = args[0];
        String edges_file = args[1];
        String rpqs_file = args[2];

        Integer lineNumber;         // 1st column
        Integer method;             // 2nd column
        Integer fixpoint;           // 3rd column
        Integer semantic;           // 4th column
        String timeOut;             // 5th column
        boolean isOptimized;        // 6th column
        String sn;                  // 7th column
        String rpq;                 // 8th column
        String en;                  // 9th column

        Tools.loadCustomGraphFiles(nodes_file, edges_file);

        ArrayList<String> rpqs = Tools.readRPQsFromFile(rpqs_file);

        Context.getInstance().setRPQFileName(rpqs_file);

        for (String line : rpqs) {

            // #region Check if line is valid
            if (!line.endsWith(";")) {
                System.out.println("Error in line: '" + line + "'. Missing semicolon. Must end with ';'. Skipping line.\n");
                continue;
            }

            String[] data = line.split(",");
            
            if (data.length != 9) {
                System.out.println("Error in line: '" + line + "'. Invalid number of arguments (9 expected). Skipping line.\n");
            }
            // #endregion

            //#region 1st column - Line Number - Mandatory
            try {
                lineNumber = Integer.parseInt(data[0]);
                if (lineNumber < 0) {
                    System.out.println("Error in line: '" + line + "'. Invalid line number. Must be a positive number. Skipping line.\n");
                    continue;
                }
                Context.getInstance().setNumber(lineNumber);
            } catch (Exception e) {
                System.out.println("Error in line: '" + line + "'. Invalid line number. Must be a positive number. Skipping line.\n");
                continue;
            }
            //#endregion

            // #region 2nd column - Method - Optional (Default is 1 - Algebra)
            try {
                if (data[1].isEmpty()) {
                    method = 1;
                }
                else{
                    method = Integer.parseInt(data[1]);
                    if (method < 1 || method > 5) {
                        System.out.println("Error in line: '" + line + "'. Invalid method. Skipping line.\n");
                        continue;
                    }
                }
                Context.getInstance().setMethod(method);
            } catch (Exception e) {
                System.out.println("Error in line: '" + line + "'. Invalid method. Must be a number between 1 and 5. Skipping line.\n");
                continue;
            }
            // #endregion

            // #region 3rd column - Fixpoint - Optional (Default is 5)
            try {
                if (data[2].isEmpty()) {
                    fixpoint = 5;
                }
                else {
                    fixpoint = Integer.parseInt(data[2]);
                    if (fixpoint <= 0) {
                        System.out.println("Error in line: '" + line + "'. Invalid fixpoint. Must be a positive number. Skipping line.\n");
                        continue;
                    }
                }
                Context.getInstance().setFixPoint(fixpoint);
            } catch (Exception e) {
                System.out.println("Error in line: '" + line + "'. Invalid fixpoint. Must be a number. Skipping line.\n");
                continue;
            }
            // #endregion

            // #region 4th column - Semantic - Optional (Default is 2 - Trail)
            try {
                System.out.println("Data:" + data[3] + "///");
                if (data[3].equals("")) {
                    semantic = 2;
                    System.out.println("aaaa");
                }
                else {
                    semantic = Integer.parseInt(data[3]);
                    if (semantic < 1 || semantic > 3) {
                        System.out.println("Error in line: '" + line + "'. Invalid semantic. Must be a number between 1 and 3. Skipping line.\n");
                        continue;
                    }
                }
                Context.getInstance().setSemantic(semantic);
            } catch (Exception e) {
                System.out.println("Error in line: '" + line + "'. Invalid semantic. Must be a number. Skipping line.\n");
                continue;
            }
            //#endregion

            // #region 5th column - Timeout - Optional (Default is 2 Minutes)
            try {
                timeOut = data[4];

                if (timeOut.isEmpty()) {
                    Context.getInstance().setTimeoutDuration(2);
                    Context.getInstance().setTimeoutTimeUnit(TimeUnit.MINUTES);
                }

                else if (timeOut.equals("0")) {
                    Context.getInstance().setTimeoutDuration(Integer.MAX_VALUE);
                    Context.getInstance().setTimeoutTimeUnit(TimeUnit.DAYS);
                }

                else {
                    String number = timeOut.substring(0, timeOut.length() - 1);
                    String unit = timeOut.substring(timeOut.length() - 1);

                    try {
                        Integer numberInt = Integer.parseInt(number);
                        if (numberInt < 1) {
                            System.out.println("Error in line: '" + line + "'. Invalid timeout. Must be a positive number. Skipping line.\n");
                            continue;
                        }
                        Context.getInstance().setTimeoutDuration(numberInt);
                    } catch (Exception e) {
                        System.out.println("Error in line: '" + line + "'. Invalid timeout. Must be a number. Skipping line.\n");
                        continue;
                    }

                    if (!unit.equals("S") && !unit.equals("M") && !unit.equals("H")) {
                        System.out.println("Error in line: '" + line + "'. Invalid timeout unit. Must be S (seconds), M (minutes) or H (hours). Skipping line.\n");
                        continue;
                    }

                    switch (unit) {
                        case "S":
                            Context.getInstance().setTimeoutTimeUnit(TimeUnit.SECONDS);
                            break;
                        case "M":
                            Context.getInstance().setTimeoutTimeUnit(TimeUnit.MINUTES);
                            break;
                        case "H":
                            Context.getInstance().setTimeoutTimeUnit(TimeUnit.HOURS);
                            break;
                        default:
                            System.out.println("Error in line: '" + line + "'. Invalid timeout unit. Must be S (seconds), M (minutes) or H (hours). Skipping line.\n");
                            continue;
                    }
                }

            } catch (Exception e) {}
            //#endregion

            // #region 6th column - Optimized - Optional (Default is false)
            try {
                if (data[5].isEmpty()) {
                    isOptimized = false;
                }
                else {
                    isOptimized = Boolean.parseBoolean(data[5]);
                }
                Context.getInstance().setOptimize(isOptimized);
            } catch (Exception e) {
                System.out.println("Error in line: '" + line + "'. Invalid optimized option. Must be true or false. Skipping line.\n");
                continue;
            }
            // #endregion

            // #region 7th column - Start Node - Optional (Default is empty)
            try {
                sn = data[6];
                if (!sn.isEmpty()) Context.getInstance().setStartNode(sn);
                else Context.getInstance().setStartNode("");
            } catch (Exception e) {}
            // #endregion

            // #region 8th column - RPQ - Mandatory
            try {
                rpq = data[7];
                if (!rpq.isEmpty()) {
                    Context.getInstance().setRPQ(rpq);
                }
                else {
                    System.out.println("Error in line: '" + line + "'. Invalid RPQ. RPQ is mandatory. Skipping line.\n");
                    continue;
                }
            } catch (Exception e) {
                System.out.println("Error in line: '" + line + "'. Invalid RPQ. Must be a string. Skipping line.\n");
                continue;
            }
            // #endregion

            // #region 9th column - End Node - Optional (Default is empty)
            try {
                en = data[8].replaceAll(";", "");
                if (!en.isEmpty()) Context.getInstance().setEndNode(en);
                else Context.getInstance().setEndNode("");
            } catch (Exception e) {
                System.out.println("Error in line: '" + line + "'. Invalid end node. Must be a string. Skipping line.\n");
                continue;
            }
            // #endregion

            // #region Execution of the RPQ
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<Void> future = executor.submit(new runExecution());

            try {
                future.get(Context.getInstance().getTimeoutDuration(), Context.getInstance().getTimeoutTimeUnit());
            }
            catch (Exception e) {
                System.out.println("Execution timed out.\n");
                future.cancel(true);
            }

            executor.shutdownNow();
            // #endregion
        }
    }

}

class runExecution implements Callable<Void> {

    @Override
    public Void call() throws Exception {
        
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
        }

        return null;
    }
}