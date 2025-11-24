package com.gdblab.execution;

import java.io.IOException;
import java.util.ArrayList;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import com.gdblab.algebra.condition.Condition;
import com.gdblab.algebra.condition.First;
import com.gdblab.algebra.condition.Last;
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
import com.gdblab.graph.Graph;
import com.gdblab.graph.schema.Edge;
import com.gdlab.parser.RPQGrammarLexer;
import com.gdlab.parser.RPQGrammarParser;

public final class Execute {

    public static void EvalRPQWithAlgebra() {
        long start = System.nanoTime();
        int counter = 1;

        byte[] emergencyMemory = new byte[1024 * 1024];
        PhysicalOperator po = null;

        try {
            RPQGrammarLexer lexer = new RPQGrammarLexer(CharStreams.fromString(Context.getInstance().getCompleteQuery()));
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

            Condition condition = Context.getInstance().getCondition();
            lo = checkAndAddFilter(lo, condition);

            LogicalToBFPhysicalVisitor visitor2 = new LogicalToBFPhysicalVisitor();
            lo.acceptVisitor(visitor2);
            po = visitor2.getPhysicalPlan().getRootOperator();

            counter = Utils.printAndCountPaths(po);

            long end = System.nanoTime();
            System.out.println("\nTotal paths: " + (counter - 1) + " paths");
            System.out.println("Execution time: " + Utils.getTime(start, end) + " seconds");
            System.out.println("");

            Tools.resetContext();
            // return Context.getInstance().getCompleteQuery() + Utils.getTime(start, end);
        } catch (SyntaxErrorException | RecognitionException syntaxError) {
            Tools.resetContext();
            System.out.println(syntaxError.toString());
            // return Context.getInstance().getCompleteQuery() + "999.999";
        } catch (OutOfMemoryError e) {
            emergencyMemory = null;
            System.gc();
            Tools.resetContext();
            System.out.println("Out of memory error. Try again with more memory.\n");
            // return Context.getInstance().getCompleteQuery() + "999.999";
        }
    }

    public static void interactive(String[] args) {

        try {
            Terminal terminal = TerminalBuilder.terminal();
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

            reader.getHistory().add("");

            Tools.clearConsole();

            if (args.length == 0) {
                Tools.showUsageNoArgs();
                Tools.loadDefaultGraph();
            } else {
                Tools.showUsageArgsLoadingCustomGraph(args[0], args[1]);
                Tools.loadCustomGraphFiles(args[0], args[1]);
            }

            String prompt = "PathDB> ";

            // ServerSocket ss = new ServerSocket(12000);
            // System.out.println("Server started on port 12000. Waiting for client connections...");
            // while (true) {
            //     try (Socket clientSocket = ss.accept(); BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            //         String input = in.readLine();
            //         System.out.println("Received: " + input);
            //         Context.getInstance().setCompleteQuery(input);
            //         String res = EvalRPQWithAlgebra();
            //         out.println(res);
            //     }
            // }
            while (true) {
                String line = reader.readLine(prompt);
                reader.getHistory().add(line);
                if (line.equalsIgnoreCase("/h") || line.equalsIgnoreCase("/help")) {
                    Tools.showHelp();
                    System.out.println();
                } else if (line.equalsIgnoreCase("/in") || line.equalsIgnoreCase("/information")) {
                    System.out.println("Graph Information:");
                    System.out.println("Total nodes: " + Graph.getGraph().getNodesQuantity());
                    System.out.println("Total edges: " + Graph.getGraph().getEdgesQuantity());
                    System.out.println("Total label: " + Graph.getGraph().getDifferetEdgesQuantity());
                    System.out.println("Edges per label: " + Graph.getGraph().getEdgesByLabelQuantity().toString());
                    System.out.println("");
                } else if (line.equalsIgnoreCase("/la") || line.equalsIgnoreCase("/labels")) {
                    System.out.println("Samples: ");
                    ArrayList<Edge> edges = Graph.getGraph().getSampleOfEachlabel();
                    for (Edge e : edges) {
                        System.out.println(e.getId() + ": " + e.getSource().getId() + "," + e.getLabel() + "," + e.getTarget().getId());
                    }
                    System.out.println("");
                } else if (line.equalsIgnoreCase("/q") || line.equalsIgnoreCase("/quit")) {
                    System.out.println("Exiting...");
                    System.exit(0);
                } else if (line.endsWith(";")) {
                    try {
                        Context.getInstance().setCompleteQuery(line);
                        EvalRPQWithAlgebra();
                    } catch (OutOfMemoryError e) {
                        System.out.println("Out of memory error. Try again with more memory.\n");
                    } catch (VariableNotFoundException e) {
                        System.out.println(e.toString());
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } else {
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } catch (UserInterruptException e) {
            System.out.println("\nExiting...");
            System.exit(0);
        }
    }

    private static LogicalOperator checkAndAddFilter(LogicalOperator lo, Condition condition) {
        if (condition == null) {
            // Caso sin condiciones
            return lo;
        }
        // if (condition instanceof And) {
        //     // Significa que es un And compuesto
        //     Condition leftCond = ((And) condition).getC1();
        //     Condition rightCond = ((And) condition).getC2();
        //     if (leftCond instanceof First && rightCond instanceof First) {
        //         lo = new LogicalOpSelection(lo, condition);
        //         PredicatePushdownLogicalPlanVisitor v = new PredicatePushdownLogicalPlanVisitor();
        //         lo.acceptVisitor(v);
        //         lo = v.getRoot();
        //         return lo;
        //     }
        //     if (leftCond instanceof First && rightCond instanceof Last) {
        //         lo = new LogicalOpSelection(lo, leftCond);
        //         PredicatePushdownLogicalPlanVisitor v = new PredicatePushdownLogicalPlanVisitor();
        //         lo.acceptVisitor(v);
        //         lo = v.getRoot();
        //         return new LogicalOpSelection(lo, rightCond);
        //     }
        //     if (leftCond instanceof Last && rightCond instanceof First) {
        //         lo = new LogicalOpSelection(lo, rightCond);
        //         PredicatePushdownLogicalPlanVisitor v = new PredicatePushdownLogicalPlanVisitor();
        //         lo.acceptVisitor(v);
        //         lo = v.getRoot();
        //         return new LogicalOpSelection(lo, leftCond);
        //     }
        //     if (leftCond instanceof Last && rightCond instanceof Last) {
        //         lo = new LogicalOpSelection(lo, condition);
        //         PredicatePushdownLogicalPlanVisitor v = new PredicatePushdownLogicalPlanVisitor();
        //         lo.acceptVisitor(v);
        //         lo = v.getRoot();
        //         return lo;
        //     }
        // }
        if (condition instanceof First || condition instanceof Last) {
            // Significa que es un First o Last y simplemente se baja
            lo = new LogicalOpSelection(lo, condition);
            PredicatePushdownLogicalPlanVisitor v = new PredicatePushdownLogicalPlanVisitor();
            lo.acceptVisitor(v);
            lo = v.getRoot();
            return lo;
        }

        // Caso en que sea cualquiera otra condicion no se puede bajar
        return new LogicalOpSelection(lo, condition);
    }

}
