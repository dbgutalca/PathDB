package com.gdblab.main;

import java.util.Scanner;

import com.gdblab.execution.Context;
import com.gdblab.execution.Execute;
import com.gdblab.queryplan.util.Utils;
import com.gdblab.schema.Graph;
import com.gdblab.schema.impl.CSRVPMin;

public class Main {
    
    public static void main(String[] args){

        System.out.println("Start configuration...");

        Context context = Context.getInstance();
        
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
                    
                case "-dt":
                    context.setDataType(arg[1]);
                    context.setGraph(getDataTypeGraph(arg[1]));
                    System.out.println("Graph created");
                    break;
                case "--data_type":
                    context.setDataType(arg[1]);
                    context.setGraph(getDataTypeGraph(arg[1]));
                    System.out.println("Graph created");
                    break;
                    
                case "-nf":
                    long startnf = System.nanoTime();
                    context.uploadNodes(arg[1]);
                    long endnf = System.nanoTime();
                    System.out.println(
                        "Uploaded Nodes in " + 
                        Utils.getTime(startnf, endnf) + " seconds"
                    );
                    break;
                case "--nodes_file":
                    long startnfc = System.nanoTime();
                    context.uploadNodes(arg[1]);
                    long endnfc = System.nanoTime();
                    System.out.println(
                        "Uploaded Nodes in " + 
                        Utils.getTime(startnfc, endnfc) + " seconds"
                    );
                    break;

                case "-ef":
                    long startef = System.nanoTime();
                    context.uploadEdges(arg[1]);
                    long endef = System.nanoTime();
                    System.out.println(
                        "Uploaded Edges in " + 
                        Utils.getTime(startef, endef) + " seconds"
                    );
                    break;
                case "--edges_file":
                    long startefc = System.nanoTime();
                    context.uploadEdges(arg[1]);
                    long endefc = System.nanoTime();
                    System.out.println(
                        "Uploaded Edges in " + 
                        Utils.getTime(startefc, endefc) + " seconds"
                    );
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
                    context.setMaxShowedPaths(Integer.valueOf(arg[1]));
                    break;
                case "--max_showed":
                    context.setMaxShowedPaths(Integer.valueOf(arg[1]));
                    break;
                
                default:
                    // Print error message
                    System.out.println("Invalid argument: " + arg[0]);

                    // Print usage of arguments
                    System.out.println("Usage: ");
                    System.out.println("  -dt, --data_type: Data type of the graph");
                    System.out.println("  -nf, --nodes_file: File path of the nodes");
                    System.out.println("  -ef, --edges_file: File path of the edges");
                    System.out.println("  -fp, --fixed_point: Fixed point of the recursion");
                    System.out.println("  -of, --output_file: Output file name");
                    System.out.println("  -ms, --max_showed: Max showed paths");
                    System.out.println("  -h, --help: Show usage");
                    // Exit
                    System.exit(1);
                    break;
            }
        }
        
        System.out.println("Configuration completed\n");
    
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

    private static Graph getDataTypeGraph(String data_type){
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
        System.out.println("  -dt, --data_type: Data type of the graph");
        System.out.println("  -nf, --nodes_file: File path of the nodes");
        System.out.println("  -ef, --edges_file: File path of the edges");
        System.out.println("  -fp, --fixed_point: Fixed point of the recursion");
        System.out.println("  -of, --output_file: Output file name");
        System.out.println("  -ms, --max_showed: Max showed paths");
        System.out.println("  -h, --help: Show usage");
    }
}
