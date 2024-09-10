/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.schema;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import com.gdblab.execution.Context;
import com.gdblab.queryplan.util.Utils;

/**
 *
 * @author ramhg
 */
public class Path extends GraphObject implements PathInterface {

    private Node start;
    private LinkedList<String> nodeSequence; // this is a sequence of nodes
    private LinkedHashSet<String> edgeSequence; // this is a sequence of edges
    private Node end;

    public Path() {
        super("", "");
        this.start = null;
        this.nodeSequence = new LinkedList<>();
        this.edgeSequence = new LinkedHashSet<>();
        this.end = null;
    }

    // Constructor for S0
    public Path (Node start) {
        super("", "");
        this.start = start;
        this.nodeSequence = new LinkedList<>();
        this.nodeSequence.add(start.getId());
        this.edgeSequence = new LinkedHashSet<>();
        this.end = start;

    }

    // Constructor for S1
    public Path (Edge e) {
        super("", "");
        this.start = e.getSource();
        this.nodeSequence = new LinkedList<>();
        this.edgeSequence = new LinkedHashSet<>();
         
        this.nodeSequence.add(e.getSource().getId());
        this.edgeSequence.add(e.getId());
        this.nodeSequence.add(e.getTarget().getId());
        this.end = e.getTarget();
    }

    // Constructor for Sn
    public Path (Path pathA, Path pathB) {
        super("", "");
        this.start = pathA.first();
        this.nodeSequence = new LinkedList<>();
        this.edgeSequence = new LinkedHashSet<>();

        this.nodeSequence.addAll(pathA.getNodeSequence());
        LinkedList<String> nodeSequenceB = pathB.getNodeSequence();
        for (int i = 1; i < nodeSequenceB.size(); i++) this.nodeSequence.add(nodeSequenceB.get(i));

        this.edgeSequence.addAll(pathA.getEdgeSequence());
        this.edgeSequence.addAll(pathB.getEdgeSequence()); 

        this.end = pathB.last();
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
        return this.nodeSequence.size() + this.edgeSequence.size();
    }

    @Override
    public Path join(Path pathB) {
        if (this.isLinkeable(pathB) && this.isTrail(pathB)) {
            return new Path(this, pathB);
        }
        return null;
    }

    @Override
    public void insertEdge(Edge e) {
        if (this.getEdgeSequence().isEmpty()) this.nodeSequence.add(e.getSource().getId());

        this.edgeSequence.add(e.getId());
        this.nodeSequence.add(e.getTarget().getId());
        
        this.end = e.getTarget();
    }

    @Override
    public void insertNode(Node n) {
        // Only usable when extract S0
        this.start = n;
        this.nodeSequence.addFirst(n.getId());
        this.end = n;
    }

    @Override
    public boolean isLinkeable(Path that) {
        return this.end.equals(that.first());
    }

    public LinkedList<String> getNodeSequence() {
        return this.nodeSequence;
    }

    public LinkedHashSet<String> getEdgeSequence() {
        return this.edgeSequence;
    }

    @Override
    public boolean isTrail(Path pathB) {
        return pathB.getEdgeSequence().stream().noneMatch(this.getEdgeSequence()::contains);
    }

    @Override
    public boolean isLabelAt(int pos, String label) {
        
        Iterator<String> it = this.edgeSequence.iterator();
        int index = 0;

        while ( it.hasNext() ){
            String edgeId = it.next();
            if (index == Math.ceil(pos / 2)) return Context.getInstance().getGraph().getEdge(edgeId, label) == null ? false : true;
            index++;
        }

        return false;
    }

    @Override
    public String getIdAt(int pos) {
        return getNodeSequence().get(pos);
    }
    
    @Override
    public String toString() {
        String seq = "";

        for (int i = 0; i < this.nodeSequence.size(); i++) {
            seq += this.nodeSequence.get(i) + " ";
            try {
                seq += this.edgeSequence.toArray()[i] + " ";
            } catch (Exception e) {
            }
        }
        return seq;
    }
}
