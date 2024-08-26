package com.gdblab.schema;

import java.util.ArrayList;
import java.util.List;

public class PathString implements PathInterface {

    private Node start;
    private ArrayList<String> path; // This is the path in string format of Label_1, Label_2, Label_3, ...
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
    public PathInterface join(PathInterface pathB) {
        if (this.isJoinable(pathB)) {
            return new PathString(this.start, this, (PathString) pathB , end);
        }
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
    public boolean isJoinable(PathInterface that) {
        return this.end.equals(that.first());
    }

    public List<String> getPath() {
        return this.path;
    }
    
}
