package com.gdblab.graph.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.Node;

public interface InterfaceGraph {

    public Node getNode(final String id);

    public Iterator<Node> getNodeIterator();

    public Iterator<Edge> getEdgeIterator();

    public Iterator<Edge> getEdgeIteratorByLabel(final String label);

    public Node insertNode(final Node node);

    public Edge insertEdge(final Edge edge);

    public HashSet<Edge> getNeighbours(final String nodeID);

    // Statistics methods

    public Integer getNodesQuantity();

    public Integer getEdgesQuantity();

    public Integer getDifferetEdgesQuantity();
    
    public HashMap<String, Integer> getEdgesByLabelQuantity();

    public ArrayList<Edge> getSampleOfEachlabel();

    public void cleanDatabase();

    public boolean isDatabaseLoaded();
    
    public void setDatabaseLoaded(boolean flag);
}
