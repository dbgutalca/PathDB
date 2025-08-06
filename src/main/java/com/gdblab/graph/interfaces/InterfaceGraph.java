package com.gdblab.graph.interfaces;

import java.util.Iterator;

public interface InterfaceGraph {

    public void insertNode(final String id, final String nodeObject);

    public void insertEdge(final String label, final String edgeObject);

    public String getNode(final String id);

    public String getEdge(final String id);

    public Iterator<String> getNodeIterator();

    public Iterator<String> getEdgeIteratorByLabel(final String label);

    public Iterator<String> getEdgeIteratorByNegatedLabel(final String label);

    public void cleanNodes();

}
