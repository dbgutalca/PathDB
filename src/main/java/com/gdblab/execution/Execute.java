package com.gdblab.execution;

import java.io.IOException;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import com.gdblab.algebra.parser.RPQErrorListener;
import com.gdblab.algebra.parser.RPQExpression;
import com.gdblab.algebra.parser.RPQGrammarListener;
import com.gdblab.algebra.parser.error.SyntaxErrorException;
import com.gdblab.algebra.parser.error.VariableNotFoundException;
import com.gdblab.algebra.parser.impl.RPQtoAlgebraVisitor;
import com.gdblab.algebra.queryplan.logical.LogicalOperator;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpSelection;
import com.gdblab.algebra.queryplan.logical.visitor.LogicalToBFPhysicalVisitor;
import com.gdblab.algebra.queryplan.logical.visitor.PredicatePushdownLogicalPlanVisitor;
import com.gdblab.algebra.queryplan.physical.PhysicalOperator;
import com.gdblab.algebra.queryplan.util.Utils;
import com.gdlab.parser.RPQGrammarLexer;
import com.gdlab.parser.RPQGrammarParser;

public final class Execute {

    private static final String prefix = "/";

    public static void EvalRPQWithAlgebra() {
        long start = System.nanoTime();
        int counter = 1;

        byte[] emergencyMemory = new byte[1024 * 1024];

        PhysicalOperator po;

        try {
            RPQGrammarLexer lexer = new RPQGrammarLexer(
                    CharStreams.fromString(Context.getInstance().getCompleteQuery()));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            RPQGrammarParser parser = new RPQGrammarParser(tokens);

            parser.removeErrorListeners();
            parser.addErrorListener(new RPQErrorListener());

            ParseTreeWalker walker = new ParseTreeWalker();
            RPQGrammarListener listener = new RPQGrammarListener();
            walker.walk(listener, parser.query());

            RPQExpression rpqExp = Context.getInstance().getRegularExpression();
            RPQtoAlgebraVisitor visitor = new RPQtoAlgebraVisitor();
            rpqExp.acceptVisit(visitor);

            LogicalOperator lo = visitor.getRoot();

            if (Context.getInstance().getCondition() != null) {
                lo = addFilter(lo);
            }

            /*
             * Predicate Pushdown
             * This is a special version to force to pushdown the filter.
             * Only usable for testing purposes.
             */
            PredicatePushdownLogicalPlanVisitor v = new PredicatePushdownLogicalPlanVisitor();
            lo.acceptVisitor(v);
            lo = v.getRoot();

            LogicalToBFPhysicalVisitor visitor2 = new LogicalToBFPhysicalVisitor();
            lo.acceptVisitor(visitor2);
            po = visitor2.getPhysicalPlan().getRootOperator();

            counter = Utils.printAndCountPaths(po);

            long end = System.nanoTime();

            System.out.println("\nTotal paths: " + counter + " paths");
            System.out.println("Execution time: " + Utils.getTime(start, end) + " seconds");
            System.out.println("");
            Tools.resetContext();
            
            po = null;

        } catch (RecognitionException see) {
            Tools.resetContext();
        } catch (OutOfMemoryError e) {
            emergencyMemory = null;
            System.gc();
            po = null;
            Tools.resetContext();
        }
    }

    public static void interactive(String[] args) {

        try {
            Terminal terminal = TerminalBuilder.terminal();
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

            Tools.clearConsole();

            if (args.length == 0) {
                Tools.showUsageNoArgs();
                Tools.loadDefaultGraph();
            } else {
                Tools.showUsageArgsLoadingCustomGraph(args[0], args[1]);
                Tools.loadCustomGraphFiles(args[0], args[1]);
            }

            String prompt = "PathDB> ";

            //#region Server
            // ServerSocket ss = new ServerSocket(12000);
            // System.out.println("Server started on port 12000. Waiting for client connections...");

            // while (true) {
            //     try (Socket clientSocket = ss.accept();
            //         BufferedReader in = new BufferedReader(new
            //         InputStreamReader(clientSocket.getInputStream()));
            //         PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            //         String input = in.readLine();
            //         System.out.println("Received: " + input);
            //         Context.getInstance().setCompleteQuery(input);
            //         String res = EvalRPQWithAlgebra();
            //         out.println(res);
            //     }
            // }
            //#endregion

            while (true) {

                String line = reader.readLine(prompt);

                if (line == null || line.equalsIgnoreCase(prefix + "q") || line.equalsIgnoreCase(prefix + "quit")) {
                    System.out.println("Terminated execution.");
                    break;
                }

                reader.getHistory().add(line);

                if (line.equals(prefix + "h") || line.equals(prefix + "help")) {
                    Tools.showHelp();
                    System.out.println();
                }

                else if (line.startsWith(prefix + "ml ") || line.startsWith(prefix + "max_length ")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        // clearConsole();
                        System.out.println("Invalid command. Use /ml <#> or /max_length <#> to set max the length of generated paths.\n");
                        continue;
                    }

                    try {
                        Integer.valueOf(parts[1]);
                    } catch (NumberFormatException e) {
                        // clearConsole();
                        System.out.println("Invalid command. # must be a positive number greater than 0.\n");
                        continue;
                    }

                    Context.getInstance().setMaxPathsLength(Integer.valueOf(parts[1]));
                    System.out.println("Set max length of paths to: " + parts[1] + "\n");
                }

                else if (line.startsWith(prefix + "mr ") || line.startsWith(prefix + "max_recursion ")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        // clearConsole();
                        System.out.println(
                                "Invalid command. Use /mr <#> or /max_recursion <#> to set max recursion of each recursive operation.\n");
                        continue;
                    }

                    try {
                        Integer.valueOf(parts[1]);
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

                    Context.getInstance().setMaxRecursion(Integer.valueOf(parts[1]));
                    System.out.println("Set max recursion to: " + parts[1] + "\n");
                }

                else if (line.startsWith(prefix + "pts ") || line.startsWith(prefix + "paths_to_show ")) {
                    String[] parts = line.split(" ");

                    if (parts.length != 2) {
                        System.out.println(
                                "Invalid command. Use /pts <#> or /paths_to_show <#> to set the number of paths to show.\n");
                        continue;
                    }

                    try {
                        Integer.valueOf(parts[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid command. Use /p <#> or /paths_to_show <#> to set the number of paths to show.\n");
                        continue;
                    }

                    if (Integer.parseInt(parts[1]) < 0) {
                        System.out.println("Invalid command. <#> must be a positive number greater than 0.\n");
                        continue;
                    }

                    Context.getInstance().setTotalPathsToShow(Integer.valueOf(parts[1]));
                    System.out.println("Set show paths configuration to: " + parts[1] + "\n");
                }

                else if (line.endsWith(";")) {
                    try {
                        Context.getInstance().setCompleteQuery(line);
                        EvalRPQWithAlgebra();
                    } catch (OutOfMemoryError e) {
                        System.out.println("Out of memory error. Try again with more memory.\n");
                    } catch (SyntaxErrorException see) {
                        System.out.println(see.toString());
                    } catch (VariableNotFoundException e) {
                        System.out.println(e.toString());
                    } catch (Exception e) {
                        System.out.println("An error occurred inside.");
                    }
                }

                // else if (line.equals(prefix + "in") || line.equals(prefix + "information")) {
                // // clearConsole();
                // System.out.println("Graph Information:");
                // System.out.println("Total nodes: " + Graph.getGraph().getNodesQuantity());
                // System.out.println("Total edges: " + Graph.getGraph().getEdgesQuantity());
                // System.out.println("Total label: " +
                // Graph.getGraph().getDifferetEdgesQuantity());
                // System.out.println("Edges per label: " +
                // Graph.getGraph().getEdgesByLabelQuantity().toString());
                // System.out.println("\n");
                // continue;
                // }

                // else if (line.equals(prefix + "la") || line.equals(prefix + "labels")) {
                // System.out.println("Samples: ");
                // ArrayList<Edge> edges = Graph.getGraph().getSampleOfEachlabel();
                // for (Edge e : edges) {
                // System.out.println(e.getId() + ": " + e.getSource().getId() + "," +
                // e.getLabel() + "," + e.getTarget().getId());
                // }
                // System.out.println("\n");
                // continue;
                // }

                else {
                    System.out.println("Invalid command. For help, type /h.\n\n");
                }

            }

        } catch (IOException | NumberFormatException | EndOfFileException e) {
            System.out.println("An error occurred.");
        } catch (UserInterruptException e) {
            System.out.println("User interrupted the process.");
        }
    }

    public static LogicalOperator addFilter(LogicalOperator lo) {
        return new LogicalOpSelection(lo, Context.getInstance().getCondition());
    }
}