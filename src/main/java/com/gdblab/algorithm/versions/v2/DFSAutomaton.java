package com.gdblab.algorithm.versions.v2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.gdblab.algorithm.automata.RegexMatcher;
import com.gdblab.algorithm.versions.utils.Algorithm;
import com.gdblab.execution.Context;
import com.gdblab.graph.Graph;
import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.Node;
import com.gdblab.graph.schema.Path;

public class DFSAutomaton implements Algorithm {

    private RegexMatcher matcher;
    private int limit;
    private int maxAppearances;
    private int counter;
    private String startNodeProp;
    private String startNodeValue;

    public DFSAutomaton(String regex) {
        this.matcher = new RegexMatcher(regex);
        this.limit = Context.getInstance().getLimit();
        this.maxAppearances = Context.getInstance().getMaxRecursion();
        this.counter = 0;
        this.startNodeProp = Context.getInstance().getStartNodeProp();
        this.startNodeValue = Context.getInstance().getStartNodeValue();
    }

    @Override
    public void execute() {
        checkZeroPaths();
        Path iterPath = new Path("");

        // Set<String> visitedEdges = new HashSet<>();
        // while (nodes.hasNext()) {
        //     Node node = nodes.next();
        //     trail(node, visitedEdges, iterPath);
        // }
        Integer semantic = Context.getInstance().getSemantic();
        switch (semantic) {
            case 1:
                Map<String, Integer> visitCount = new HashMap<>();
                if (this.startNodeProp.equals("")) {
                    Iterator<Node> nodes = Graph.getGraph().getNodeIterator();
                    while (nodes.hasNext()) {
                        Node node = nodes.next();
                        walk(node, visitCount, iterPath);
                    }
                } else {
                    Node n = Graph.getGraph().getNode(this.startNodeValue);
                    walk(n, visitCount, iterPath);
                }
                break;
            case 2:
                Set<String> visitedEdges = new HashSet<>();
                if (this.startNodeProp.equals("")) {
                    Iterator<Node> nodes = Graph.getGraph().getNodeIterator();
                    while (nodes.hasNext()) {
                        Node node = nodes.next();
                        trail(node, visitedEdges, iterPath);
                    }
                } else {
                    Node n = Graph.getGraph().getNode(this.startNodeValue);
                    trail(n, visitedEdges, iterPath);
                }
                break;
            case 3:
                Set<String> simpleVisitedNodes = new HashSet<>();
                if (this.startNodeProp.equals("")) {
                    Iterator<Node> nodes = Graph.getGraph().getNodeIterator();
                    while (nodes.hasNext()) {
                        Node node = nodes.next();
                        simple(node, simpleVisitedNodes, iterPath);
                    }
                } else {
                    Node n = Graph.getGraph().getNode(this.startNodeValue);
                    simple(n, simpleVisitedNodes, iterPath);
                }
                break;
        }
    }

    private void walk(Node node, Map<String, Integer> visitCount, Path iterPath) {
        if (this.counter >= this.limit) {
            return;
        }
        Iterator<Edge> neighbours = Graph.getGraph().getNeighbours(node.getId()).iterator();

        while (neighbours.hasNext()) {
            Edge edge = neighbours.next();

            if (visitCount.getOrDefault(edge.getId(), 0) <= this.maxAppearances) {
                iterPath.insertEdge(edge);

                if (this.matcher.match(iterPath.getStringEdgeSequence()) == "Accepted" && this.counter < this.limit) {
                    Path newPath = new Path("");
                    newPath.setSequence(iterPath.getSequence());
                    printPath(newPath);
                    walk(edge.getTarget(), visitCount, iterPath);
                } else if (this.matcher.match(iterPath.getStringEdgeSequence()) == "Substring") {
                    walk(edge.getTarget(), visitCount, iterPath);
                }

                iterPath.pop();
            }
        }

        return;
    }

    private void trail(Node node, Set<String> visitedEdges, Path iterPath) {
        if (this.counter >= this.limit) {
            return;
        }

        Iterator<Edge> neighbours = Graph.getGraph().getNeighbours(node.getId()).iterator();

        while (neighbours.hasNext()) {
            Edge edge = neighbours.next();

            if (visitedEdges.add(edge.getId())) {
                iterPath.insertEdge(edge);

                if (this.matcher.match(iterPath.getStringEdgeSequenceAscii()) == "Accepted" && this.counter < this.limit) {
                    Path newPath = new Path("");
                    newPath.setSequence(iterPath.getSequence());
                    printPath(newPath);
                    trail(edge.getTarget(), visitedEdges, iterPath);
                } else if (this.matcher.match(iterPath.getStringEdgeSequenceAscii()) == "Substring") {
                    trail(edge.getTarget(), visitedEdges, iterPath);
                }

                iterPath.pop();
                visitedEdges.remove(edge.getId());
            }
        }

        return;
    }

    private void simple(Node node, Set<String> visitedNodes, Path iterPath) {
        if (this.counter >= this.limit) {
            return;
        }
        visitedNodes.add(node.getId());

        Iterator<Edge> neighbours = Graph.getGraph().getNeighbours(node.getId()).iterator();

        while (neighbours.hasNext()) {
            Edge edge = neighbours.next();

            if (edge.getSource().getId().equals(node.getId()) && !visitedNodes.contains(edge.getTarget().getId())) {
                iterPath.insertEdge(edge);

                if (this.matcher.match(iterPath.getStringEdgeSequence()) == "Accepted" && this.counter < this.limit) {
                    Path newPath = new Path("");
                    newPath.setSequence(iterPath.getSequence());
                    printPath(newPath);
                    simple(edge.getTarget(), visitedNodes, iterPath);
                } else if (this.matcher.match(iterPath.getStringEdgeSequence()) == "Substring") {
                    simple(edge.getTarget(), visitedNodes, iterPath);
                }

                iterPath.pop();
            }
        }

        visitedNodes.remove(node.getId());
    }

    @Override
    public void checkZeroPaths() {
        if (this.matcher.match("") == "Accepted") {
            Iterator<Node> it = Graph.getGraph().getNodeIterator();

            while (it.hasNext()) {
                Node node = it.next();
                Path path = new Path("");
                path.insertNode(node);
                this.printPath(path);
            }
        }
    }

    @Override
    public void printPath(Path p) {

        if (this.counter < this.limit) {
            // System.out.print("Pathss #" + counter + " - ");
            // for (GraphObject go : p.getSequence()) {
            //     if (go instanceof Edge) {
            //         System.out.print(go.getId() + "(" + go.getLabel() + ") ");
            //     } else {
            //         System.out.print(go.getId() + " ");
            //     }
            // }
            // System.out.println();
        }

        this.counter++;
    }

    @Override
    public int getTotalPaths() {
        return counter;
    }
}
