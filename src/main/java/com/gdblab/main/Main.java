package com.gdblab.main;

import com.gdblab.execution.Execute;

public class Main {

    public static void main(String[] args) {

        /*
         * Ejecución del programa con 6 argumentos.
         * 
         * Se ejecuta el modo experimental, esto quiere decir que
         * se ejecutaran todas las consultas de un archivo de consultas.
         * 
         * Todas las salidas son mediante archivos que contienen los resultados
         * de las ejecuciones entregadas en el arhcivo de consultas.
         * 
         * Su uso se restringe para los experimentos de la investigación.
         */
        if (args.length == 6) {

            String nodes_file = "";
            String edges_file = "";
            String rpq_file = "";

            int i = 0;

            while ( i < 6 ) {
                if (args[i].equals("-n")) {
                    nodes_file = args[i + 1];
                }
                else if (args[i].equals("-e")) {
                    edges_file = args[i + 1];
                }
                else if (args[i].equals("-r")) {
                    rpq_file = args[i + 1];
                }
                i += 2;
            }

            String[] files = { nodes_file, edges_file, rpq_file };

            Execute.experimental(files);
        }

        /*
         * Ejecución del programa con 4 argumentos.
         * 
         * Se ejecuta el modo interactivo pero con un grafo creado.
         * 
         * Las salidas son en pantalla con un máximo de 10 resultados,
         * Siendo estos los primeros 10 resultados obtenidos.
         * 
         * Es de uso libre.
         */
        else if (args.length == 4) {

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
