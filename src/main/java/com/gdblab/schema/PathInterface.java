package com.gdblab.schema;

import java.util.List;

public interface PathInterface {
    
    public Node first();
    public Node last();
    public int lenght();

    public boolean isLinkeable(Path pathB);
    public Path join(Path pathB);

    public void insertEdge(final Edge e);
    public void insertNode(final Node n);
    
    public boolean isTrail();

    public String getLabelAt(int pos);
}
