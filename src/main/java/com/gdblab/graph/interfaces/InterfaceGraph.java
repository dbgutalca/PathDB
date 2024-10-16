package com.gdblab.graph.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.gdblab.graph.schema.*;

public interface InterfaceGraph {

    public Node getNode(final String id);

    public Iterator<Node> getNodeIterator();

    public Iterator<Edge> getEdgeIterator();

    public Node insertNode(final Node node);

    public Edge insertEdge(final Edge edge);

    // Statistics methods

    public Integer getNodesQuantity();

    public Integer getEdgesQuantity();

    public Integer getDifferetEdgesQuantity();
    
    public HashMap<String, Integer> getEdgesByLabelQuantity();

    public ArrayList<Edge> getSampleOfEachlabel();
}
