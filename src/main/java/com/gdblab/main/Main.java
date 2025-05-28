package com.gdblab.main;

import com.gdblab.execution.Execute;

public class Main {

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

        /*
         * Ejecución del programa con 0 argumentos.
         * 
         * Se ejecuta el modo interactivo con el grafo por defecto.
         * 
         * las salidas son en pantalla con un máximo de 10 resultados,
         * siendo estos los primeros 10 resultados obtenidos.
         * 
         * Es de uso libre.
         */
        else if (args.length == 0) {
            String[] files = {};
            Execute.interactive(files);
        }

        /*
         * Error en la ejecución del programa.
         */
        else {
            System.out.println("Invalid arguments. Use -n nodes_file.txt -e edges_file.txt to use a custom graph");
            System.exit(1);
        }
            
    }
    
}
