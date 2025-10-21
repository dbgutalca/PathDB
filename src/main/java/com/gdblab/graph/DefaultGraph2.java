package com.gdblab.graph;

import java.util.HashMap;

import com.gdblab.graph.interfaces.InterfaceGraph;
import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.Node;

public final class DefaultGraph2 {

    private static InterfaceGraph graph = Graph.getGraph();

    public static Node[] getDefaultNodes() {
        Node[] nodes = new Node[7];

        HashMap<String, String> m_1_property = new HashMap<>();
        m_1_property.put("txt", "Msg1");
        nodes[0] = new Node("m1", "Message", m_1_property);

        HashMap<String, String> m_2_property = new HashMap<>();
        m_2_property.put("txt", "Msg2");
        nodes[1] = new Node("m2", "Message", m_2_property);

        HashMap<String, String> m_3_property = new HashMap<>();
        m_3_property.put("txt", "Msg3");
        nodes[2] = new Node("m3", "Message", m_3_property);

        HashMap<String, String> p_1_property = new HashMap<>();
        p_1_property.put("name", "Moe");
        nodes[3] = new Node("p1", "Person", p_1_property);

        HashMap<String, String> p_2_property = new HashMap<>();
        p_2_property.put("name", "Bart");
        nodes[4] = new Node("p2", "Person", p_2_property);

        HashMap<String, String> p_3_property = new HashMap<>();
        p_3_property.put("name", "Lisa");
        nodes[5] = new Node("p3", "Person", p_3_property);

        HashMap<String, String> p_4_property = new HashMap<>();
        p_4_property.put("name", "Apu");
        nodes[6] = new Node("p4", "Person", p_4_property);

        return nodes;
    }

    public static Edge[] getDefaultEdges() {
        Edge[] edges = new Edge[11];
        edges[0] = new Edge("E1", "knows", graph.getNode("p1"), graph.getNode("p2"), new HashMap<String, String>());
        edges[1] = new Edge("E2", "knows", graph.getNode("p2"), graph.getNode("p3"), new HashMap<String, String>());
        edges[2] = new Edge("E3", "knows", graph.getNode("p3"), graph.getNode("p2"), new HashMap<String, String>());
        edges[3] = new Edge("E4", "knows", graph.getNode("p2"), graph.getNode("p4"), new HashMap<String, String>());
        edges[4] = new Edge("E5", "likes", graph.getNode("p2"), graph.getNode("m3"), new HashMap<String, String>());
        edges[5] = new Edge("E6", "likes", graph.getNode("p4"), graph.getNode("m3"), new HashMap<String, String>());
        edges[6] = new Edge("E7", "likes", graph.getNode("p3"), graph.getNode("m2"), new HashMap<String, String>());
        edges[7] = new Edge("E8", "likes", graph.getNode("p1"), graph.getNode("m1"), new HashMap<String, String>());
        edges[8] = new Edge("E9", "hasCreator", graph.getNode("m3"), graph.getNode("p1"), new HashMap<String, String>());
        edges[9] = new Edge("E10", "hasCreator", graph.getNode("m2"), graph.getNode("p4"), new HashMap<String, String>());
        edges[10] = new Edge("E11", "hasCreator", graph.getNode("m1"), graph.getNode("p3"), new HashMap<String, String>());
        return edges;
    }
}
