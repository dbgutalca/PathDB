package com.gdblab.algorithm.versions.v2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.gdblab.algorithm.automata.RegexMatcher;
import com.gdblab.algorithm.versions.utils.Algorithm;
import com.gdblab.algorithm.versions.utils.PathWithGOCount;
import com.gdblab.execution.Context;
import com.gdblab.graph.Graph;
import com.gdblab.graph.schema.Edge;
import com.gdblab.graph.schema.Node;
import com.gdblab.graph.schema.Path;

public class BFSAutomaton implements Algorithm {

    private RegexMatcher matcher;
    private int limit;
    private int maxAppearances;
    private int counter;
    private String startNodeProp;
    private String startNodeValue;

    public BFSAutomaton(String regex) {
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
        // while (nodes.hasNext()) {
        //     Node node = nodes.next();
        //     trail(node);
        // }

        Integer semantic = Context.getInstance().getSemantic();
        switch (semantic) {
            case 1:
                if (this.startNodeProp.equals("")) {
                    Iterator<Node> nodes = Graph.getGraph().getNodeIterator();
                    while (nodes.hasNext()) {
                        Node node = nodes.next();
                        walk(node);
                    }
                } else {
                    Node n = Graph.getGraph().getNode(this.startNodeValue);
                    walk(n);
                }
                break;
            case 2:
                if (this.startNodeProp.equals("")) {
                    Iterator<Node> nodes = Graph.getGraph().getNodeIterator();
                    while (nodes.hasNext()) {
                        Node node = nodes.next();
                        trail(node);
                    }
                } else {
                    Node n = Graph.getGraph().getNode(this.startNodeValue);
                    trail(n);
                }
                break;
            case 3:
                if (this.startNodeProp.equals("")) {
                    Iterator<Node> nodes = Graph.getGraph().getNodeIterator();
                    while (nodes.hasNext()) {
                        Node node = nodes.next();
                        simple(node);
                    }
                } else {
                    Node n = Graph.getGraph().getNode(this.startNodeValue);
                    simple(n);
                }
                break;
        }
    }

    private void walk(Node node) {
        Queue<PathWithGOCount> queue = new LinkedList<>();

        Iterator<Edge> neighbours = Graph.getGraph().getNeighbours(node.getId()).iterator();

        while (neighbours.hasNext()) {
            Edge e = neighbours.next();
            Path p = new Path("", e);
            Map<String, Integer> visitCountMap = new HashMap<>();
            visitCountMap.put(e.getId(), 1);
            queue.add(new PathWithGOCount(p, visitCountMap));
        }

        while (!queue.isEmpty()) {
            if (this.counter >= this.limit) {
                return;
            }
            PathWithGOCount current = queue.poll();
            Path currentPath = current.getPath();
            Map<String, Integer> currentVisitCount = current.getVisitedGOCount();

            if (this.matcher.match(currentPath.getStringEdgeSequence()) == "Accepted" && this.counter < this.limit) {
                printPath(currentPath);
            } else if (this.matcher.match(currentPath.getStringEdgeSequence()) == "Rejected") {
                continue;
            }

            neighbours = Graph.getGraph().getNeighbours(currentPath.last().getId()).iterator();

            while (neighbours.hasNext()) {
                Edge e = neighbours.next();

                if (currentVisitCount.getOrDefault(e.getId(), 0) <= this.maxAppearances) {
                    Path p = new Path("", currentPath.getEdgeSequence());
                    p.insertEdge(e);
                    Map<String, Integer> visitCountMap = new HashMap<>(currentVisitCount);
                    visitCountMap.put(e.getId(), visitCountMap.getOrDefault(e.getId(), 0) + 1);
                    queue.add(new PathWithGOCount(p, visitCountMap));
                }
            }
        }
    }

    private void trail(Node node) {
        Queue<PathWithGOCount> queue = new LinkedList<>();

        Iterator<Edge> neighbours = Graph.getGraph().getNeighbours(node.getId()).iterator();

        while (neighbours.hasNext()) {
            Edge e = neighbours.next();
            Path p = new Path("", e);
            Map<String, Integer> visitCountMap = new HashMap<>();
            visitCountMap.put(e.getId(), 1);
            queue.add(new PathWithGOCount(p, visitCountMap));
        }

        while (!queue.isEmpty()) {
            if (this.counter >= this.limit) {
                return;
            }
            PathWithGOCount current = queue.poll();
            Path currentPath = current.getPath();
            Map<String, Integer> currentVisitCount = current.getVisitedGOCount();

            if (this.matcher.match(currentPath.getStringEdgeSequenceAscii()) == "Accepted" && this.counter < this.limit) {
                this.printPath(currentPath);
            } else if (this.matcher.match(currentPath.getStringEdgeSequenceAscii()) == "Rejected") {
                continue;
            }

            neighbours = Graph.getGraph().getNeighbours(currentPath.last().getId()).iterator();

            while (neighbours.hasNext()) {
                Edge e = neighbours.next();

                if (currentVisitCount.getOrDefault(e.getId(), 0) < 1) {
                    Path p = new Path("", currentPath.getEdgeSequence());
                    p.insertEdge(e);
                    Map<String, Integer> visitCountMap = new HashMap<>(currentVisitCount);
                    visitCountMap.put(e.getId(), visitCountMap.getOrDefault(e.getId(), 0) + 1);
                    queue.add(new PathWithGOCount(p, visitCountMap));
                }
            }
        }
    }

    private void simple(Node node) {
        Queue<PathWithGOCount> queue = new LinkedList<>();

        Iterator<Edge> neighbours = Graph.getGraph().getNeighbours(node.getId()).iterator();

        while (neighbours.hasNext()) {
            Edge e = neighbours.next();
            Path p = new Path("", e);
            Map<String, Integer> visitCountMap = new HashMap<>();
            visitCountMap.put(e.getTarget().getId(), 1);
            queue.add(new PathWithGOCount(p, visitCountMap));
        }

        while (!queue.isEmpty()) {
            if (this.counter >= this.limit) {
                return;
            }
            PathWithGOCount current = queue.poll();
            Path currentPath = current.getPath();
            Map<String, Integer> currentVisitCount = current.getVisitedGOCount();

            if (this.matcher.match(currentPath.getStringEdgeSequence()) == "Accepted" && this.counter < this.limit) {
                printPath(currentPath);
            } else if (this.matcher.match(currentPath.getStringEdgeSequence()) == "Rejected") {
                continue;
            }

            neighbours = Graph.getGraph().getNeighbours(currentPath.last().getId()).iterator();

            while (neighbours.hasNext()) {
                Edge e = neighbours.next();

                if (currentVisitCount.getOrDefault(e.getTarget().getId(), 0) < 1) {
                    Path p = new Path("", currentPath.getEdgeSequence());
                    p.insertEdge(e);
                    Map<String, Integer> visitCountMap = new HashMap<>(currentVisitCount);
                    visitCountMap.put(e.getId(), visitCountMap.getOrDefault(e.getId(), 0) + 1);
                    queue.add(new PathWithGOCount(p, visitCountMap));
                }
            }
        }
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
            // System.out.print("Path #" + counter + 1 + " - ");
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
