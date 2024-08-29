package com.gdblab.schema;

import java.util.ArrayList;
import java.util.List;

public class PathString implements PathInterface {

    private Node start;
    private ArrayList<String> path; // This is the path in string format of [ID_NODE_1, ID_EDGE_1, ID_NODE_2 ...
    private Node end;

    // Constructor for S0
    public PathString (Node start, Node end) {
        this.start = start;
        this.path = new ArrayList<>();
        this.end = end;

    }

    // Constructor for S1
    public PathString (Node start, Edge e, Node end) {
        this.start = start;
        this.path = new ArrayList<>();
        this.path.add(e.getLabel());
        this.end = end;
    }

    // Constructor for Sn
    public PathString (Node start, PathString pathA, PathString pathB, Node end) {
        this.start = start;
        this.path = new ArrayList<>();
        this.path.addAll(pathA.getPath());
        this.path.addAll(pathB.getPath());
        this.end = end;
    }

    @Override
    public Node first() { 
        return this.start;
    }

    @Override
    public Node last() { 
        return this.end;
    }

    @Override
    public int lenght() { 
        return this.path.size();
    }

    @Override
    public Path join(Path pathB) {
        // if (this.isLinkeable(pathB)) {
        //     return new Path(this.start, this, (Path) pathB , end);
        // }
        return null;
    }

    @Override
    public void insertEdge(Edge e) {
        this.path.add(e.getLabel());
        this.end = e.getTarget();
    }

    @Override
    public void insertNode(Node n) {
        // Only usable when extract S0
        this.start = n;
        this.end = n;
    }

    @Override
    public boolean isLinkeable(Path that) {
        return this.end.equals(that.first());
    }

    public List<String> getPath() {
        return this.path;
    }

    @Override
    public boolean isTrail() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isTrail'");
    }

    @Override
    public String getLabelAt(int pos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'edgeLabelAt'");
    }

    @Override
    public String getIdAt(int pos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getIdAt'");
    }
    
}
