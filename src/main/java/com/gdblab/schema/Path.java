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
public class Path extends GraphObject implements PathInterface {

    // Si se transformase a solamente una lista de edges
    // seria imposible almacenar paths de largo 0
    // ya que el Edge como objeto debe si o si tener un source y un target
    // Podria ser el target null pero traería problemas con los joins
    // y habría que modificar demasiado codigo para implementar solo
    // la lista de Edges en vez de una lista de Nodes y Edges.
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

    @Override
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

    @Override
    public void insertNode(final Node node) {
        if (sequence.isEmpty()) {
            sequence.add(node);
        }
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

    public Node first() {
        return (Node) this.sequence.get(0);
    }

    public Node last() {
        return (Node) this.sequence.get( this.sequence.size() - 1 );
    }

    public Edge getEdgeAt(final int pos) {
        final ArrayList<Edge> seq = this.getEdgeSequence();
        if (seq.size() >= pos) {
            return seq.get(pos);
        }
        return null;
    }

    public boolean isNodeLinkable(final Path path2) {
        return last().getId().equals(path2.first().getId());
    }

    public Edge isEdgeLinkable(final Path path2, final MemoryGraph graph) {
        final String node1_id = last().getId();
        final String node2_id = path2.first().getId();

        return graph.getEdge(node1_id, node2_id);
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


    @Override
    public int lenght() {
        return sequence.size();
    }
    
    @Override
    public Path join(Path pathB) {
        if (isLinkeable(pathB)) {
            Path path = new Path(UUID.randomUUID().toString());
            path.setSequence(this.sequence);
            path.appendSequence(pathB.getSequence());
            return path;
        }
        return  null;
    }

    @Override
    public boolean isTrail() {

        int n = sequence.size();
        
        for (int i = 1; i < n - 1; i += 2) {
            GraphObject edge = sequence.get(i);
            
            for (int j = i + 2; j < n; j += 2) {
                if (edge.equals(sequence.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isLinkeable(Path pathB) {
        return this.last().getId().equals(pathB.first().getId());
    }

    @Override
    public String getLabelAt(int pos) {
        return this.sequence.get(pos).getLabel();
    }
}
