/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.schema;

import com.gdblab.schema.impl.MemoryGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author ramhg
 */
public class Path extends GraphObject {

    private List<GraphObject> sequence;

    public Path(final String id, final String label) {
        super(id, label);
        this.sequence = new ArrayList<>();
    }

    public Path(final String id) {
        super(id);
        this.sequence = new ArrayList<>();
    }
    
    public Path (final String id, final Edge edge) {
        super(id);
        this.sequence = new ArrayList<>();
        this.insertEdge(edge);
    }
    
    public Path(final String id, final Node node) {
        super(id);
        this.sequence = new ArrayList<>();
        this.insertNode(node);
    }

    public Path(final String id, final List<Edge> edges) {
        super(id);
        this.sequence = new ArrayList<>();
        for (final Edge e : edges) {
            this.insertEdge(e);
        }
    }
    
    public Path(final String id, final boolean reverse, final List<GraphObject> sequence) {
        super(id);
        this.sequence = new ArrayList<GraphObject>(sequence);
        Collections.reverse(this.sequence);
    }

    public List<GraphObject> getSequence() {
        return sequence;
    }

    public void insertEdge(final Edge edge) {
        if (sequence.isEmpty()) {
            sequence.add(edge.getSource());
            sequence.add(edge);
            sequence.add(edge.getTarget());
        } else if (Objects.equals(this.last().getId(), edge.getSource().getId())) {
            sequence.add(edge);
            sequence.add(edge.getTarget());
        }
    }

    public void insertNode(final Node node) {
        if (sequence.isEmpty()) {
            sequence.add(node);
        }
    }

    public List<Node> getNodeSequence() {
        final ArrayList<Node> nodes = new ArrayList<>();
        for (int i = 0; i < sequence.size(); i++) {
            if (sequence.get(i) instanceof Node node) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    public String getStringNodeSequence() {
        String nodes = "";
        for (int i = 0; i < sequence.size(); i++) {
            if (sequence.get(i) instanceof Node node) {
                nodes += node.getId() + " ";
            }
        }
        return nodes;
    }

    public ArrayList<Edge> getEdgeSequence() {
        final ArrayList<Edge> edges = new ArrayList<>();
        for (int i = 0; i < sequence.size(); i++) {
            if (sequence.get(i) instanceof Edge edge) {
                edges.add(edge);
            }
        }
        return edges;
    }

    public String getStringEdgeSequence() {
        String edges = "";
        for (int i = 0; i < sequence.size(); i++) {
            if (sequence.get(i) instanceof Edge edge) {
                edges += edge.getId() + " ";
            }
        }
        return edges;
    }
    
    public String getStringSequence() {
        String seq = "";
        for (int i = 0; i < sequence.size(); i++) {
            seq += sequence.get(i).getLabel() + " ";
        }
        return seq;
    }

    public int getNodesAmount() {
        return getNodeSequence().size();
    }

    public Node first() {
        return (Node) this.sequence.get(0);
    }

    public Node last() {
        return (Node) this.sequence.get( this.sequence.size() - 1 );
    }

    public Node getNodeAt(final int pos) {
        final List<Node> seq = this.getNodeSequence();
        if (seq.size() >= pos) {
            return seq.get(pos);
        }
        return null;
    }

    public Edge getEdgeAt(final int pos) {
        final ArrayList<Edge> seq = this.getEdgeSequence();
        if (seq.size() >= pos) {
            return seq.get(pos);
        }
        return null;
    }

    public Path subPath(final int i, final int j) {
        final Node first = getNodeAt(i);
        final Node last = getNodeAt(j);
        final ArrayList<Edge> seq = this.getEdgeSequence();
        final Path new_path = new Path(UUID.randomUUID().toString(), "path");

        boolean last_reached = false;
        boolean first_reached = false;

        if (i == j) {
            new_path.insertNode(first);
        }

        for (int k = 0; k < seq.size() && !last_reached; k++) {
            if (seq.get(k).getSource().getId().equals(first.getId()) && !first_reached) {
                first_reached = true;
            }
            if (seq.get(k).getTarget().getId().equals(last.getId())) {
                last_reached = true;
            }

            if (first_reached) {
                new_path.insertEdge(seq.get(k));
            }
        }
        return new_path;
    }

    public Path leftSubPath(final int i) {
        return subPath(0, i);
    }

    public Path rightSubPath(final int j) {
        return subPath(j, this.getNodesAmount() - 1);
    }

    public boolean isNodeLinkable(final Path path2) {
        return last().getId().equals(path2.first().getId());
    }

    public Edge isEdgeLinkable(final Path path2, final MemoryGraph graph) {
        final String node1_id = last().getId();
        final String node2_id = path2.first().getId();

        return graph.getEdge(node1_id, node2_id);
    }

    public int lenght() {
        return sequence.size();
    }
    
    public void setSequence(List<GraphObject> sequence) {
        this.sequence = new ArrayList<GraphObject>(sequence);
    }
    
    public void appendSequence(List<GraphObject> sequence) {
        for (GraphObject go: sequence)
            this.sequence.add(go);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Path) {

            final Path p2 = (Path) obj;

            final List<GraphObject> sequence1 = this.getSequence();
            final List<GraphObject> sequence2 = p2.getSequence();

            if (sequence1.size() != sequence2.size()) {
                return false;
            }

            for (int i = 0; i < sequence1.size(); i++) {
                if (!sequence1.get(i).getId().equals(sequence2.get(i).getId())) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

}
