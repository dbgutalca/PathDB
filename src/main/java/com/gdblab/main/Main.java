package com.gdblab.main;

import com.gdblab.execution.Execute;

public class Main {
    // O.(X.O)*X?
    // MATCH WALK p = (x)-[knows*]->(y) WHERE x.id="m1003606" RETURN p LIMIT 1;

    public static void main(String[] args) {

        switch (args.length) {
            case 4 -> {
                    String nodes_file = "";
                    String edges_file = "";
                    int i = 0;
                    while ( i < 4 ) {
                        if (args[i].equals("-n")) {
                            nodes_file = args[i + 1];
                        }
                        else if (args[i].equals("-e")) {
                            edges_file = args[i + 1];
                        }
                        i += 2;
                    }       String[] files = { nodes_file, edges_file };
                    Execute.interactive(files);
                }
            case 0 -> {
                    String[] files = {};
                    Execute.interactive(files);
                }
            default -> {
                System.out.println("Invalid arguments. Use -n nodes_file.txt -e edges_file.txt to use a custom graph");
                System.exit(1);
            }
        }
            
    }
    
}
