package com.gdblab.algorithm.versions.v1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gdblab.algorithm.versions.utils.Algorithm;
import com.gdblab.execution.Context;
import com.gdblab.graph.Graph;
import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.GraphObject;
import com.gdblab.graph.schema.Node;
import com.gdblab.graph.schema.Path;

public class DFSRegex implements Algorithm {
    private Pattern pattern;
    private int limit;
    private int maxAppearances;
    private int counter;


    public DFSRegex(String regex) {
        this.pattern = Pattern.compile(regex);
        this.limit = Context.getInstance().getLimit();
        this.maxAppearances = Context.getInstance().getMaxRecursion();
        this.counter = 0;
    }

    @Override
    public void execute() {
        checkZeroPaths();
        Iterator<Node> nodes = Graph.getGraph().getNodeIterator();
        Path iterPath = new Path("");
        Integer semantic = Context.getInstance().getSemantic();
        
        switch (semantic) {
            case 1:
                Map<String, Integer> visitCount = new HashMap<>();
                while (nodes.hasNext()) {
                    Node node = nodes.next();
                    walk(node, visitCount, iterPath);
                }
                break;
            case 2:
                Set<String> visitedEdges = new HashSet<>();
                while (nodes.hasNext()) {
                    Node node = nodes.next();
                    trail(node, visitedEdges, iterPath);
                }
                break;
            case 3:
                Set<String> simpleVisitedNodes = new HashSet<>();
                while (nodes.hasNext()) {
                    Node node = nodes.next();
                    simple(node, simpleVisitedNodes, iterPath);
                }
                break;
            case 4:
                Set<String> acyclicVisitedNodes = new HashSet<>();
                while (nodes.hasNext()) {
                    Node node = nodes.next();
                    acyclicVisitedNodes.add(node.getId());
                    acyclic(node, acyclicVisitedNodes, iterPath);
                    acyclicVisitedNodes.remove(node.getId());
                }
                break;
        }
    }

    private void walk(Node node, Map<String, Integer> visitCount, Path iterPath) {
        visitCount.put(node.getId(), visitCount.getOrDefault(node.getId(), 0) + 1);
        Iterator<Edge> neighbours = Graph.getGraph().getNeighbours(node.getId()).iterator();

        while (neighbours.hasNext()) {
            Edge edge = neighbours.next();

            if (visitCount.getOrDefault(edge.getTarget().getId(), 0) <= this.maxAppearances) {
                iterPath.insertEdge(edge);
                Matcher m = pattern.matcher(iterPath.getStringEdgeSequenceAscii());
                if (m.matches()) {
                    Path newPath = new Path("");
                    newPath.setSequence(iterPath.getSequence());
                    this.printPath(newPath);
                }
                walk(edge.getTarget(), visitCount, iterPath);
                iterPath.pop();
            }
        }
        visitCount.put(node.getId(), visitCount.getOrDefault(node.getId(), 0) - 1);
    }

    private void trail(Node node, Set<String> visitedEdges, Path iterPath) {

        Iterator<Edge> neighbours = Graph.getGraph().getNeighbours(node.getId()).iterator();

        while (neighbours.hasNext()) {
            Edge edge = neighbours.next();

            if (!visitedEdges.contains(edge.getId())) {
                iterPath.insertEdge(edge);
                visitedEdges.add(edge.getId());

                Matcher m = pattern.matcher(iterPath.getStringEdgeSequenceAscii());
                if (m.matches()) {
                    Path newPath = new Path("");
                    newPath.setSequence(iterPath.getSequence());
                    this.printPath(newPath);
                }

                trail(edge.getTarget(), visitedEdges, iterPath);

                iterPath.pop();
                visitedEdges.remove(edge.getId());
            }
        }

        return;
    }

    private void simple(Node node, Set<String> visitedNodes, Path iterPath) {
        
        Iterator<Edge> neighbours = Graph.getGraph().getNeighbours(node.getId()).iterator();

        while (neighbours.hasNext()) {
            Edge edge = neighbours.next();

            if ( visitedNodes.add(edge.getTarget().getId()) ) {
                iterPath.insertEdge(edge);

                Matcher m = pattern.matcher(iterPath.getStringEdgeSequence());
                if (m.matches()) this.printPath(iterPath);

                simple(edge.getTarget(), visitedNodes, iterPath);

                visitedNodes.remove(edge.getTarget().getId());
                iterPath.pop();
            }
        }
    }

    private void acyclic(Node node, Set<String> visitedNodes, Path iterPath) {
        
        Iterator<Edge> neighbours = Graph.getGraph().getNeighbours(node.getId()).iterator();

        while (neighbours.hasNext()) {
            Edge edge = neighbours.next();

            if ( visitedNodes.add(edge.getTarget().getId()) ) {
                iterPath.insertEdge(edge);

                Matcher m = pattern.matcher(iterPath.getStringEdgeSequence());
                if (m.matches()) this.printPath(iterPath);

                acyclic(edge.getTarget(), visitedNodes, iterPath);

                visitedNodes.remove(edge.getTarget().getId());
                iterPath.pop();
            }
        }
    }

    @Override
    public void checkZeroPaths() {
        if (pattern.matcher("").matches()) {
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
            System.out.print("Path #" + counter + " - ");
            for (GraphObject go : p.getSequence()) {
                if (go instanceof Edge) {
                    System.out.print(go.getId() + "(" + go.getLabel() + ") ");
                }
                else {
                    System.out.print(go.getId() + " ");
                }
            }
            System.out.println();
        }    

        counter++;
    }
    
    @Override
    public int getTotalPaths() {
        return counter;
    }
}
