package com.gdblab.execution;

import java.io.IOException;
import java.util.ArrayList;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import com.gdblab.algebra.parser.RPQErrorListener;
import com.gdblab.algebra.parser.RPQExpression;
import com.gdblab.algebra.parser.RPQGrammarListener;
import com.gdblab.algebra.parser.error.ErrorDetails;
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

    private static final String prefix = "/";

    public static void EvalRPQWithAlgebra(){
        // MATCH p=(x)-[Knows]->(y) RETURN p LIMIT 10;
        long start = System.nanoTime();

        int counter = 1;        

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
        PhysicalOperator po = visitor2.getPhysicalPlan().getRootOperator();

        counter = Utils.printAndCountPaths(po);
        long end = System.nanoTime();
        System.out.println("\nTotal paths: " + (counter - 1) + " paths");
        System.out.println("Execution time: " + Utils.getTime(start, end) + " seconds");
        System.out.println("");
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
                    Tools.showHelp();
                    System.out.println();
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

                    Context.getInstance().setMaxPathsLength(Integer.parseInt(parts[1]));
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

                    Context.getInstance().setTotalPathsToShow(Integer.parseInt(parts[1]));
                    System.out.println("Set show paths configuration to: " + parts[1] + "\n");
                    continue;
                }
                
                else if (line.endsWith(";")) {
                    try {
                        Context.getInstance().setCompleteQuery(line);
                        EvalRPQWithAlgebra();   
                    }
                    catch (OutOfMemoryError e) {
                        System.out.println("Out of memory error. Try again with more memory.\n");
                    }
                    catch (SyntaxErrorException see) {
                        System.out.println(see.toString());
                    }
                    catch (VariableNotFoundException e) {
                        System.out.println(e.toString());
                    }
                }

                else if (line.equals(prefix + "in") || line.equals(prefix + "information")) {
                    // clearConsole();
                    System.out.println("Graph Information:");
                    System.out.println("Total nodes: " + Graph.getGraph().getNodesQuantity());
                    System.out.println("Total edges: " + Graph.getGraph().getEdgesQuantity());
                    System.out.println("Total label: " + Graph.getGraph().getDifferetEdgesQuantity());
                    System.out.println("Edges per label: " + Graph.getGraph().getEdgesByLabelQuantity().toString());
                    System.out.println("\n");
                    continue;
                }
                
                else if (line.equals(prefix + "la") || line.equals(prefix + "labels")) {
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
        catch (IOException e) {
            System.out.println(e.toString());
        }
        // catch (Exception e) {
        //     System.out.println("Any Error");
        // }
    }

    public static LogicalOperator addFilter(LogicalOperator lo) {
        return new LogicalOpSelection(lo, Context.getInstance().getCondition());
    }
}