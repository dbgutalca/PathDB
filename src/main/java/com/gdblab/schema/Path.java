/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.schema;

import java.util.ArrayList;
import java.util.List;

import com.gdblab.execution.Context;

/**
 *
 * @author ramhg
 */
public class Path extends GraphObject implements PathInterface {

    private Node start;
    private ArrayList<String> sequence; // This is the sequence in string format of [ID_NODE_1, ID_EDGE_1, ID_NODE_2 ...
    private Node end;

    public Path() {
        super("", "");
        this.start = null;
        this.sequence = new ArrayList<>();
        this.end = null;
    }

    // Constructor for S0
    public Path (Node start) {
        super("", "");
        this.start = start;
        this.sequence = new ArrayList<>();
        this.end = start;

    }

    // Constructor for S1
    public Path (Edge e) {
        super("", "");
        this.start = e.getSource();
        this.sequence = new ArrayList<>();
        this.sequence.add(e.getSource().getId());
        this.sequence.add(e.getId());
        this.sequence.add(e.getTarget().getId());
        this.end = e.getTarget();
    }

    // Constructor for Sn
    public Path (Node start, Path pathA, Path pathB, Node end) {
        super("", "");
        this.start = start;
        this.sequence = new ArrayList<>();
        this.sequence.addAll(pathA.getSequence());
        this.sequence.addAll(pathB.getSequence());
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
        return this.sequence.size();
    }

    @Override
    public Path join(Path pathB) {
        if (this.isLinkeable(pathB)) {
            return new Path(this.start, this, (Path) pathB , end);
        }
        return null;
    }

    @Override
    public void insertEdge(Edge e) {
        this.sequence.add(e.getSource().getId());
        this.sequence.add(e.getId());
        this.sequence.add(e.getTarget().getId());
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

    public List<String> getSequence() {
        return this.sequence;
    }

    @Override
    public boolean isTrail() {
        
        int n = sequence.size();
        
        for (int i = 1; i < n - 1; i += 2) {
            String edge = sequence.get(i);
            
            for (int j = i + 2; j < n; j += 2) {
                if (edge.equals(sequence.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isLabelAt(int pos, String label) {
        return Context.getInstance().getGraph().getEdge(this.sequence.get(pos), label) == null ? false : true;
    }

    @Override
    public String getIdAt(int pos) {
        return sequence.get(pos);
    }
    
    @Override
    public String toString() {
        String seq = "";
        for (String s : sequence) {
            seq += s + " ";
        }
        return seq;
    }
}
