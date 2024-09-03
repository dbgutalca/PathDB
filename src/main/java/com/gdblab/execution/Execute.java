package com.gdblab.execution;

import java.io.File;
import java.io.FileWriter;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.gdblab.algebra.condition.And;
import com.gdblab.algebra.condition.First;
import com.gdblab.algebra.condition.Label;
import com.gdblab.algebra.condition.Last;
import com.gdblab.parser.RPQExpression;
import com.gdblab.parser.RPQGrammarListener;
import com.gdblab.parser.impl.RPQtoAlgebraVisitor;
import com.gdblab.queryplan.logical.LogicalOperator;
import com.gdblab.queryplan.logical.impl.LogicalOpSelection;
import com.gdblab.queryplan.logical.visitor.LogicalToBFPhysicalVisitor;
import com.gdblab.queryplan.physical.PhysicalOperator;
import com.gdblab.queryplan.util.Utils;
import com.gdblab.schema.GraphObject;
import com.gdblab.schema.Path;
import com.gdlab.parser.RPQGrammarLexer;
import com.gdlab.parser.RPQGrammarParser;

public final class Execute {

    private static long counter;
    
    public static void EvalRPQ(String rpq){

        long start = System.nanoTime();

        counter = 1;

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
        if ( !Context.getInstance().getStartingNode().equalsIgnoreCase("") ) {
            lo = filterOnTopLeft(lo);
        }

        if ( !Context.getInstance().getEndingNode().equalsIgnoreCase("") ) {
            lo = filterOnTopRight(lo);
        }

        LogicalToBFPhysicalVisitor visitor2 = new LogicalToBFPhysicalVisitor();
        lo.acceptVisitor(visitor2);
        PhysicalOperator po = visitor2.getPhysicalPlan().getRootOperator();

        switch (Context.getInstance().getOutputType()) {
            case "console":
                printPath(po);
                break;
            case "file":
                writePath(po);
                break;
            default:
                break;
        }

        long end = System.nanoTime();
        System.out.println("\nTotal paths: " + (counter - 1) + " paths");
        System.out.println("Execution time: " + Utils.getTime(start, end) + " seconds");
        System.out.println("");
    }

    private static void printPath(PhysicalOperator po){
        Integer ms = Context.getInstance().getMaxShowedPaths();
        if (ms <= 0) {
            while (po.hasNext()) {
                Path p = po.next();
                System.out.print("Path #" + counter + " - ");
                for (GraphObject go : p.getSequence()) {
                    System.out.print( go.getLabel() + " ");
                }
                System.out.println();
                counter++;
            }
            return;
        }

        while ( po.hasNext() ) {
            Path p = po.next();

            if (counter <= ms) {
                System.out.print("Path #" + counter + " - ");
                for (GraphObject go : p.getSequence()) {
                    System.out.print( go.getLabel() + " ");
                }
                System.out.println();
            }
            if (counter == (ms + 1)) {
                System.out.println("...");
            }
            counter++;
        }
    }

    private static void writePath(PhysicalOperator po){
        File file = new File(Context.getInstance().getOutputFileName());
        try (FileWriter writer = new FileWriter(file)) {
            while ( po.hasNext() ) {
                Path p = po.next();
                writer.write("Path #" + counter + " - ");
                for (GraphObject go : p.getSequence()) {
                    writer.write( go.getLabel() + " ");
                }
                writer.write("\n");
                counter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static LogicalOperator filterOnTopLeft(LogicalOperator lo) {
        return new LogicalOpSelection(lo, new First(Context.getInstance().getStartingNode()));
    }

    private static LogicalOperator filterOnTopRight(LogicalOperator lo) {
        return new LogicalOpSelection(lo, new Last(Context.getInstance().getEndingNode()));
    }
}
