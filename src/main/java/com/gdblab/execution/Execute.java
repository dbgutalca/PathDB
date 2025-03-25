package com.gdblab.execution;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        if ( !Context.getInstance().getStartNodeProp().equalsIgnoreCase("") ) {
            lo = filterOnTopLeft(lo);

            if (Context.getInstance().isOptimize()) {
                PredicatePushdownLogicalPlanVisitor v = new PredicatePushdownLogicalPlanVisitor();
                lo.acceptVisitor(v);
                lo = v.getRoot();
            }
        }

        if ( !Context.getInstance().getEndNodeProp().equalsIgnoreCase("") ) {
            lo = filterOnTopRight(lo);

            if (Context.getInstance().isOptimize() && Context.getInstance().getStartNodeProp().equalsIgnoreCase("")) {
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
            String time = Utils.getTime(start, end);
            System.out.println("RPQ:" + Context.getInstance().getRPQ() + "##" + "Time:" + time + "##" + "Total:" + Context.getInstance().getTotalPaths());
            // Utils.writeTotalAndTime();
            // Utils.writeOnSummary();
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
            // String filename = "results_" + Context.getInstance().getRPQFileName() + "_" + Context.getInstance().getNumber() + ".txt";
            // Context.getInstance().setResultFilename(filename);

            // Writer writer = null;
            
            try {
                // writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Context.getInstance().getResultFilename()), "utf-8"));
                // Utils.writeConfig(writer);

                DFSRegex dfsRegex = new DFSRegex(er);
                dfsRegex.Trail();
                // Context.getInstance().setTotalPaths(dfsRegex.getTotalPaths());

                // writer.close();

                long end = System.nanoTime();
                String time = Utils.getTime(start, end);
                System.out.println("RPQ:" + Context.getInstance().getRPQ() + "##" + "Time:" + time);
                // Context.getInstance().setTime(Utils.getTime(start, end));
                // Utils.writeTotalAndTime();
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
            // String filename = "results_" + Context.getInstance().getRPQFileName() + "_" + Context.getInstance().getNumber() + ".txt";
            // Context.getInstance().setResultFilename(filename);

            // Writer writer = null;

            try {
                // writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Context.getInstance().getResultFilename()), "utf-8"));
                // Utils.writeConfig(writer);

                BFSRegex bfsRegex = new BFSRegex(er);
                bfsRegex.Trail();
                // Context.getInstance().setTotalPaths(bfsRegex.getTotalPaths());

                // writer.close();

                long end = System.nanoTime();
                String time = Utils.getTime(start, end);
                System.out.println("RPQ:" + Context.getInstance().getRPQ() + "##" + "Time:" + time);
                // Context.getInstance().setTime(Utils.getTime(start, end));
                // Utils.writeTotalAndTime();
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
        // String filename = "results_" + Context.getInstance().getRPQFileName() + "_" + Context.getInstance().getNumber() + ".txt";
        // Context.getInstance().setResultFilename(filename);
        
        long start = System.nanoTime();
        String er = RPQtoER.Translate(Context.getInstance().getRPQ());

        DFSAutomaton dfsAutomaton = new DFSAutomaton(er);
        dfsAutomaton.Trail();

        long end = System.nanoTime();
        String time = Utils.getTime(start, end);
        System.out.println("RPQ:" + Context.getInstance().getRPQ() + "##" + "Time:" + time);



        // try (FileWriter fw = new FileWriter("summary.txt", true)){
        //     System.out.println("hola");
        //     BufferedWriter bw = new BufferedWriter(fw);
        //     bw.write("RPQ: " + Context.getInstance().getRPQ() + "\n");
        //     bw.write("Total: " + dfsAutomaton.getTotalPaths() + "\n");
        //     bw.write("Time: " + total + " s\n");
        //     bw.write("=====================================");
        //     bw.close();
        //     fw.close();
            
        // } catch (Exception e) {
            
        // }
    }

    public static void EvalRPQWithAutomatonBFS() {
        String filename = "results_" + Context.getInstance().getRPQFileName() + "_" + Context.getInstance().getNumber() + ".txt";
        Context.getInstance().setResultFilename(filename);

        long start = System.nanoTime();
        String er = RPQtoER.Translate(Context.getInstance().getRPQ());

        BFSAutomaton bfsAutomaton = new BFSAutomaton(er);
        bfsAutomaton.Trail();

        
        long end = System.nanoTime();
        String time = Utils.getTime(start, end);
        System.out.println("RPQ:" + Context.getInstance().getRPQ() + "##" + "Time:" + time);

        // try (FileWriter fw = new FileWriter("summary.txt", true)){
        //     BufferedWriter bw = new BufferedWriter(fw);
        //     bw.write("RPQ: " + Context.getInstance().getRPQ() + "\n");
        //     bw.write("Total: " + bfsAutomaton.getTotalPaths() + "\n");
        //     bw.write("Time: " + total + " s\n");
        //     bw.write("=====================================");
        //     bw.close();
        //     fw.close();
            
        // } catch (Exception e) {
            
        // }

    }

    private static LogicalOperator filterOnTopLeft(LogicalOperator lo) {
        return new LogicalOpSelection(lo, new First(Context.getInstance().getStartNodeProp(), Context.getInstance().getStartNodeValue()));
    }

    private static LogicalOperator filterOnTopRight(LogicalOperator lo) {
        return new LogicalOpSelection(lo, new Last(Context.getInstance().getEndNodeProp(), Context.getInstance().getEndNodeValue()));
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
                Tools.showUsageArgsLoadingCustomGraph(args[0], args[1]);
                Tools.loadCustomGraphFiles(args[0], args[1]);
            }

            String prompt = "PathDB> ";

            while (true) {

                String line = reader.readLine(prompt);

                if (line == null || line.equalsIgnoreCase(prefix + "q") || line.equalsIgnoreCase(prefix + "quit")) {
                    System.out.println("Terminated execution.");
                    break;
                }

                reader.getHistory().add(line);

                if (line.equals(prefix + "h") || line.equals(prefix + "help")) {
                    // clearConsole();
                    Tools.showHelp();
                    System.out.println();
                    continue;
                }

                else if (line.startsWith(prefix + "me ") || line.startsWith(prefix + "method ")) {
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

                else if (line.startsWith(prefix + "ml ") || line.startsWith(prefix + "max_length ")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        // clearConsole();
                        System.out.println("Invalid command. Use /ml <#> or /max_length <#> to set max the length of generated paths.\n");
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
                    System.out.println("Set max length of paths to: " + parts[1] + "\n");
                    continue;
                }

                else if(line.startsWith(prefix + "mr ") || line.startsWith(prefix + "max_recursion ")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        // clearConsole();
                        System.out.println("Invalid command. Use /mr <#> or /max_recursion <#> to set max recursion of each recursive operation.\n");
                        continue;
                    }

                    try {
                        Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        // clearConsole();
                        System.out.println("Invalid command. # must be a positive number greater or equals to 0.\n");
                        continue;
                    }

                    if (Integer.parseInt(parts[1]) < 0) {
                        // clearConsole();
                        System.out.println("Invalid command. # must be a positive number greater or equals to 0.\n");
                        continue;
                    }

                    Context.getInstance().setMaxRecursion(Integer.parseInt(parts[1]));
                    System.out.println("Set max recursion to: " + parts[1] + "\n");
                    continue;
                }

                else if (line.startsWith(prefix + "sem ") || line.startsWith(prefix + "semantic ")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        System.out.println("Invalid command. Use /sem <1-3> or /semantic <1-3> to set a semantic.\n");
                        continue;
                    }

                    try {
                        Integer.parseInt(parts[1]);
                    } catch (Exception e) {
                        System.out.println("Invalid command. Use /sem <1-3> or /semantic <1-3> to set a semantic.\n");
                        continue;
                    }

                    if (Integer.parseInt(parts[1]) < 1 || Integer.parseInt(parts[1]) > 3) {
                        System.out.println("Invalid command. Use /sem <1-3> or /semantic <1-3> to set a semantic.\n");
                        continue;
                    }

                    Context.getInstance().setSemantic(Integer.parseInt(parts[1]));
                    Tools.showSelectedSemantic(Integer.parseInt(parts[1]));
                    continue;
                }

                else if (line.startsWith(prefix + "pts ") || line.startsWith(prefix + "paths_to_show ")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        System.out.println("Invalid command. Use /pts <#> or /paths_to_show <#> to set the number of paths to show.\n");
                        continue;
                    }

                    try {
                        Integer.parseInt(parts[1]);
                    } catch (Exception e) {
                        System.out.println("Invalid command. Use /p <#> or /paths_to_show <#> to set the number of paths to show.\n");
                        continue;
                    }

                    if (Integer.parseInt(parts[1]) < 0) {
                        System.out.println("Invalid command. <#> must be a positive number greater than 0.\n");
                        continue;
                    }

                    Context.getInstance().setShowPaths(Integer.parseInt(parts[1]));
                    System.out.println("Set show paths configuration to: " + parts[1] + "\n");
                    continue;
                }

                else if (line.startsWith(prefix + "lim ") || line.startsWith(prefix + "limit ")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        System.out.println("Invalid command. Use /lim <#> or /limit <#> to set the number of paths to calculate.\n");
                        continue;
                    }

                    if (parts[1].equalsIgnoreCase("all")) {
                        Context.getInstance().setMaxPaths(Integer.MAX_VALUE);
                        System.out.println("Set to calculate all possible paths.\n");
                        continue;
                    }

                    try {
                        Integer.parseInt(parts[1]);
                    } catch (Exception e) {
                        System.out.println("Invalid command. Use /lim <#> or /limit <#> to set the number of paths to calculate.\n");
                        continue;
                    }

                    if (Integer.parseInt(parts[1]) < 0) {
                        System.out.println("Invalid command. <#> must be a positive number greater than 0.\n");
                        continue;
                    }

                    Context.getInstance().setMaxPaths(Integer.parseInt(parts[1]));
                    System.out.println("Set show paths configuration to: " + parts[1] + "\n");
                    continue;
                }

                // else if (line.startsWith(prefix + "to ") || line.startsWith(prefix + "timeout ")) {
                //     String[] parts = line.split(" ");
                //     if (parts.length != 2) {
                //         System.out.println("Invalid command. Use /t <#><U> to set a timeout.\n");
                //         continue;
                //     }
                //     String number = parts[1].substring(0, parts[1].length() - 1);
                //     String unit = parts[1].substring(parts[1].length() - 1).toUpperCase();
                //     try { Integer.parseInt(number); }
                //     catch (Exception e) {
                //         System.out.println("Invalid command. Use /t <#><U> to set a timeout. # must be a number.\n");
                //         continue;
                //     }
                //     if ( Integer.parseInt(number) < 1)  {
                //         System.out.println("Invalid command. Use /t <#><U> to set a timeout. # must be greater than 0.\n");
                //         continue;
                //     }
                //     switch (unit) {
                //         case "S": 
                //             Context.getInstance().setTimeoutTimeUnit(TimeUnit.SECONDS);
                //             System.out.println("Timeout set to: " + number + " seconds.\n");
                //             break;
                //         case "M": 
                //             Context.getInstance().setTimeoutTimeUnit(TimeUnit.MINUTES);
                //             System.out.println("Timeout set to: " + number + " minutes.\n");
                //             break;
                //         case "H": 
                //             Context.getInstance().setTimeoutTimeUnit(TimeUnit.HOURS);
                //             System.out.println("Timeout set to: " + number + " hours.\n");
                //             break;
                //         default:
                //             System.out.println("Invalid command. Use /t " + number + "<U> to set a timeout. U must be S, M or H.\n");
                //             continue;
                //     }
                // }

                else if (line.startsWith(prefix + "opt ") || line.startsWith(prefix + "optimize ")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        // clearConsole();
                        System.out.println("Invalid command. Use /opt <true/false> or /optimize <true/false> to set optimization.\n");
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
                        System.out.println("Invalid command. Use /opt <true/false> or /optimization <true/false> to set optimization.\n");
                    }
                    continue;
                }

                else if (line.endsWith(";")) {

                    line = line.trim();
                    String[] parts = line.split("-");

                    if (parts.length != 3) {
                        System.out.println("Invalid command. Use '(X{prop:value})-[RPQ]->(Y);'' to execute a RPQ.\n");
                        continue;
                    }

                    String regex = "\\{([^:]+):([^}]+)\\}";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(parts[0]);

                    if (matcher.find()) {
                        Context.getInstance().setStartNodeProp(matcher.group(1));
                        Context.getInstance().setStartNodeValue(matcher.group(2));
                    }
                    else {
                        Context.getInstance().setStartNodeProp("");
                        Context.getInstance().setStartNodeValue("");
                    }

                    matcher = pattern.matcher(parts[2]);
                    
                    if (matcher.find()) {
                        Context.getInstance().setEndNodeProp(matcher.group(1));
                        Context.getInstance().setEndNodeValue(matcher.group(2));
                    }
                    else {
                        Context.getInstance().setEndNodeProp("");
                        Context.getInstance().setEndNodeValue("");
                    }

                    Context.getInstance().setRPQ(parts[1].substring(1, parts[1].length() - 1));

                    try {
                        switch (Context.getInstance().getMethod  ()) {
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
                    } catch (Exception e) {
                        System.out.println("An unexpected error occurred. Check syntax and try again.\n");
                    }

                }

                else if (line.equals(prefix + "i") || line.equals(prefix + "information")) {
                    // clearConsole();
                    System.out.println("Graph Information:");
                    System.out.println("Total nodes: " + Graph.getGraph().getNodesQuantity());
                    System.out.println("Total edges: " + Graph.getGraph().getEdgesQuantity());
                    System.out.println("Total label: " + Graph.getGraph().getDifferetEdgesQuantity());
                    System.out.println("Edges per label: " + Graph.getGraph().getEdgesByLabelQuantity().toString());
                    System.out.println("\n");
                    continue;
                }
                
                else if (line.equals(prefix + "l") || line.equals(prefix + "labels")) {
                    System.out.println("Samples: ");
                    ArrayList<Edge> edges = Graph.getGraph().getSampleOfEachlabel();
                    for (Edge e : edges) {
                    System.out.println(e.getId() + ": " + e.getSource().getId() + "," + e.getLabel() + "," + e.getTarget().getId());
                    }
                    System.out.println("\n");
                    continue;
                }

                else {
                    System.out.println("Invalid command. For help, type /h.\n\n");
                }
            }

        }
        catch (OutOfMemoryError e) {
            System.out.println("Out of memory error. Try again with more memory.\n");
        }
        catch (Exception e) {
            System.out.println("An unexpected error occurred.\n");
        }
    }

    public static void experimental(String[] args) {
        // Context.getInstance().setExperimental(true);
        // String nodes_file = args[0];
        // String edges_file = args[1];
        // String rpqs_file = args[2];
        // Integer lineNumber;         // 1st column
        // Integer method;             // 2nd column
        // Integer fixpoint;           // 3rd column MAX LENGTH
        // Integer maxRecursion;       // 4th column
        // Integer semantic;           // 5th column
        // Integer pathsToShow;        // 6th column
        // Integer limit;              // 7th column
        // String timeOut;             // 8th column
        // boolean isOptimized;        // 9th column
        // String sn;                  // 10th column
        // String rpq;                 // 11th column
        // String en;                  // 12th column
        // Tools.loadCustomGraphFiles(nodes_file, edges_file);
        // ArrayList<String> rpqs = Tools.readRPQsFromFile(rpqs_file);
        // Context.getInstance().setRPQFileName(rpqs_file);
        // for (String line : rpqs) {
        //     // #region Check if line is valid
        //     if (!line.endsWith(";")) {
        //         System.out.println("Error in line: '" + line + "'. Missing semicolon. Must end with ';'. Skipping line.\n");
        //         continue;
        //     }
        //     String[] data = line.split(",");
        //     if (data.length != 12) {
        //         System.out.println("Error in line: '" + line + "'. Invalid number of arguments (10 expected). Skipping line.\n");
        //     }
        //     // #endregion
        //     // #region 1st column - Line Number - Mandatory
        //     try {
        //         lineNumber = Integer.parseInt(data[0]);
        //         if (lineNumber < 0) {
        //             System.out.println("Error in line: '" + line + "'. Invalid line number. Must be a positive number. Skipping line.\n");
        //             continue;
        //         }
        //         Context.getInstance().setNumber(lineNumber);
        //     } catch (Exception e) {
        //         System.out.println("Error in line: '" + line + "'. Invalid line number. Must be a positive number. Skipping line.\n");
        //         continue;
        //     }
        //     //#endregion
        //     // #region 2nd column - Method - Optional (Default is 1 - Algebra)
        //     try {
        //         if (data[1].isEmpty()) {
        //             method = 1;
        //         }
        //         else{
        //             method = Integer.parseInt(data[1]);
        //             if (method < 1 || method > 5) {
        //                 System.out.println("Error in line: '" + line + "'. Invalid method. Skipping line.\n");
        //                 continue;
        //             }
        //         }
        //         Context.getInstance().setMethod(method);
        //     } catch (Exception e) {
        //         System.out.println("Error in line: '" + line + "'. Invalid method. Must be a number between 1 and 5. Skipping line.\n");
        //         continue;
        //     }
        //     // #endregion
        //     // #region 3rd column - Fixpoint - Optional (Default is 10)
        //     try {
        //         if (data[2].isEmpty()) {
        //             fixpoint = 10;
        //         }
        //         else {
        //             fixpoint = Integer.parseInt(data[2]);
        //             if (fixpoint <= 0) {
        //                 System.out.println("Error in line: '" + line + "'. Invalid fixpoint. Must be a positive number. Skipping line.\n");
        //                 continue;
        //             }
        //         }
        //         Context.getInstance().setFixPoint(fixpoint);
        //     } catch (Exception e) {
        //         System.out.println("Error in line: '" + line + "'. Invalid fixpoint. Must be a number. Skipping line.\n");
        //         continue;
        //     }
        //     // #endregion
        //     // #region 4th column - Max Recursion - Optional (Default is 4)
        //     try {
        //         if (data[3].isEmpty()) {
        //             maxRecursion = 4;
        //         }
        //         else {
        //             maxRecursion = Integer.parseInt(data[3]);
        //             if (maxRecursion < 1) {
        //                 System.out.println("Error in line: '" + line + "'. Invalid max recursion. Must be a positive number. Skipping line.\n");
        //                 continue;
        //             }
        //         }
        //         Context.getInstance().setMaxRecursion(maxRecursion);
        //     } catch (Exception e) {
        //         System.out.println("Error in line: '" + line + "'. Invalid max recursion. Must be a number. Skipping line.\n");
        //         continue;
        //     }
        //     // #endregion
        //     // #region 5th column - Semantic - Optional (Default is 2 - Trail)
        //     try {
        //         if (data[4].equals("")) {
        //             semantic = 2;
        //         }
        //         else {
        //             semantic = Integer.parseInt(data[4]);
        //             if (semantic < 1 || semantic > 3) {
        //                 System.out.println("Error in line: '" + line + "'. Invalid semantic. Must be a number between 1 and 3. Skipping line.\n");
        //                 continue;
        //             }
        //         }
        //         Context.getInstance().setSemantic(semantic);
        //     } catch (Exception e) {
        //         System.out.println("Error in line: '" + line + "'. Invalid semantic. Must be a number. Skipping line.\n");
        //         continue;
        //     }
        //     //#endregion
        //     // #region 6th column - Paths to Show - Optional (Default is 10)
        //     try {
        //         if (data[5].isEmpty()) {
        //             pathsToShow = 10;
        //         }
        //         else {
        //             pathsToShow = Integer.parseInt(data[5]);
        //             if (pathsToShow < 1) {
        //                 System.out.println("Error in line: '" + line + "'. Invalid paths to show. Must be a positive number. Skipping line.\n");
        //                 continue;
        //             }
        //         }
        //         Context.getInstance().setShowPaths(pathsToShow);
        //     } catch (Exception e) {
        //         System.out.println("Error in line: '" + line + "'. Invalid paths to show. Must be a number. Skipping line.\n");
        //         continue;
        //     }
        //     // #endregion
        //     // #region 7th column - Limit - Optional (Default is all)
        //     try {
        //         if (data[6].isEmpty()) {
        //             limit = Integer.MAX_VALUE;
        //         }
        //         else {
        //             if (data[6].equals("all")) {
        //                 limit = Integer.MAX_VALUE;
        //             }
        //             else {
        //                 limit = Integer.parseInt(data[6]);
        //                 if (limit < 1) {
        //                     System.out.println("Error in line: '" + line + "'. Invalid limit. Must be a positive number. Skipping line.\n");
        //                     continue;
        //                 }
        //             }
        //         }
        //         Context.getInstance().setMaxPaths(limit);
        //     } catch (Exception e) {
        //         System.out.println("Error in line: '" + line + "'. Invalid limit. Must be a number. Skipping line.\n");
        //         continue;
        //     }
        //     // #endregion
        //     // #region 8th column - Timeout - Optional (Default is 2 Minutes)
        //     try {
        //         timeOut = data[7];
        //         if (timeOut.isEmpty()) {
        //             Context.getInstance().setTimeoutDuration(2);
        //             Context.getInstance().setTimeoutTimeUnit(TimeUnit.MINUTES);
        //         }
        //         else if (timeOut.equals("0")) {
        //             Context.getInstance().setTimeoutDuration(Integer.MAX_VALUE);
        //             Context.getInstance().setTimeoutTimeUnit(TimeUnit.DAYS);
        //         }
        //         else {
        //             String number = timeOut.substring(0, timeOut.length() - 1);
        //             String unit = timeOut.substring(timeOut.length() - 1);
        //             try {
        //                 Integer numberInt = Integer.parseInt(number);
        //                 if (numberInt < 1) {
        //                     System.out.println("Error in line: '" + line + "'. Invalid timeout. Must be a positive number. Skipping line.\n");
        //                     continue;
        //                 }
        //                 Context.getInstance().setTimeoutDuration(numberInt);
        //             } catch (Exception e) {
        //                 System.out.println("Error in line: '" + line + "'. Invalid timeout. Must be a number. Skipping line.\n");
        //                 continue;
        //             }
        //             if (!unit.equals("S") && !unit.equals("M") && !unit.equals("H")) {
        //                 System.out.println("Error in line: '" + line + "'. Invalid timeout unit. Must be S (seconds), M (minutes) or H (hours). Skipping line.\n");
        //                 continue;
        //             }
        //             switch (unit) {
        //                 case "S":
        //                     Context.getInstance().setTimeoutTimeUnit(TimeUnit.SECONDS);
        //                     break;
        //                 case "M":
        //                     Context.getInstance().setTimeoutTimeUnit(TimeUnit.MINUTES);
        //                     break;
        //                 case "H":
        //                     Context.getInstance().setTimeoutTimeUnit(TimeUnit.HOURS);
        //                     break;
        //                 default:
        //                     System.out.println("Error in line: '" + line + "'. Invalid timeout unit. Must be S (seconds), M (minutes) or H (hours). Skipping line.\n");
        //                     continue;
        //             }
        //         }
        //     } catch (Exception e) {}
        //     //#endregion
        //     // #region 9th column - Optimized - Optional (Default is true)
        //     try {
        //         if (data[8].isEmpty()) {
        //             isOptimized = true;
        //         }
        //         else {
        //             isOptimized = Boolean.parseBoolean(data[8]);
        //         }
        //         Context.getInstance().setOptimize(isOptimized);
        //     } catch (Exception e) {
        //         System.out.println("Error in line: '" + line + "'. Invalid optimized option. Must be true or false. Skipping line.\n");
        //         continue;
        //     }
        //     // #endregion
        //     // #region 10th column - Start Node - Optional (Default is empty)
        //     try {
        //         sn = data[9];
        //         if (!sn.isEmpty()) Context.getInstance().setStartNode(sn);
        //         else Context.getInstance().setStartNode("");
        //     } catch (Exception e) {}
        //     // #endregion
        //     // #region 11th column - RPQ - Mandatory
        //     try {
        //         rpq = data[10];
        //         if (!rpq.isEmpty()) {
        //             Context.getInstance().setRPQ(rpq);
        //         }
        //         else {
        //             System.out.println("Error in line: '" + line + "'. Invalid RPQ. RPQ is mandatory. Skipping line.\n");
        //             continue;
        //         }
        //     } catch (Exception e) {
        //         System.out.println("Error in line: '" + line + "'. Invalid RPQ. Must be a string. Skipping line.\n");
        //         continue;
        //     }
        //     // #endregion
        //     // #region 12th column - End Node - Optional (Default is empty)
        //     try {
        //         en = data[11].replaceAll(";", "");
        //         if (!en.isEmpty()) Context.getInstance().setEndNode(en);
        //         else Context.getInstance().setEndNode("");
        //     } catch (Exception e) {
        //         System.out.println("Error in line: '" + line + "'. Invalid end node. Must be a string. Skipping line.\n");
        //         continue;
        //     }
        //     // #endregion
        //     // #region Execution of the RPQ without Threads
        //     Thread tt = new Thread( () -> {
        //         switch (Context.getInstance().getMethod()) {
        //             case 1:
        //                 Execute.EvalRPQWithAlgebra();
        //                 break;
        //             case 2:
        //                 Execute.EvalRPQWithRegexDFS();
        //                 break;
        //             case 3:
        //                 Execute.EvalRPQWithRegexBFS();
        //                 break;
        //             case 4:
        //                 Execute.EvalRPQWithAutomatonDFS();
        //                 break;
        //             case 5:
        //                 Execute.EvalRPQWithAutomatonBFS();
        //                 break;
        //         }
        //     });
        //     tt.start();
        //     try {
        //         tt.join(Context.getInstance().getTimeoutTimeUnit().toMillis(Context.getInstance().getTimeoutDuration()));
        //         if (tt.isAlive()) {
        //             System.out.println("RPQ:" + Context.getInstance().getRPQ() + "##" + "Time:-999,999");
        //             tt.stop();
        //         }
        //     } catch (Exception e) {}
        //     // #endregion
        // }
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