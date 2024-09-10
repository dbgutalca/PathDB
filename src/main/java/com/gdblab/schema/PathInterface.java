package com.gdblab.schema;

public interface PathInterface {
    
    public Node first();
    public Node last();
    public int lenght();

    public boolean isLinkeable(Path pathB);
    public Path join(Path pathB);

    public void insertEdge(final Edge e);
    public void insertNode(final Node n);
    
    public boolean isTrail(final Path pathB);

    public boolean isLabelAt(int pos, String label);
    public String getIdAt(int pos);

    public String toString();
}
