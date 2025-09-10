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

import com.gdblab.algebra.parser.RPQErrorListener;
import com.gdblab.algebra.parser.RPQExpression;
import com.gdblab.algebra.parser.RPQGrammarListener;
import com.gdblab.algebra.parser.error.SyntaxErrorException;
import com.gdblab.algebra.parser.error.VariableNotFoundException;
import com.gdblab.algebra.parser.impl.RPQtoAlgebraVisitor;
import com.gdblab.algebra.queryplan.logical.LogicalOperator;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpSelection;
import com.gdblab.algebra.queryplan.logical.visitor.LogicalToBFPhysicalVisitor;
import com.gdblab.algebra.queryplan.physical.PhysicalOperator;
import com.gdblab.algebra.queryplan.util.Utils;
import com.gdblab.graph.Graph;
import com.gdblab.graph.schema.Edge;
import com.gdlab.parser.RPQGrammarLexer;
import com.gdlab.parser.RPQGrammarParser;

public final class Execute {

    public static void EvalRPQWithAlgebra(){
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

            
            if (Context.getInstance().getCondition() != null) {
                lo = addFilter(lo);
            }

            LogicalToBFPhysicalVisitor visitor2 = new LogicalToBFPhysicalVisitor();
            lo.acceptVisitor(visitor2);
            po = visitor2.getPhysicalPlan().getRootOperator();
            
            counter = Utils.printAndCountPaths(po);
                
            long end = System.nanoTime();
            System.out.println("\nTotal paths: " + (counter - 1) + " paths");
            System.out.println("Execution time: " + Utils.getTime(start, end) + " seconds");
            System.out.println("");
            Tools.resetContext();
        }
        catch (SyntaxErrorException syntaxError) {
            System.out.println(syntaxError.toString());
            Tools.resetContext();
        }
        catch (OutOfMemoryError e) {
            emergencyMemory = null;
            System.gc();
            Tools.resetContext();
        }
        catch (RecognitionException e) {
            Tools.resetContext();
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
            }
            else {
                Tools.showUsageArgsLoadingCustomGraph(args[0], args[1]);
                Tools.loadCustomGraphFiles(args[0], args[1]);
            }

            String prompt = "PathDB> ";

            while (true) {

                String line = reader.readLine(prompt);

                reader.getHistory().add(line);

                if (line.equals("/h") || line.equals("/help")) {
                    Tools.showHelp();
                    System.out.println();
                }

                else if (line.equals("/in") || line.equals("/information")) {
                    System.out.println("Graph Information:");
                    System.out.println("Total nodes: " + Graph.getGraph().getNodesQuantity());
                    System.out.println("Total edges: " + Graph.getGraph().getEdgesQuantity());
                    System.out.println("Total label: " +
                    Graph.getGraph().getDifferetEdgesQuantity());
                    System.out.println("Edges per label: " +
                    Graph.getGraph().getEdgesByLabelQuantity().toString());
                    System.out.println("");
                }

                else if (line.equals("/la") || line.equals("/labels")) {
                    System.out.println("Samples: ");
                    ArrayList<Edge> edges = Graph.getGraph().getSampleOfEachlabel();
                    for (Edge e : edges) {
                        System.out.println(e.getId() + ": " + e.getSource().getId() + "," + e.getLabel() + "," + e.getTarget().getId());
                    }
                    System.out.println("");
                }

                else if (line.endsWith(";")) {
                    try {
                        Context.getInstance().setCompleteQuery(line);
                        EvalRPQWithAlgebra();   
                    }
                    catch (OutOfMemoryError e) {
                        System.out.println("Out of memory error. Try again with more memory.\n");
                    }
                    catch (VariableNotFoundException e) {
                        System.out.println(e.toString());
                    }
                    catch (Exception e) {
                        System.out.println(e);
                    }
                }

                else {

                }

            }

        }
        catch (IOException e) {
            System.out.println(e.toString());
        }
        catch (UserInterruptException e) {
            System.out.println("\nExiting...");
            System.exit(0);
        }
    }

    public static LogicalOperator addFilter(LogicalOperator lo) {
        return new LogicalOpSelection(lo, Context.getInstance().getCondition());
    }

}