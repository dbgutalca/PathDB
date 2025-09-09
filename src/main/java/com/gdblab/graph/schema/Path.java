/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdblab.graph.schema;

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

    // Si se transformase a solamente una lista de edges
    // seria imposible almacenar paths de largo 0
    // ya que el Edge como objeto debe si o si tener un source y un target
    // Podria ser el target null pero traería problemas con los joins
    // y habría que modificar demasiado codigo para implementar solo
    // la lista de Edges en vez de una lista de Nodes y Edges.
    private List<GraphObject> sequence;

    public Path(final String id, final String label, final Integer length) {
        super(id, label, length);
        this.sequence = new ArrayList<>();
    }

    public Path(final String id) {
        super(id, 0);
        this.sequence = new ArrayList<>();
    }

    public Path(final String id, Integer length) {
        super(id, length);
        this.sequence = new ArrayList<>();
    }
    
    public Path (final String id, final Edge edge) {
        super(id, 1); 
        this.sequence = new ArrayList<>();
        this.insertEdge(edge);
    }
    
    public Path(final String id, final Node node) {
        super(id, 0);
        this.sequence = new ArrayList<>();
        this.insertNode(node);
    }

    public Path(final String id, final List<Edge> edges) {
        super(id, edges.size());
        this.sequence = new ArrayList<>();
        for (final Edge e : edges) {
            this.insertEdge(e);
        }
    }
    
    public Path(final String id, final boolean reverse, final List<GraphObject> sequence, final Integer length) {
        super(id, length);
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
                edges += edge.getLabel();
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
        if (pos >= 0 && pos < seq.size()) {
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
        final Path new_path = new Path(UUID.randomUUID().toString(), "path", seq.size());

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

    public int lenght() {
        return sequence.size();
    }
    
    public void setSequence(List<GraphObject> sequence) {
        this.sequence = new ArrayList<GraphObject>(sequence);
    }
    
    public void appendSequence(List<GraphObject> sequence) {
        for (int i = 1; i < sequence.size(); i++) {
            this.sequence.add(sequence.get(i));
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Path p2) {

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

    public GraphObject pop() {
        if (sequence.size() == 1) {
            return sequence.remove(0);
        }
        else if (sequence.size() == 3) {
            sequence.remove(sequence.size() - 1);
            GraphObject r = sequence.remove(sequence.size() - 1);
            sequence.remove(sequence.size() - 1);
            return r;
        }
        else {
            sequence.remove(sequence.size() - 1);
            GraphObject r = sequence.remove(sequence.size() - 1);
            return r;
        }
    }

    public ArrayList<String> getListIDEdgeSequence() {
        ArrayList<String> ids = new ArrayList<>();
        for (int i = 0; i < sequence.size(); i++) {
            if (sequence.get(i) instanceof Edge edge) {
                ids.add(edge.getId());
            }
        }
        return ids;
    }

    public ArrayList<String> getListIDNodeSequence() {
        ArrayList<String> ids = new ArrayList<>();
        for (int i = 0; i < sequence.size(); i++) {
            if (sequence.get(i) instanceof Node node) {
                ids.add(node.getId());
            }
        }
        return ids;
    }

    public boolean isTrail(Path pathB) {
        return this.getListIDEdgeSequence().stream().noneMatch(pathB.getListIDEdgeSequence()::contains);
    }

    public boolean isAcyclic (Path pathB) {
        List<String> res = pathB.getListIDNodeSequence().subList(1, pathB.getListIDNodeSequence().size());
        return this.getListIDNodeSequence().stream().noneMatch(res::contains);
    }

    public boolean isSelfAcyclic() {
        return this.getListIDNodeSequence().stream().distinct().count() == this.getListIDNodeSequence().size();
    }

    public boolean isSimplePath (Path pathB) {
        ArrayList<String> pathBNodeSequence = pathB.getListIDNodeSequence();

        if (pathBNodeSequence.size() > 1) {
            List<String> res = pathB.getListIDNodeSequence().subList(1, pathB.getListIDNodeSequence().size() - 1);
            return this.getListIDNodeSequence().stream().noneMatch(res::contains);
        }
        else {
            return this.isSelfSimplePath();
        }
    }

    public boolean isSelfSimplePath() {
        return this.getListIDNodeSequence().stream().distinct().count() == (this.getListIDNodeSequence().size() - 1);
    }


    public Integer getSumEdges(Path pathB) {
        return this.getEdgeLength() + pathB.getEdgeLength();
    }

    public Integer getEdgeLength() {
        return this.getLength();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"sequence\": [");

        List<GraphObject> seq = this.getSequence();
        for (int i = 0; i < seq.size(); i++) {
            sb.append("    ").append(seq.get(i).toString());
            if (i < seq.size() - 1) {
                sb.append(",");
            }
            sb.append("");
        }

        sb.append("]");
        sb.append("}");
        return sb.toString();
    }
}
