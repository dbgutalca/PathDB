package com.gdblab.tester;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.gdblab.parser.RPQExpression;
import com.gdblab.parser.RPQGrammarListener;
import com.gdblab.parser.impl.RPQtoAlgebraVisitor;
import com.gdblab.queryplan.logical.LogicalOperator;
import com.gdblab.queryplan.logical.visitor.LogicalToBFPhysicalVisitor;
import com.gdblab.queryplan.physical.PhysicalOperator;
import com.gdblab.queryplan.physical.impl.PhysicalOp;
import com.gdblab.schema.Edge;
import com.gdblab.schema.Graph;
import com.gdblab.schema.GraphObject;
import com.gdblab.schema.Node;
import com.gdblab.schema.Path;
import com.gdblab.schema.impl.CSRVPMin;
import com.gdblab.schema.impl.MemoryGraph;
import com.gdlab.parser.RPQGrammarLexer;
import com.gdlab.parser.RPQGrammarParser;
import java.util.Scanner;

public class Fulltest {
    
    /*
        --dt == 'csrvp | csr | memorygraph | csrvpmin' 
        --nf == 'node_file_csv'
        --ef == 'edge_file_csv'
        --fp == 3 (default)
    */
    
    public static Graph graph = null;
    
    public static void main(String[] args) {
        
        graph = new CSRVPMin();
        
        Scanner sc = new Scanner(System.in);
        
        Node n1 = new Node("N1", "N1");
        Node n2 = new Node("N2", "N2");
        Node n3 = new Node("N3", "N3");

        Edge e1 = new Edge("E1", "A", n1, n2);
        Edge e2 = new Edge("E2", "B", n2, n2);
        Edge e3 = new Edge("E3", "C", n2, n3);
        
        MemoryGraph.getInstance().insertNode(n1);
        MemoryGraph.getInstance().insertNode(n2);
        MemoryGraph.getInstance().insertNode(n3);
        
        MemoryGraph.getInstance().insertEdge(e1);
        MemoryGraph.getInstance().insertEdge(e2);
        MemoryGraph.getInstance().insertEdge(e3);
         
        System.out.println("Graph loaded...");
        
        while (true) {
            System.out.print("RPQ: ");
            String rpq = sc.nextLine();
            
            if (rpq.equals("quit")){
                System.exit(0);
            }

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
            LogicalToBFPhysicalVisitor visitor2 = new LogicalToBFPhysicalVisitor();
            lo.acceptVisitor(visitor2);
            PhysicalOperator po = visitor2.getPhysicalPlan().getRootOperator();
            
            int i = 1;
            
            while ( po.hasNext() ) {
                Path p = po.next();
                System.out.print("#" + i + " - ");
                System.out.print(p.getId() + ": ");
                for (GraphObject go : p.getSequence()) {
                    System.out.print( go.getLabel() + " ");
                }
                i++;
                System.out.println();
            }
            
            System.out.println("");
        }

        

    }
}
