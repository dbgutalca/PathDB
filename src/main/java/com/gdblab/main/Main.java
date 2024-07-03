package com.gdblab.main;

import java.util.Scanner;

import com.gdblab.execution.Context;
import com.gdblab.execution.DefaultGraph;
import com.gdblab.execution.Execute;
import com.gdblab.queryplan.util.Utils;
import com.gdblab.schema.Edge;
import com.gdblab.schema.Graph;
import com.gdblab.schema.Node;
import com.gdblab.schema.impl.CSRVPMin;

public class Main {

    private static Context context = Context.getInstance();
    private static Boolean isNodeFile = false;
    private static Boolean isEdgeFile = false;
    private static Boolean defaultGraph = true;
    private static String nodesUploadTime = "";
    private static String edgesUploadTime = "";
    
    public static void main(String[] args){
        
        for( String string : args ){
            String[] arg = string.split("=");

            switch (arg[0]) {
                case "-h":
                    printUsage();
                    System.exit(0);
                    break;
                case "--help":
                    printUsage();
                    System.exit(0);
                    break;
                    
                case "-gs":
                    context.setDataType(arg[1]);
                    context.setGraph(getGraphStructure(arg[1]));
                    break;
                case "--graph_structure":
                    context.setDataType(arg[1]);
                    context.setGraph(getGraphStructure(arg[1]));
                    break;
                    
                case "-nf":
                    long startnf = System.nanoTime();
                    context.uploadNodes(arg[1]);
                    long endnf = System.nanoTime();
                    nodesUploadTime = Utils.getTime(startnf, endnf) + " seconds";
                    isNodeFile = true;
                    break;
                case "--nodes_file":
                    long startnfc = System.nanoTime();
                    context.uploadNodes(arg[1]);
                    long endnfc = System.nanoTime();
                    nodesUploadTime = Utils.getTime(startnfc, endnfc) + " seconds";
                    isNodeFile = true;
                    break;

                case "-ef":
                    if (!isNodeFile) {
                        System.out.println("Need to set the nodes file first, use -nf=X or --node_file=X flag");
                        System.exit(1);
                    }
                    long startef = System.nanoTime();
                    context.uploadEdges(arg[1]);
                    long endef = System.nanoTime();
                    edgesUploadTime = Utils.getTime(startef, endef) + " seconds";
                    isEdgeFile = true;
                    defaultGraph = false;
                    break;
                case "--edges_file":
                    if (!isNodeFile) {
                        System.out.println("Need to set the nodes file first, use -nf=X or --node_file=X flag");
                        System.exit(1);
                    }
                    long startefc = System.nanoTime();
                    context.uploadEdges(arg[1]);
                    long endefc = System.nanoTime();
                    edgesUploadTime = Utils.getTime(startefc, endefc) + " seconds";
                    isEdgeFile = true;
                    defaultGraph = false;
                    break;

                case "-fp":
                    context.setFixedPoint(Integer.valueOf(arg[1]));
                    break;
                case "--fixed_point":
                    context.setFixedPoint(Integer.valueOf(arg[1]));
                    break;

                case "-of":
                    context.setOutputType("file");
                    context.setOutputFileName(arg[1]);
                    break;
                case "--output_file":
                    context.setOutputType("file");
                    context.setOutputFileName(arg[1]);
                    break;

                case "-ms":
                    if(arg[1].equalsIgnoreCase("all")) {
                        System.out.println("Showing all paths");
                        context.setMaxShowedPaths(-1);
                    }
                    else {
                        context.setMaxShowedPaths(Integer.valueOf(arg[1]));
                    }
                    break;
                case "--max_showed":
                    if(arg[1].equalsIgnoreCase("all")) {
                        context.setMaxShowedPaths(-1);
                    }
                    else {
                        context.setMaxShowedPaths(Integer.valueOf(arg[1]));
                    }
                    break;
                
                default:
                    System.out.println("Invalid argument: " + arg[0]);
                    printUsage();
                    System.exit(1);
                    break;
            }
        }

        if (!isNodeFile && !isEdgeFile) {
            for (Node node : DefaultGraph.loadDefaultNodes()) {
                context.getGraph().insertNode(node);
            }
            for (Edge edge : DefaultGraph.loadDefaultEdges()) {
                context.getGraph().insertEdge(edge);
            }
        }

        printConfiguration();
    
        String rpq;
        Scanner sc = new Scanner(System.in);
        System.out.print("RPQ: ");
        rpq = sc.nextLine();
        while (!rpq.equals("\\q")) {
            Execute.EvalRPQ(rpq);
            System.out.print("\nRPQ: ");
            rpq = sc.nextLine();
            
            if (rpq.equals("\\q")) System.out.println("Good bye!");
        }
    }

    private static Graph getGraphStructure(String data_type){
        switch (data_type) {
            case "csrvpmin":
                return new CSRVPMin();

            case "csr":
                return null;
            
            case "csrvp":
                return null;

            case "memorygraph":
                return null;
        
            default:
                return null;
        }
    }
    
    private static void printUsage() {
        System.out.println("Usage: java -jar PathDB.jar [options]");
        // System.out.println("  -dt, --data_type: Structure of the graph, default is CSRVPMin");
        System.out.println("  -nf, --nodes_file: File path of the nodes, if -nf and -ef are not set, the default graph will be used.");
        System.out.println("  -ef, --edges_file: File path of the edges, if -nf and -ef are not set, the default graph will be used.");
        System.out.println("  -fp, --fixed_point: Fixed point of the recursion.");
        System.out.println("  -of, --output_file: Output file name [Make a copy of the file for each RPQ, the file will be overwritten].");
        System.out.println("  -ms, --max_showed: Max showed paths on console, default is 10, all to show all paths.");
        System.out.println("  -h, --help: Show usage.");
        System.out.println("  \\q: Exit the program\n");
        System.out.println("Example: java -jar PathDB.jar -nf=nodes.txt -ef=edges.txt -fp=3 -of=output.txt\n");
    }

    private static void printConfiguration() {
        System.out.println("Configuration:");
        System.out.println("    Graph Structure: " + Context.getInstance().getDataType() + ".");

        if (defaultGraph) {
            System.out.println("    Graph: Default.");
        }
        else {
            System.out.println("    Graph:");
            System.out.println("        Nodes file: " + Context.getInstance().getNodesFileName() + " (Loaded in " + nodesUploadTime + ").");
            System.out.println("        Edges file: " + Context.getInstance().getEdgesFileName() + " (Loaded in " + edgesUploadTime + ").");
        }
        
        System.out.println("    Fixed Point: " + Context.getInstance().getFixedPoint() + ".");
        
        if (context.getOutputType().equals("console")) {
            System.out.println("    Output Type: Console.");
            System.out.println("    Max Showed Paths: " + Context.getInstance().getMaxShowedPaths() + ".");
        }
        else {
            System.out.println("    Output Type: File.");
            System.out.println("    Output File Name: " + Context.getInstance().getOutputFileName() + ".");
        }
        System.out.println();
    }
}
