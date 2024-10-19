package com.gdblab.graph;

import com.gdblab.graph.impl.CSRVPMin;
import com.gdblab.graph.interfaces.InterfaceGraph;

public final class Graph {

    public static InterfaceGraph getGraph() {
        /**
         * Aqui va un switch con las distintas
         * estructuras que se desean utilizar
         * para almacenar el grafo.
         * 
         * Las estructuras originales eran:
         * 1. Memory Graph
         * 2. CSR
         * 3. CSRVP
         * 4. CSRVPMin (Actual)
         */
        return CSRVPMin.getInstance();
    }

}
