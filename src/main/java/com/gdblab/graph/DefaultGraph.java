package com.gdblab.graph;

import com.gdblab.graph.interfaces.InterfaceGraph;
import com.gdblab.graph.schema.*;

public final class DefaultGraph {

    private static InterfaceGraph graph = Graph.getGraph();
    
    public static Node[] getDefaultNodes(){
        Node[] nodes = new Node[3];
        // nodes[0] = new Node("N1", "N1");
        // nodes[1] = new Node("N2", "N2");
        // nodes[2] = new Node("N3", "N3");
        return nodes;
    }

    public static Edge[] getDefaultEdges() {
        Edge[] edges = new Edge[5];
        edges[0] = new Edge("E1", "A", graph.getNode("N1"), graph.getNode("N2"));
        edges[1] = new Edge("E2", "A", graph.getNode("N2"), graph.getNode("N2"));
        edges[2] = new Edge("E3", "B", graph.getNode("N2"), graph.getNode("N3"));
        edges[3] = new Edge("E4", "A", graph.getNode("N1"), graph.getNode("N3"));
        edges[4] = new Edge("E5", "B", graph.getNode("N3"), graph.getNode("N1"));
        return edges;
    }
}
