package com.gdblab.execution;

import com.gdblab.schema.Edge;
import com.gdblab.schema.Node;

public final class DefaultGraph {

    public static Node[] loadDefaultNodes(){
        Node[] nodes = new Node[3];
        nodes[0] = new Node("N1", "N1");
        nodes[1] = new Node("N2", "N2");
        nodes[2] = new Node("N3", "N3");

        return nodes;
    }

    public static Edge[] loadDefaultEdges(){
        Edge[] edges = new Edge[5];
        edges[0] = new Edge("E1", "A", Context.getInstance().getGraph().getNode("N1"), Context.getInstance().getGraph().getNode("N2"));
        edges[1] = new Edge("E3", "B", Context.getInstance().getGraph().getNode("N2"), Context.getInstance().getGraph().getNode("N2"));
        edges[2] = new Edge("E6", "D", Context.getInstance().getGraph().getNode("N2"), Context.getInstance().getGraph().getNode("N3"));
        edges[3] = new Edge("E4", "C", Context.getInstance().getGraph().getNode("N2"), Context.getInstance().getGraph().getNode("N2"));
        edges[4] = new Edge("E2", "A", Context.getInstance().getGraph().getNode("N3"), Context.getInstance().getGraph().getNode("N2"));
        

        return edges;
    }

}
