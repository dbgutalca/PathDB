package com.gdblab.main;

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
    private static String[] data;

    public static void main(String[] args) {

        for (String string : args) {
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
                    context.setFixPoint(Integer.valueOf(arg[1]));
                    break;
                case "--fix_point":
                    context.setFixPoint(Integer.valueOf(arg[1]));
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
                    if (arg[1].equalsIgnoreCase("all")) {
                        System.out.println("Showing all paths");
                        context.setMaxShowedPaths(-1);
                    } else {
                        context.setMaxShowedPaths(Integer.valueOf(arg[1]));
                    }
                    break;
                case "--max_showed":
                    if (arg[1].equalsIgnoreCase("all")) {
                        context.setMaxShowedPaths(-1);
                    } else {
                        context.setMaxShowedPaths(Integer.valueOf(arg[1]));
                    }
                    break;

                case "-rpq":
                    data = preProcessRPQData(arg[1]);
                    context.setStartingNode(data[0]);
                    context.setRPQ(data[1]);
                    context.setEndingNode(data[2]);
                    break;
                case "--regular_path_query":
                    data = preProcessRPQData(arg[1]);
                    context.setStartingNode(data[0]);
                    context.setRPQ(data[1]);
                    context.setEndingNode(data[2]);
                    break;

                default:
                    System.out.println("Invalid argument: " + arg[0]);
                    printUsage();
                    System.exit(1);
                    break;
            }
        }

        if (context.getRPQ().equals("")) {
            System.out.println("Regular Path Query is obligatory, use -rpq=X or --regular_path_query=X flag");
            System.exit(1);
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
        Execute.EvalRPQ(context.getRPQ());
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar PathDB.jar [options]");
        // System.out.println(" -dt, --data_type: Structure of the graph, default is
        // CSRVPMin");
        System.out.println(
                "  -nf, --nodes_file: File path of the nodes, if -nf and -ef are not set, the default graph will be used.");
        System.out.println(
                "  -ef, --edges_file: File path of the edges, if -nf and -ef are not set, the default graph will be used.");
        System.out.println("  -rpq, --regular_path_query: The Regular Path Query to be executed (Obligatory) with format '(X,rpq,Y)'.");
        System.out.println("  -fp, --fixed_point: Fixed point of the recursion.");
        System.out.println(
                "  -of, --output_file: Output file name [Make a copy of the file for each RPQ, the file will be overwritten].");
        System.out.println("  -ms, --max_showed: Max showed paths on console, default is 10, all to show all paths.");
        System.out.println("  -h, --help: Show usage.");
        System.out.println("  \\q: Exit the program\n");
        System.out
                .println("Example: java -jar PathDB.jar -nf=nodes.txt -ef=edges.txt -rpq=A.B* -fp=3 -of=output.txt\n");
    }

    private static void printConfiguration() {
        System.out.println("Configuration:");
        System.out.println("    Graph Structure: " + context.getDataType() + ".");

        if (defaultGraph) {
            System.out.println("    Graph: Default.");
        } else {
            System.out.println("    Graph:");
            System.out.println(
                    "        Nodes file: " + context.getNodesFileName() + " (Loaded in " + nodesUploadTime + ").");
            System.out.println(
                    "        Edges file: " + context.getEdgesFileName() + " (Loaded in " + edgesUploadTime + ").");
        }

        System.out.println("    Regular Path Query: " + context.getRPQ() + ".");

        if (context.getStartingNode().equals("X")) {
            System.out.println("    Starting Node: Any.");
        } else {
            System.out.println("    Starting Node: " + context.getStartingNode() + ".");
        }

        if (context.getEndingNode().equals("Y")) {
            System.out.println("    Ending Node: Any.");
        } else {
            System.out.println("    Ending Node: " + context.getEndingNode() + ".");
        }

        System.out.println("    Fix Point: " + context.getFixPoint() + ".");

        if (context.getOutputType().equals("console")) {
            System.out.println("    Output Type: Console.");
            System.out.println("    Max Showed Paths: " + context.getMaxShowedPaths() + ".");
        } else {
            System.out.println("    Output Type: File.");
            System.out.println("    Output File Name: " + context.getOutputFileName() + ".");
        }
        System.out.println();
    }

    private static Graph getGraphStructure(String data_type) {
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

    private static String[] preProcessRPQData(String rpq) {
        String[] data = rpq.trim().split(",");
        data[0] = data[0].replaceAll("\\(", "");
        data[2] = data[2].replaceAll("\\)", "");

        if (data.length != 3) {
            System.out.println("Invalid Regular Path Query: " + rpq);
            printUsage();
            System.exit(1);
        }
        
        return data;
    }
}
