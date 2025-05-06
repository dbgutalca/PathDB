package com.gdblab.main;

import com.gdblab.execution.Execute;

public class Main {

    // MATCH p = (X)-[A.B]->(Y) WHERE FIRST().PROP = "algo" RETURN p;

    // RETURN
    //     FIRST()
    //     FIRST().PROP
    //     LAST()
    //     LAST().PROP
    //     NODE(#)
    //     NODE(#).PROP
    //     EDGE(#)
    //     EDGE(#).PROP

    public static void main(String[] args) {

        if (args.length == 4) {

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
            }

            String[] files = { nodes_file, edges_file };
            
            Execute.interactive(files);
        }

        else if (args.length == 0) {
            String[] files = {};
            Execute.interactive(files);
        }

        else {
            System.out.println("Invalid arguments. Use -n nodes_file.txt -e edges_file.txt to use a custom graph");
            System.exit(1);
        }
            
    }
    
}
