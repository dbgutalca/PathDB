package com.gdblab.execution;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.gdblab.algebra.parser.RPQErrorListener;
import com.gdblab.algebra.parser.RPQExpression;
import com.gdblab.algebra.parser.RPQGrammarListener;
import com.gdblab.algebra.parser.error.SyntaxErrorException;
import com.gdblab.algebra.parser.impl.RPQtoAlgebraVisitor;
import com.gdblab.algebra.queryplan.logical.LogicalOperator;
import com.gdblab.algebra.queryplan.logical.impl.LogicalOpSelection;
import com.gdblab.algebra.queryplan.logical.visitor.LogicalToBFPhysicalVisitor;
import com.gdblab.algebra.queryplan.physical.PhysicalOperator;
import com.gdblab.algebra.queryplan.util.Utils;
import com.gdblab.response.ResponseQuery;
import com.gdlab.parser.RPQGrammarLexer;
import com.gdlab.parser.RPQGrammarParser;

public final class Execute {

    public static ResponseQuery EvalRPQWithAlgebra(){
        
        byte[] emergencyMemory = new byte[1024 * 1024];
        
        try {
            long start = System.nanoTime();
    
            PhysicalOperator po = null;

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
                lo = new LogicalOpSelection(lo, Context.getInstance().getCondition());
            }

            LogicalToBFPhysicalVisitor visitor2 = new LogicalToBFPhysicalVisitor();
            lo.acceptVisitor(visitor2);
            po = visitor2.getPhysicalPlan().getRootOperator();
            
            ResponseQuery response = Utils.calculatePaths(po);
                
            long end = System.nanoTime();

            response.setTime(Utils.getTime(start, end));
            response.setSuccess(true);
            response.setMessage("Paths calculated successfully");
            
            Tools.resetContext();
            return response;
        }
        catch (SyntaxErrorException syntaxError) {
            ResponseQuery response = new ResponseQuery();
            response.setMessage("Error: Syntax Error.");
            Tools.resetContext();
            return response;
        }
        catch (OutOfMemoryError e) {
            ResponseQuery response = new ResponseQuery();
            response.setMessage("Error: Out of Memory Error.");
            emergencyMemory = null;
            System.gc();
            Tools.resetContext();
            return response;
        }
        catch (RecognitionException e) {
            ResponseQuery response = new ResponseQuery();
            response.setMessage("Error: Generic Error.");
            Tools.resetContext();
            return response;
        }
    }

}