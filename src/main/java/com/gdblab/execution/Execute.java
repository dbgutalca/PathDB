package com.gdblab.execution;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

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
import com.gdblab.algorithm.versions.v1.BFSRegex;
import com.gdblab.algorithm.versions.v1.DFSRegex;
import com.gdblab.algorithm.versions.v2.BFSAutomaton;
import com.gdblab.algorithm.versions.v2.DFSAutomaton;
import com.gdlab.parser.RPQGrammarLexer;
import com.gdlab.parser.RPQGrammarParser;

public final class Execute {

    public static void EvalRPQWithAlgebra(String rpq){

        long start = System.nanoTime();

        int counter = 1;

        RPQGrammarLexer lexer = new RPQGrammarLexer(CharStreams.fromString(rpq));
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

        counter = Utils.printAndCountPaths(po);

        long end = System.nanoTime();
        System.out.println("\nTotal paths: " + (counter - 1) + " paths");
        System.out.println("Execution time: " + Utils.getTime(start, end) + " seconds");
        System.out.println("");
    }

    public static void EvalRPQWithRegexDFS(String rpq) {
        long start = System.nanoTime();

        DFSRegex dfsRegex = new DFSRegex(rpq);
        dfsRegex.Trail();

        long end = System.nanoTime();
        System.out.println("\nTotal paths: " + dfsRegex.getTotalPaths() + " paths");
        System.out.println("Execution time: " + Utils.getTime(start, end) + " seconds");
        System.out.println("");
    }

    public static void EvalRPQWithRegexBFS(String rpq) {
        long start = System.nanoTime();

        BFSRegex bfsRegex = new BFSRegex(rpq);
        bfsRegex.Trail();

        long end = System.nanoTime();
        System.out.println("\nTotal paths: " + bfsRegex.getTotalPaths() + " paths");
        System.out.println("Execution time: " + Utils.getTime(start, end) + " seconds");
        System.out.println("");
    }

    public static void EvalRPQWithAutomatonDFS(String rpq) {
        long start = System.nanoTime();

        DFSAutomaton dfsAutomaton = new DFSAutomaton(rpq);
        dfsAutomaton.Trail();

        long end = System.nanoTime();
        System.out.println("\nTotal paths: " + dfsAutomaton.getTotalPaths() + " paths");
        System.out.println("Execution time: " + Utils.getTime(start, end) + " seconds");
        System.out.println("");
    }

    public static void EvalRPQWithAutomatonBFS(String rpq) {
        long start = System.nanoTime();

        BFSAutomaton bfsAutomaton = new BFSAutomaton(rpq);
        bfsAutomaton.Trail();
        
        long end = System.nanoTime();
        System.out.println("\nTotal paths: " + bfsAutomaton.getTotalPaths() + " paths");
        System.out.println("Execution time: " + Utils.getTime(start, end) + " seconds");
        System.out.println("");
    }

    // private static void writePath(PhysicalOperator po){
    //     File file = new File(Context.getInstance().getOutputFileName());
    //     try (FileWriter writer = new FileWriter(file)) {
    //         while ( po.hasNext() ) {
    //             Path p = po.next();
    //             writer.write("Path #" + counter + " - ");
    //             for (GraphObject go : p.getSequence()) {
    //                 writer.write( go.getLabel() + " ");
    //             }
    //             writer.write("\n");
    //             counter++;
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    private static LogicalOperator filterOnTopLeft(LogicalOperator lo) {
        return new LogicalOpSelection(lo, new First(Context.getInstance().getStartNode()));
    }

    private static LogicalOperator filterOnTopRight(LogicalOperator lo) {
        return new LogicalOpSelection(lo, new Last(Context.getInstance().getEndNode()));
    }
}
