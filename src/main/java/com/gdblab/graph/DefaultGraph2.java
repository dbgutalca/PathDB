package com.gdblab.graph;

import com.gdblab.graph.interfaces.InterfaceGraph;
import com.gdblab.graph.schema.*;

public final class DefaultGraph2 {

    private static InterfaceGraph graph = Graph.getGraph();
    
    public static Node[] getDefaultNodes(){
        Node[] nodes = new Node[7];

        nodes[0] = new Node("m1", "Message");
        nodes[1] = new Node("m2", "Message");
        nodes[2] = new Node("m3", "Message");
        nodes[3] = new Node("p1", "Person");
        nodes[4] = new Node("p2", "Person");
        nodes[5] = new Node("p3", "Person");
        nodes[6] = new Node("p4", "Person");

        return nodes;
    }

    public static Edge[] getDefaultEdges() {
        Edge[] edges = new Edge[11];
        edges[0] = new Edge("E1", "knows", graph.getNode("p1"), graph.getNode("p2"));
        edges[1] = new Edge("E2", "knows", graph.getNode("p2"), graph.getNode("p3"));
        edges[2] = new Edge("E3", "knows", graph.getNode("p3"), graph.getNode("p2"));
        edges[3] = new Edge("E4", "knows", graph.getNode("p2"), graph.getNode("p4"));
        edges[4] = new Edge("E5", "likes", graph.getNode("p2"), graph.getNode("p3"));
        edges[5] = new Edge("E6", "likes", graph.getNode("p4"), graph.getNode("p3"));
        edges[6] = new Edge("E7", "likes", graph.getNode("p3"), graph.getNode("p2"));
        edges[7] = new Edge("E8", "likes", graph.getNode("p1"), graph.getNode("p1"));
        edges[8] = new Edge("E9", "hasCreator", graph.getNode("m3"), graph.getNode("p1"));
        edges[9] = new Edge("E10", "hasCreator", graph.getNode("m2"), graph.getNode("p4"));
        edges[10] = new Edge("E11", "hasCreator", graph.getNode("m1"), graph.getNode("p3"));
        return edges;
    }
}
