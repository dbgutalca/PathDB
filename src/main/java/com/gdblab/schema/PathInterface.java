package com.gdblab.schema;

public interface PathInterface {
    
    public Node first();
    public Node last();
    public int lenght();
    public PathInterface join(PathInterface pathB);

    public void insertEdge(final Edge e);
    public void insertNode(final Node n);
    
}
